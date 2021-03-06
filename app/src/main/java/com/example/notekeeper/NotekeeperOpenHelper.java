package com.example.notekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotekeeperOpenHelper extends SQLiteOpenHelper {
    public static final String DATBASE_NAME="Notekeeper.db";
    public static final int DATABASE_VERSION=2;
    public NotekeeperOpenHelper(@Nullable Context context) {
        super(context, DATBASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteKeeperDatabaseContract.CourseInfoEntry.COURSE_INFO_SQL);
        db.execSQL(NoteKeeperDatabaseContract.NoteInfoEntry.NOTE_INFO_SQL);
        db.execSQL(NoteKeeperDatabaseContract.CourseInfoEntry.SQL_CREATE_INDEX1);
        db.execSQL(NoteKeeperDatabaseContract.NoteInfoEntry.SQL_CREATE_INDEX1);
        DatabaseDataWorker worker=new DatabaseDataWorker(db);
        worker.insertCourses();
        worker.insertSampleNotes();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<2)
        {
            db.execSQL(NoteKeeperDatabaseContract.CourseInfoEntry.SQL_CREATE_INDEX1);
            db.execSQL(NoteKeeperDatabaseContract.NoteInfoEntry.SQL_CREATE_INDEX1);
        }

    }
}
