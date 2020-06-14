package com.example.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
    public static final String ORIGINAL_NOTE_COURSE_ID="com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_TITLE="com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_TEXT="com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public String mOrignalcourseValue;
    public String mOriginaltitleValue;
    public String mOriginaltextValue;

    public boolean isnewlycreated=true;


    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID,mOrignalcourseValue);
        outState.putString(ORIGINAL_TITLE,mOriginaltitleValue);
        outState.putString(ORIGINAL_TEXT,mOriginaltextValue);
    }
    public void restorestate(Bundle instate)
    {
        mOrignalcourseValue=instate.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginaltitleValue=instate.getString(ORIGINAL_TITLE);
        mOriginaltextValue=instate.getString(ORIGINAL_TEXT);
    }
}
