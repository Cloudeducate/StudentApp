package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudeducate.redtick.Model.Courses;
import com.cloudeducate.redtick.Model.Result;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 26-01-2016.
 */
public class CourseRecyclerviewAdapter extends RecyclerView.Adapter<CourseRecyclerviewAdapter.ViewHolder> {

    View courseView;
    Context context;
    List<Courses> list = new ArrayList<Courses>();

    public CourseRecyclerviewAdapter(Context context, List<Courses> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_card_item, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.coursename.setText(list.get(position).getcoursename());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView coursename;

        public ViewHolder(View itemView) {
            super(itemView);
            courseView = itemView;
            coursename = (TextView) itemView.findViewById(R.id.coursename);
                 }
    }
}
