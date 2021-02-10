package com.example.notekeeper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import static com.example.notekeeper.NoteKeeperDatabaseContract.*;
import static com.example.notekeeper.NoteKeeperProviderContract.*;

public class NoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int ID_NOT_SET = -1;
    public static final String NOTE_ID ="com.example.notekeeper.NOTE_POSITION";
    private static final String TAG =NoteActivity.class.getSimpleName();
    public static final int LOADER_NOTES = 0;
    public static final int LOADER_COURSES = 1;
    //public static final String NOTE_POSITION = NOTE_POSITION;
    private NoteInfo mNote;
    private boolean mIsnewnote;
    private EditText mMtitle;
    private EditText mMtext;
    private Spinner mSpinner_courses;
    private int mNoteId;
    private boolean mIscancelling;
    private NoteActivityViewModel mViewModel;
    private NotekeeperOpenHelper mDbOpenHelper;
    private Cursor mNotescursor;
    private int mCourseidPos;
    private int mTitlePos;
    private int mTextPos;
    private SimpleCursorAdapter mAdapterCourses;
    private boolean mCoursesQueryFinished;
    private boolean mNotesQueryFinished;
    private Uri mNoteUri;
    private ModuleStatusView mModuleStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NotekeeperOpenHelper(this);
        mMtitle = findViewById(R.id.note_title_text);
        mMtext = findViewById(R.id.note_body_text);
        ViewModelProvider viewModelProvider=new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel=viewModelProvider.get(NoteActivityViewModel.class);
        if (mViewModel.isnewlycreated&&savedInstanceState!=null)
            mViewModel.restoreState(savedInstanceState);


        mViewModel.isnewlycreated=false;
        mSpinner_courses = findViewById(R.id.spinner_courses);
        //List<CourseInfo> courses=DataManager.getInstance().getCourses();
        mAdapterCourses = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,null,
                new String[] {CourseInfoEntry.COLUMN_COURSE_TITLE},new int[] {android.R.id.text1},0);
        mAdapterCourses.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner_courses.setAdapter(mAdapterCourses);
        //loadCourseData();
        getSupportLoaderManager().initLoader(LOADER_COURSES,null,this);
        readDisplayStateValues();
        //saveOriginalNoteValues();

        if (!mIsnewnote)
        //setNoteINfoTofields();
        //loadNoteData();
            getSupportLoaderManager().initLoader(LOADER_NOTES,null, this);
        mModuleStatusView = (ModuleStatusView)findViewById(R.id.module_status);
        LoadModuleStatusValues();

    }

    private void LoadModuleStatusValues() {
        int totalNumberOfModules=11;
        int completedNumberOfModules=7;
        boolean[] moduleStatus=new boolean[totalNumberOfModules];
        for (int moduleIndex=0;moduleIndex<completedNumberOfModules;moduleIndex++){
            moduleStatus[moduleIndex]=true;
            mModuleStatusView.setModuleStatus(moduleStatus);
        }
    }

    private void loadCourseData() {
        SQLiteDatabase db= mDbOpenHelper.getReadableDatabase();
        String[] courseColumns={CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_ID,CourseInfoEntry._ID};
        Cursor cursor=db.query(CourseInfoEntry.TABLE_NAME,courseColumns,null,null,null,
        null,CourseInfoEntry.COLUMN_COURSE_TITLE);
        mAdapterCourses.changeCursor(cursor);
    }

    private void loadNoteData() {
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        String courseid="android_intents";
        String titlestart="Dynamic";
//        String selection= NoteInfoEntry._ID + " =? AND " + NoteInfoEntry.COLUMN_NOTE_TITLE + " LIKE ?";
//        String[] selectionArgs={courseid,titlestart + "%"};
        String selection= NoteInfoEntry._ID + " = ?";
        String[] selectionArgs={Integer.toString(mNoteId)};
        String[] columnsNote={
           NoteInfoEntry.COLUMN_COURSE_ID,
           NoteInfoEntry.COLUMN_NOTE_TITLE,
           NoteInfoEntry.COLUMN_NOTE_TEXT
        };
        mNotescursor = db.query(NoteInfoEntry.TABLE_NAME,columnsNote,selection,selectionArgs,null,null,null);
        mCourseidPos = mNotescursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mTitlePos = mNotescursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mTextPos = mNotescursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mNotescursor.moveToNext();
        //displayNote();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState!=null)
            mViewModel.saveState(outState);
    }

    private void saveOriginalNoteValues() {
        if (mIsnewnote)
            return;
        mViewModel.mOrignalcourseValue = mNote.getCourse().getCourseId();
        mViewModel.mOriginaltitleValue = mNote.getTitle();
        mViewModel.mOriginaltextValue = mNote.getText();
    }
    private void readDisplayStateValues() {
        Intent intent=getIntent();
        mNoteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET);
        mIsnewnote = mNoteId == ID_NOT_SET;
        if (mIsnewnote)
        {
            createNewNote();
        }
        //loadNoteData();
        //mNote=DataManager.getInstance().getNotes().get(mNoteId);

    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    private void displayNote() {
         String courseID=mNotescursor.getString(mCourseidPos);
         String notetitle=mNotescursor.getString(mTitlePos);
         String notetext=mNotescursor.getString(mTextPos);
//        List<CourseInfo> courses=DataManager.getInstance().getCourses();
//        CourseInfo course=DataManager.getInstance().getCourse(courseID);
        int mcourserindex=getIndexOfCourseId(courseID);
        mSpinner_courses.setSelection(mcourserindex);
        mMtitle.setText(notetitle);
        mMtext.setText(notetext);
        CourseEventBroadCastHelper.sendEventBroadcast(this,courseID,"Editing note");

    }

    private int getIndexOfCourseId(String courseID) {
        Cursor cursor=mAdapterCourses.getCursor();
        int courseIdPos=cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
        int courseRowIndex=0;
        boolean more=cursor.moveToFirst();
        while (more){
            String cursorCourseId=cursor.getString(courseIdPos);
            if (courseID.equals(cursorCourseId))
                break;
                courseRowIndex++;
                more=cursor.moveToNext();
        }
        return courseRowIndex;
    }

    private void storePreviousNoteValues() {
        CourseInfo course=DataManager.getInstance().getCourse(mViewModel.mOrignalcourseValue);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginaltitleValue);
        mNote.setText(mViewModel.mOriginaltextValue);
    }

    private void createNewNote() {
//        DataManager dm=DataManager.getInstance();
//        mNoteId = dm.createNewNote();
        //mNote=dm.getNotes().get(mNotePosition);
        AsyncTask<ContentValues,Integer,Uri> task=new AsyncTask<ContentValues, Integer, Uri>() {
            private ProgressBar mProgressBar;

//            @Override
//            protected void onPreExecute() {
//                mProgressBar=(ProgressBar) findViewById(R.id.progress_bar);
//                mProgressBar.setVisibility(View.VISIBLE);
//                mProgressBar.setProgress(1);
//
//            }

            @Override
            protected Uri doInBackground(ContentValues... contentValues) {
                Log.d(TAG,"doInBackground - thread: "+ Thread.currentThread().getId());
                ContentValues insertValues=contentValues[0];
                Uri resultUri=getContentResolver().insert(Notes.CONTENT_URI,insertValues);
                simulateLongRunningWork();
                publishProgress(2);
                simulateLongRunningWork();
                publishProgress(3);
                return resultUri;
            }

//            @Override
//            protected void onProgressUpdate(Integer... values) {
//                int progressValues=values[0];
//                mProgressBar.setProgress(progressValues);
//            }

            @Override
            protected void onPostExecute(Uri uri) {
                Log.d(TAG,"onPostExecute - thread: "+ Thread.currentThread().getId());
                mNoteUri=uri;
            }
        };
        final ContentValues values=new ContentValues();
        values.put(NoteInfoEntry.COLUMN_COURSE_ID,"");
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE,"");
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT,"");
        Log.d(TAG,"Call to execute - thread: "+ Thread.currentThread().getId());
        task.execute(values);
