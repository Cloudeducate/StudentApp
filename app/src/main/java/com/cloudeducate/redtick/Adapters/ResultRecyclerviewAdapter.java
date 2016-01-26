package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudeducate.redtick.Model.Assignment;
import com.cloudeducate.redtick.Model.Result;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 26-01-2016.
 */
public class ResultRecyclerviewAdapter extends RecyclerView.Adapter<ResultRecyclerviewAdapter.ViewHolder> {

    View resultView;
    Context context;
    List<Result> list = new ArrayList<Result>();

    public ResultRecyclerviewAdapter(Context context, List<Result> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_card_item, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.exam.setText(list.get(position).getexam());
        holder.year.setText(list.get(position).getyear());
        holder.marks.setText("MARKS : "+list.get(position).getmarks());
        holder.highest.setText("HIGHEST :"+list.get(position).gethighest());
        holder.average.setText("AVERAGE :"+list.get(position).getaverage());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exam,year,marks,highest,average;

        public ViewHolder(View itemView) {
            super(itemView);
            resultView = itemView;
            exam = (TextView) itemView.findViewById(R.id.examname);
            year = (TextView) itemView.findViewById(R.id.year);
            marks = (TextView) itemView.findViewById(R.id.marks);
            highest = (TextView) itemView.findViewById(R.id.max);
            average = (TextView) itemView.findViewById(R.id.average);


        }
    }
}
