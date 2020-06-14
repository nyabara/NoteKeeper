package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final int POSITION_NOT_SET = -1;
    public static final String NOTE_POSITION ="com.example.notekeeper.NOTE_POSITION";
    private static final String LOG=NoteActivity.class.getSimpleName();
    //public static final String NOTE_POSITION = NOTE_POSITION;
    private NoteInfo mNote;
    private boolean mIsnewnote;
    private EditText mMtitle;
    private EditText mMtext;
    private Spinner mSpinner_courses;
    private int mNotePosition;
    private boolean mIscancelling;
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMtitle = findViewById(R.id.note_title_text);
        mMtext = findViewById(R.id.note_body_text);

        ViewModelProvider viewModelProvider=new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel=viewModelProvider.get(NoteActivityViewModel.class);

        if (mViewModel.isnewlycreated&&savedInstanceState!=null)
            mViewModel.restorestate(savedInstanceState);
        mViewModel.isnewlycreated=false;
        mSpinner_courses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses=DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> courseInfoArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,courses);
        courseInfoArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner_courses.setAdapter(courseInfoArrayAdapter);
        getNoteInfo();
        storeOriginalValues();
        if (!mIsnewnote)
        setNoteINfoTofields(mSpinner_courses, mMtitle, mMtext);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState!=null)
            mViewModel.saveState(outState);
    }

    private void storeOriginalValues() {
        if (mIsnewnote)
            return;
        mViewModel.mOrignalcourseValue = mNote.getCourse().getCourseId();
        mViewModel.mOriginaltitleValue = mNote.getTitle();
        mViewModel.mOriginaltextValue = mNote.getText();
    }
    private void getNoteInfo() {
        Intent intent=getIntent();
        mNotePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsnewnote = mNotePosition==POSITION_NOT_SET;
        if (mIsnewnote)
        {
            createNewNote();
        }
        mNote=DataManager.getInstance().getNotes().get(mNotePosition);

    }

    private void setNoteINfoTofields(Spinner mSpinner_courses, EditText mMtitle, EditText mMtext) {
        List<CourseInfo> courses=DataManager.getInstance().getCourses();
        int mcourserindex=courses.indexOf(mNote.getCourse());
        mSpinner_courses.setSelection(mcourserindex);

        mMtitle.setText(mNote.getTitle());
        mMtext.setText(mNote.getText());

    }

    private void saveOriginalvalues() {
        CourseInfo course=DataManager.getInstance().getCourse(mViewModel.mOrignalcourseValue);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginaltitleValue);
        mNote.setText(mViewModel.mOriginaltextValue);
    }

    private void createNewNote() {
        DataManager dm=DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        //mNote=dm.getNotes().get(mNotePosition);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo)mSpinner_courses.getSelectedItem());
        mNote.setTitle(mMtitle.getText().toString());
        mNote.setText(mMtext.getText().toString());
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
        item.setEnabled(mNotePosition<lastIndex);
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
            ++mNotePosition;
            mNote=DataManager.getInstance().getNotes().get(mNotePosition);
            storeOriginalValues();
            setNoteINfoTofields(mSpinner_courses,mMtitle,mMtext);
            invalidateOptionsMenu();
        }
        else if (id==R.id.action_send)
        {
            sendEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                saveOriginalvalues();
            }
        else {
            saveNote();
        }
        Log.d(LOG,"onPause"+mNotePosition);

    }
}