//        AsyncTask task=new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                SQLiteDatabase db=mDbOpenHelper.getWritableDatabase();
//                mNoteId=(int) db.insert(NoteInfoEntry.TABLE_NAME,null,values);
//                return null;
//            }
//        };
//        task.execute();
    }

    private void simulateLongRunningWork() {

    }

    private void saveNote() {

        String noteTitle=mMtitle.getText().toString();
        String noteText=mMtext.getText().toString();
        String courseId=selectedCourseId();
        saveNoteToDatabase(courseId,noteTitle,noteText);
    }

    private String selectedCourseId() {
        int selectedPosition=mSpinner_courses.getSelectedItemPosition();
        Cursor cursor=mAdapterCourses.getCursor();
        cursor.moveToPosition(selectedPosition);
        int courseIdPos=cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
        String courseId=cursor.getString(courseIdPos);
        return courseId;
    }

    private void saveNoteToDatabase(String courseId,String noteTitle, String noteText){
        final String selection=NoteInfoEntry._ID +" =?";
        final String[] selectionArgs={Integer.toString(mNoteId)};
        final ContentValues values= new ContentValues();
        values.put(NoteInfoEntry.COLUMN_COURSE_ID,courseId);
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE,noteTitle);
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT,noteText);
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                SQLiteDatabase db=mDbOpenHelper.getWritableDatabase();
                db.update(NoteInfoEntry.TABLE_NAME,values,selection,selectionArgs);
                return null;
            }
        };
        task.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_next);
        int lastIndex=DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNoteId <lastIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            mIscancelling = true;
            finish();
            return true;
        }
        else if (id==R.id.action_next)
        {
            saveNote();
            ++mNoteId;
            mNote=DataManager.getInstance().getNotes().get(mNoteId);
            saveOriginalNoteValues();
            displayNote();
            invalidateOptionsMenu();
        }

        else if (id==R.id.action_send)
        {
            sendEmail();
            return true;
        }
        else if(id==R.id.action_reminder){
            showNotificationReminder();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNotificationReminder() {
        String noteText=mMtext.getText().toString();
        String noteTitle=mMtitle.getText().toString();
        int noteId=(int)ContentUris.parseId(mNoteUri);
        //NoteReminderNotification.notify(this,noteTitle,noteText,noteId);

        Intent intent=new Intent(this,NoteReminderReceiver.class);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_TITLE,noteTitle);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_TEXT,noteText);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_ID,noteId);

        PendingIntent pendingIntent=PendingIntent
                .getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        long currentTimeMilliseconds= SystemClock.elapsedRealtime();
        long ONE_HOUR=60*60*1000;
        long TEN_SECONDS=10*1000;
        long alarmTime=currentTimeMilliseconds+TEN_SECONDS;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmTime,pendingIntent);
    }

    private void sendEmail() {
        String subject=mSpinner_courses.getSelectedItem().toString();
        String text="from the sublect this was the title"+"\n"+mMtitle.getText().toString()+"\\n"+
                "we learned the following:"+mMtext.getText().toString()+"\n";
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIscancelling)
            if (mIsnewnote)
            {
               deletNoteFromDatabase();
            } else {
                //storePreviousNoteValues();
            }
        else {
            saveNote();
        }
        Log.d(TAG,"onPause"+ mNoteId);

    }

    private void deletNoteFromDatabase() {
        final String selection=NoteInfoEntry._ID + " =? ";
        final String[] selectionArgs={Integer.toString(mNoteId)};
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                SQLiteDatabase db=mDbOpenHelper.getWritableDatabase();
                db.delete(NoteInfoEntry.TABLE_NAME,selection,selectionArgs);
                return null;
            }
        };
       task.execute();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader=null;
        if (id==LOADER_NOTES)
            loader=createLoaderNotes();
        else if (id==LOADER_COURSES)
            loader=createLoaderCourses();
        return loader;
    }

    private CursorLoader createLoaderCourses() {
        mCoursesQueryFinished = false;
        //Uri uri=Uri.parse("content://com.example.notekeeper.provider");
        Uri uri= Courses.CONTENT_URI;
        String[] courseColumns= {
                Courses.COLUMN_COURSE_TITLE,
                Courses.COLUMN_COURSE_ID,
                Courses._ID};
        return new CursorLoader(this,uri,courseColumns,null,null,
                Courses.COLUMN_COURSE_TITLE);

    }

    private CursorLoader createLoaderNotes() {
        mNotesQueryFinished = false;
        String[] columnsNote={
                Notes.COLUMN_COURSE_ID,
                Notes.COLUMN_NOTE_TITLE,
                Notes.COLUMN_NOTE_TEXT
        };
        mNoteUri= ContentUris.withAppendedId(Notes.CONTENT_URI,mNoteId);
        return new CursorLoader(this,mNoteUri,columnsNote,null,null,null);
//        return new CursorLoader(this){
//            @Override
//            public Cursor loadInBackground() {
//                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
//                String selection= NoteInfoEntry._ID + " = ?";
//                String[] selectionArgs={Integer.toString(mNoteId)};
//                String[] columnsNote={
//                        NoteInfoEntry.COLUMN_COURSE_ID,
//                        NoteInfoEntry.COLUMN_NOTE_TITLE,
//                        NoteInfoEntry.COLUMN_NOTE_TEXT
//                };
//
//                return db.query(NoteInfoEntry.TABLE_NAME,columnsNote,selection,selectionArgs,
//                        null,null,null);
//
//            }
//        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId()==LOADER_NOTES)
            loadFinishedNotes(data);
        else if (loader.getId()==LOADER_COURSES)
        {
            mAdapterCourses.changeCursor(data);
            mCoursesQueryFinished =true;
            displayNoteWhenQueriesFinished();
        }

    }


    private void loadFinishedNotes(Cursor data) {
        mNotescursor=data;
        mCourseidPos = mNotescursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mTitlePos = mNotescursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mTextPos = mNotescursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mNotescursor.moveToNext();
        mNotesQueryFinished =true;
        displayNoteWhenQueriesFinished();
    }

    private void displayNoteWhenQueriesFinished() {
        if (mNotesQueryFinished && mCoursesQueryFinished)
            displayNote();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId()==LOADER_NOTES){
            if (mNotescursor!=null)
                mNotescursor.close();
        }
        else if (loader.getId()==LOADER_COURSES)
            mAdapterCourses.changeCursor(null);

    }
}
