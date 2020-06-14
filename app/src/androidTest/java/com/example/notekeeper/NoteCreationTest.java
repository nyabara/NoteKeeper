package com.example.notekeeper;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.*;
@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
    static DataManager sDataManager;


    @Rule
    public ActivityTestRule<NoteListActivity> mNoteListActivityActivityTestRule=new ActivityTestRule<>(NoteListActivity.class);
    @BeforeClass
    public static void classSetUp(){
        sDataManager=DataManager.getInstance();
    }
    @Test
    public void createNewNote(){
        final CourseInfo course=sDataManager.getCourse("java_lang");
        final String mtitle="my title";
        final String mtext="my body";
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.getTitle()))));
        onView(withId(R.id.note_title_text)).perform(typeText(mtitle)).check(matches(withText(containsString(mtitle))));
        onView(withId(R.id.note_body_text)).perform(typeText(mtext),closeSoftKeyboard()).check(matches(withText(containsString(mtext))));
        pressBack();
        int mnoteindex=sDataManager.getNotes().size()-1;
        NoteInfo newnote=sDataManager.getNotes().get(mnoteindex);
        assertEquals(course,newnote.getCourse());
        assertEquals(mtitle,newnote.getTitle());
        assertEquals(mtext,newnote.getText());
    }


}