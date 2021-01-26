package com.example.notekeeper;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import static com.example.notekeeper.NoteKeeperProviderContract.*;

public class NoteBackup {
    public static final String ALL_COURSES="ALL_COURSES";
    public static final String TAG=NoteBackup.class.getSimpleName();
    public static void doBackup(Context context, String backUpCourseId){
        String[] columns={
                Notes.COLUMN_COURSE_ID,
                Notes.COLUMN_NOTE_TITLE,
                Notes.COLUMN_NOTE_TEXT};
        String selection=null;
        String[] selectionArgs=null;
        if(!backUpCourseId.equals(ALL_COURSES)){
            selection=Notes.COLUMN_COURSE_ID + "=?";
            selectionArgs=new String[] {backUpCourseId};
        }
        Cursor cursor=context.getContentResolver().query(Notes.CONTENT_URI,columns,selection,selectionArgs,null);
        int courseIdPos=cursor.getColumnIndex(Notes.COLUMN_COURSE_ID);
        int noteTextPos=cursor.getColumnIndex(Notes.COLUMN_NOTE_TEXT);
        int noteTitlePos=cursor.getColumnIndex(Notes.COLUMN_NOTE_TITLE);
        Log.i(TAG,"<<<***  BACKUP START - Thread: " + Thread.currentThread().getId()+"  ***>>>");
        while (cursor.moveToNext()){
            String courseId=cursor.getString(courseIdPos);
            String notTitle=cursor.getString(noteTitlePos);
            String noteText=cursor.getString(noteTextPos);
            if (!notTitle.equals("")){
                Log.i(TAG,"<<<*** Back up note! ***>>>"+ courseId + notTitle +noteText);
            }
        }
        Log.i(TAG,"<<<*** BACKUP COMPLETE! ***>>>");
        cursor.close();
    }
}
