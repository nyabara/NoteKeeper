package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

import static com.example.notekeeper.NoteKeeperDatabaseContract.*;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    //public final List<NoteInfo>mNotes;
    private Cursor mCursor;
    private final LayoutInflater mLayoutInflater;
    private int mMCoursePos;
    private int mMNoteTitlePos;
    private int mMIdPos;

    public NoteRecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(mContext);
        populateColumnsPosition();
    }

    private void populateColumnsPosition() {
        if (mCursor==null)
            return;
        //Get Column indexes from mCursor
        mMCoursePos = mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        mMNoteTitlePos = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mMIdPos = mCursor.getColumnIndex(NoteInfoEntry._ID);
    }
    public void changeCursor(Cursor cursor){
        if (mCursor!=null)
            mCursor.close();
        mCursor=cursor;
        populateColumnsPosition();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mLayoutInflater.inflate(R.layout.item_note_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String course=mCursor.getString(mMCoursePos);
        String noteTitle=mCursor.getString(mMNoteTitlePos);
        int id=mCursor.getInt(mMIdPos);

        holder.mTextCourse.setText(course);
        holder.mTextTitle.setText(noteTitle);
        holder.mId =id;

    }

    @Override
    public int getItemCount() {

        return mCursor == null ? 0: mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextCourse, mTextTitle;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse =itemView.findViewById(R.id.textCourse);
            mTextTitle =itemView.findViewById(R.id.textTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
