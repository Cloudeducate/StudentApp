package com.cloudeducate.redtick.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudeducate.redtick.Activities.ViewConversation;
import com.cloudeducate.redtick.Model.Conversation;
import com.cloudeducate.redtick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogesh on 5/2/16.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    View resultView;
    Context context;
    Bundle bundle;
    String conversation_id;
    List<Conversation> list = new ArrayList<Conversation>();

    public ConversationAdapter(Context context, List<Conversation> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversation_item_layout, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.teacherName.setText(list.get(position).getTeacher());
        holder.convID.setText(list.get(position).getId());
        resultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversation_id = list.get(position).getId().toString();
                bundle = new Bundle();
                bundle.putString("conversation_id", conversation_id);
                Intent intent = new Intent(context, ViewConversation.class).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView teacherName, convID;

        public ViewHolder(View itemView) {
            super(itemView);
            resultView = itemView;
            teacherName = (TextView) itemView.findViewById(R.id.teacherName);
            convID = (TextView) itemView.findViewById(R.id.textView_id);


        }
    }

}
