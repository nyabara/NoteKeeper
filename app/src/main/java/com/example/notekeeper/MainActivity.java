package com.example.notekeeper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NoteRecyclerViewAdapter mNoteRecyclerViewAdapter;
    private RecyclerView mMrecyclerviewItem;
    private LinearLayoutManager mNoteslayout;
    private NotekeeperOpenHelper mOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        mOpenHelper=new NotekeeperOpenHelper(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NoteActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.opendrawer,R.string.closedrawer);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mMrecyclerviewItem = findViewById(R.id.note_list);
        mNoteslayout = new LinearLayoutManager(this);
        DataManager.loadFromDatabase(mOpenHelper);
        List<NoteInfo> notes=DataManager.getInstance().getNotes();
        mNoteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this,notes);
        DisplayNoteInfoList();
    }

    @Override
    protected void onDestroy() {
        mOpenHelper.close();
        super.onDestroy();
    }

    private void DisplayNoteInfoList() {


        mMrecyclerviewItem.setLayoutManager(mNoteslayout);
        mMrecyclerviewItem.setAdapter(mNoteRecyclerViewAdapter);
        //SQLiteDatabase db=mOpenHelper.getReadableDatabase();
        // mOpenHelper.getReadableDatabase();
        NavigationView navigationView=findViewById(R.id.nav_view);

        Menu menu=navigationView.getMenu();
        menu.findItem(R.id.notes).setChecked(true);

    }
    private void DisplayCourseInfoList() {
        mMrecyclerviewItem = findViewById(R.id.note_list);
        //mLinearlayout = new LinearLayoutManager(this);
        //mMrecyclerviewItem.setLayoutManager(mLinearlayout);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,getResources().getInteger(R.integer.grid_space_column));
        mMrecyclerviewItem.setLayoutManager(gridLayoutManager);
        List<CourseInfo> courses=DataManager.getInstance().getCourses();
        CourseRecyclerViewAdapter courseRecyclerViewAdapter=new CourseRecyclerViewAdapter(this,courses);
        //mNoteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this,notes);
        mMrecyclerviewItem.setAdapter(courseRecyclerViewAdapter);
        mMrecyclerviewItem.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if (id==R.id.notes)
            DisplayNoteInfoList();
        else if (id==R.id.nav_share){
           handleSelection(R.string.sharemessage);

        }
        else if (id==R.id.ncourse)
            DisplayCourseInfoList();
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void handleSelection(int message) {
        View view=findViewById(R.id.note_list);
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        super.onBackPressed();
    }
    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}
