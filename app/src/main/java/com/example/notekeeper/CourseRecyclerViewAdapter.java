package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    public final List<CourseInfo> mCourse;
    private final LayoutInflater mLayoutInflater;

    public CourseRecyclerViewAdapter(Context context, List<CourseInfo> course) {
        mContext = context;
        mCourse = course;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mLayoutInflater.inflate(R.layout.item_course_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseInfo course= mCourse.get(position);
        holder.course.setText(course.getTitle());
        holder.currentposition=position;

    }

    @Override
    public int getItemCount() {

        return mCourse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView course;
        public int currentposition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            course=itemView.findViewById(R.id.textCourse);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });
        }
    }
}
