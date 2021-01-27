package com.example.notekeeper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import static com.example.notekeeper.NoteKeeperProviderContract.*;

public class NoteUploader {
    private  final String TAG=NoteUploader.class.getSimpleName();
    private final Context mContext;
    private boolean mCanceled;

    public NoteUploader(Context context) {
        mContext = context;
    }
    public boolean isCanceled(){return mCanceled;}
    public void cancel(){
        Log.i(TAG,">>> !!CANCELLING!!>>>");
        mCanceled=true;
    }
    public void doUpload(Uri dataUri){
        String[] columns= {
                Notes.COLUMN_COURSE_ID,
                Notes.COLUMN_NOTE_TITLE,
                Notes.COLUMN_NOTE_TEXT
        };
        Cursor cursor=mContext.getContentResolver().query(dataUri,columns,null,null,null);
        int courseIdPos=cursor.getColumnIndex(Notes.COLUMN_COURSE_ID);
        int noteTitlePos=cursor.getColumnIndex(Notes.COLUMN_NOTE_TITLE);
        int noteTextPos=cursor.getColumnIndex(Notes.COLUMN_NOTE_TEXT);
        Log.i(TAG,">>>*** START UPLOAD -"+ dataUri+ " ***>>>");
        mCanceled=false;
        while (!mCanceled && cursor.moveToNext()){
            String courseId=cursor.getString(courseIdPos);
            String noteTitle=cursor.getString(noteTitlePos);
            String noteText=cursor.getString(noteTextPos);
            if (!noteTitle.equals("")){
                Log.i(TAG,">>>*** Uploading Notes<<<" + courseId+ "|" + noteTitle + "|" + noteText);
            }
        }


    }
}
