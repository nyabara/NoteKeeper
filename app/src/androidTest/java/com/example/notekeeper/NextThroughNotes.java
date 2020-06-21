package com.example.notekeeper;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.*;

import static org.junit.Assert.*;

public class NextThroughNotes {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            =new ActivityTestRule<>(MainActivity.class);
    @Test
    public void NavigatioNoteNex(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.notes));
        onView(withId(R.id.note_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        List<NoteInfo> notes=DataManager.getInstance().getNotes();
        for (int index=0;index<notes.size();index++) {
            NoteInfo note = notes.get(index);
            onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(note.getCourse().getTitle())));
            onView(withId(R.id.note_title_text)).check(matches(withText(note.getTitle())));
            onView(withId(R.id.note_body_text)).check(matches(withText(note.getText())));
            if (index<notes.size()-1)
            onView(allOf(withId(R.id.action_next),isEnabled())).perform(click());
        }
        onView(withId(R.id.action_next)).check(matches(not(isEnabled())));
        pressBack();

    }

}