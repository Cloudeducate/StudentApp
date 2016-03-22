package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudeducate.redtick.Model.PerformanceTracker;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogesh on 30/1/16.
 */
public class PerformanceRecyclerAdapter extends RecyclerView.Adapter<PerformanceRecyclerAdapter.ViewHolder> {

    View perfView;
    Context context;
    List<PerformanceTracker> performanceTrackerList = new ArrayList<PerformanceTracker>();

    public PerformanceRecyclerAdapter(Context context, List<PerformanceTracker> performanceTrackerList) {
        this.context = context;
        this.performanceTrackerList = performanceTrackerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.performance_item_layout, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.week.setText("Week : " + String.valueOf(performanceTrackerList.get(position).getWeek()));
        holder.grade.setText("Grade assigned : " + String.valueOf(performanceTrackerList.get(position).getGrade()));

    }

    @Override
    public int getItemCount() {
        return performanceTrackerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView week, grade;

        public ViewHolder(View itemView) {
            super(itemView);
            perfView = itemView;
            week = (TextView) itemView.findViewById(R.id.week);
            grade = (TextView) itemView.findViewById(R.id.grade);


        }
    }


}
