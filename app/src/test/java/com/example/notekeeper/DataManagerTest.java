package com.example.notekeeper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {
     static DataManager sDataManager;
    @BeforeClass
    public static void classSetup(){

        sDataManager=DataManager.getInstance();

    }
    @Before
    public void setUp(){
        //DataManager dm=DataManager.getInstance();
        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();
    }

    @Test
    public void createNewNote() {
        NoteInfo mNoteInfo;
       // DataManager dm=DataManager.getInstance();
        String mTitle="android async";
        String mtext="my body";
        CourseInfo courrse=sDataManager.getCourse("async_android");
        int position=sDataManager.createNewNote();
        mNoteInfo=sDataManager.getNotes().get(position);
        mNoteInfo.setText(mtext);
        mNoteInfo.setTitle(mTitle);
        mNoteInfo.setCourse(courrse);
        NoteInfo comparenotes=sDataManager.getNotes().get(position);
        assertEquals(mtext,comparenotes.getText());
        assertEquals(mTitle,comparenotes.getTitle());
        assertEquals(courrse,comparenotes.getCourse());


    }
    @Test
    public void findSimilarNotes(){
        //DataManager dm=DataManager.getInstance();
        CourseInfo course=sDataManager.getCourse("async_android");
        String mTitle="android async";
        String mtext="my body";
        int index=sDataManager.createNewNote();
        NoteInfo note1=sDataManager.getNotes().get(index);
        note1.setCourse(course);
        note1.setTitle(mTitle);
        note1.setText(mtext);
        NoteInfo comparenote1=sDataManager.getNotes().get(index);
        //assertEquals(course,comparenote1.getCourse());
        //assertEquals(mTitle,comparenote1.getTitle());
        //assertEquals(mtext,comparenote1.getText());

        CourseInfo course1=sDataManager.getCourse("async_android");
        String mTitle1="android async";
        String mtext1="my body";
        int index1=sDataManager.createNewNote();
        NoteInfo note2=sDataManager.getNotes().get(index1);
        note2.setCourse(course1);
        note2.setTitle(mTitle1);
        note2.setText(mtext1);
        NoteInfo comparenote2=sDataManager.getNotes().get(index1);
        //assertEquals(course1,comparenote2.getCourse());
        //assertEquals(mTitle1,comparenote2.getTitle());
        //assertEquals(mtext1,comparenote2.getText());

        assertEquals(course,comparenote2.getCourse());
        assertEquals(course1,comparenote1.getCourse());


    }
    @Test
    public void createNewNoteOnestepCreation(){
        CourseInfo course=sDataManager.getCourse("async_android");
        String mTitle="android async";
        String mtext="my body";
        int newNoteposition=sDataManager.createNewNote(course,mTitle,mtext);
        NoteInfo newnote=sDataManager.getNotes().get(newNoteposition);

    }
}