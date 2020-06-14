package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    public final List<NoteInfo>mNotes;
    private final LayoutInflater mLayoutInflater;

    public NoteRecyclerViewAdapter(Context context, List<NoteInfo> notes) {
        mContext = context;
        mNotes = notes;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mLayoutInflater.inflate(R.layout.item_note_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteInfo note=mNotes.get(position);
        holder.course.setText(note.getCourse().getTitle());
        holder.title.setText(note.getTitle());
        holder.currentposition=position;

    }

    @Override
    public int getItemCount() {

        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView course,title;
        public int currentposition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            course=itemView.findViewById(R.id.textCourse);
            title=itemView.findViewById(R.id.textTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION,currentposition);
                    mContext.startActivity(intent);

                }
            });
        }
    }
}
