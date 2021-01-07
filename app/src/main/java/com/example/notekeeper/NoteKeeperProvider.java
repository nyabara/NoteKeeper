package com.example.notekeeper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.notekeeper.NoteKeeperDatabaseContract.*;
import static com.example.notekeeper.NoteKeeperProviderContract.*;

public class NoteKeeperProvider extends ContentProvider {
    private NotekeeperOpenHelper mDbOpenHelper;
    public NoteKeeperProvider() {
    }
    private static UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    public static final int COURSES = 0;

    public static final int NOTES = 1;

    public static final int NOTES_EXPANDED = 2;

    static {
        sUriMatcher.addURI(Authority, Courses.PATH, COURSES);
        sUriMatcher.addURI(Authority,Notes.PATH, NOTES);
        sUriMatcher.addURI(Authority,Notes.PATH_EXPANDED, NOTES_EXPANDED);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
       mDbOpenHelper=new NotekeeperOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;
        SQLiteDatabase db=mDbOpenHelper.getReadableDatabase();
        int uriMatcher=sUriMatcher.match(uri);
        switch (uriMatcher){
            case COURSES:
                cursor=db.query(CourseInfoEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null, sortOrder);
                break;
            case NOTES:
                cursor=db.query(NoteInfoEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null, sortOrder);
                break;
            case NOTES_EXPANDED:
                cursor=expandedNotesQuery(db,projection,selection,selectionArgs,sortOrder);
        }
        return cursor;

    }

    private Cursor expandedNotesQuery(SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs,
                                      String sortOrder) {
        String[] columns=new String[projection.length];
        for (int idx=0; idx<projection.length; idx++){
            columns[idx]=projection[idx].equals(BaseColumns._ID) ||
                    projection[idx].equals(courseIdColumn.COLUMN_COURSE_ID) ?
                    NoteInfoEntry.getQName(projection[idx]):projection[idx];
        }
        String tablesWithJoin=(NoteInfoEntry.TABLE_NAME+ " JOIN " + CourseInfoEntry.TABLE_NAME +
                " ON "+ NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID)+ " = "+ CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID));
        return db.query(tablesWithJoin,columns,selection,selectionArgs,null,null,sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}