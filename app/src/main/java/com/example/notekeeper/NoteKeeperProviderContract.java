package com.example.notekeeper;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteKeeperProviderContract {
    private NoteKeeperProviderContract(){}
    public static final String Authority="com.example.notekeeper.provider";
    public static final Uri AUTHORITY_URI =Uri.parse("content://" + Authority);
    protected interface coursesColumns{
        public static final String COLUMN_COURSE_TITLE="course_title";
    }
    protected interface notesColumns{
        public static final String COLUMN_NOTE_TEXT="note_text";
        public static final String COLUMN_NOTE_TITLE="note_title";
    }
    protected interface courseIdColumn{
        public static final String COLUMN_COURSE_ID="course_id";
    }
    public static final class Courses implements BaseColumns,coursesColumns,courseIdColumn {
        public static final String PATH="courses";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(AUTHORITY_URI,PATH);
    }
    public static final class Notes implements BaseColumns,courseIdColumn,notesColumns,coursesColumns{
        public static final String PATH="notes";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(AUTHORITY_URI,PATH);
        public static final String PATH_EXPANDED="notes_expanded";
        public static final Uri CONTENT_EXPANDED_URL=Uri.withAppendedPath(AUTHORITY_URI,PATH_EXPANDED);

    }
}
