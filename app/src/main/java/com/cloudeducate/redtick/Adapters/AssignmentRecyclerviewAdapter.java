package com.cloudeducate.redtick.Adapters;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudeducate.redtick.Model.Assignment;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogesh on 22/1/16.
 */
public class AssignmentRecyclerviewAdapter extends RecyclerView.Adapter<AssignmentRecyclerviewAdapter.ViewHolder> {

    View assignmentView;
    Context context;
    List<Assignment> list = new ArrayList<Assignment>();
    ThinDownloadManager downloadManager;
    ProgressDialog progressDialog;
    int id = 1;

    public AssignmentRecyclerviewAdapter(Context context, List<Assignment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_item_layout, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.desc.setText(list.get(position).getDescription());
        holder.deadline.setText("Deadline : " + list.get(position).getDeadline());
        holder.course.setText(list.get(position).getCourse());
        holder.filename.setText(list.get(position).getFilename());
        if (list.get(position).getSubmitted() == true) {
            holder.status.setText("Done");
        } else {
            holder.status.setText("Not Submitted");
        }
        assignmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fileName = holder.filename.getText().toString();
                showProgressDialog(fileName);
                Uri downloadUri = Uri.parse(URL.getAssignmentDownloadURL(fileName));
                Uri destinationUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + Constants.AppFolderName + "/" + fileName);
                Log.v("test", "destination uri = " + String.valueOf(destinationUri));
                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                        //.addCustomHeader("Auth-Token", "YourTokenApiKey")
                        .setRetryPolicy(new DefaultRetryPolicy())
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                        .setDownloadListener(new DownloadStatusListener() {
                            @Override
                            public void onDownloadComplete(int id) {
                                progressDialog.dismiss();
                                Notify("Success");

                            }

                            @Override
                            public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                                Notify("Fail");
                            }

                            @Override
                            public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
                                progressDialog.setProgress(progress);
                            }
                        });
                downloadManager = new ThinDownloadManager();
                int downloadId = downloadManager.add(downloadRequest);
                Log.v("test", String.valueOf(downloadId));

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, filename, course, status, deadline;

        public ViewHolder(View itemView) {
            super(itemView);
            assignmentView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.description);
            deadline = (TextView) itemView.findViewById(R.id.deadline);
            filename = (TextView) itemView.findViewById(R.id.filename);
            course = (TextView) itemView.findViewById(R.id.course);
            status = (TextView) itemView.findViewById(R.id.status);

        }
    }

    public void showProgressDialog(String filename) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading " + filename);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Notify(String status) {
        final NotificationManager mNotifyManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        switch (status){
            case "Success" : mBuilder.setContentTitle("Download Complete"); break;
            case "Fail" : mBuilder.setContentTitle("Download failed"); break;
        }
        mBuilder.setContentTitle("Download Complete")
                .setSmallIcon(R.drawable.ic_assessment_grey);
        mNotifyManager.notify(id, mBuilder.build());

    }

}
