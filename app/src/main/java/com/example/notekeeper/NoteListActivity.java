package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private static final String TAG=NoteListActivity.class.getSimpleName();
    private NoteRecyclerViewAdapter mNoteRecyclerViewAdapter;

    // private ArrayAdapter<NoteInfo> mNoteInfoArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });
        DisplayNoteInfoList();
        Log.d(TAG,"OnCreate");

    }

    @Override
    protected void onResume() {
        super.onResume();
       // mNoteInfoArrayAdapter.notifyDataSetChanged();
        mNoteRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void DisplayNoteInfoList() {
      /*  final ListView listinfo=findViewById(R.id.listinfo);
        List<NoteInfo> noteInfoList=DataManager.getInstance().getNotes();
        mNoteInfoArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,noteInfoList);
        listinfo.setAdapter(mNoteInfoArrayAdapter);


        listinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(NoteListActivity.this,NoteActivity.class);
//                NoteInfo note=(NoteInfo)listinfo.getItemAtPosition(position);
                intent.putExtra("com.example.notekeeper.NOTE_POSITION",position);
                startActivity(intent);
            }
        });*/
      final RecyclerView mrecyclerview=findViewById(R.id.list_note);
      final LinearLayoutManager setLinearlayout=new LinearLayoutManager(this);
      mrecyclerview.setLayoutManager(setLinearlayout);
        mNoteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this,null);
      mrecyclerview.setAdapter(mNoteRecyclerViewAdapter);
      mrecyclerview.setVisibility(View.VISIBLE);
    }

}
