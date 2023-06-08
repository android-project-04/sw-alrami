package com.example.sw_alrami;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private ArrayList<JobItem> dataArrayList;
    private Activity activity;

    public JobAdapter(Activity activity, ArrayList<JobItem> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_cus_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.ViewHolder holder, int position) {
        JobItem listItem = dataArrayList.get(position);

        holder.jobText.setText(listItem.getJob());
        holder.viewText.setText(String.valueOf(listItem.getViews()));
        holder.dateText.setText(listItem.getDate());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobText;
        TextView dateText;
        TextView viewText;

        public ViewHolder(@NonNull View view) {
            super(view);

            jobText = view.findViewById(R.id.jobText);
            dateText = view.findViewById(R.id.dateText);
            viewText = view.findViewById(R.id.viewText);

            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), Job_Text_Page.class);

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        JobItem listItem = dataArrayList.get(pos);
                        String str = listItem.getJob();
                        intent.putExtra("titleText", str);
                        String str2 = listItem.getMainText();
                        intent.putExtra("mainText", str2);
                        int id = listItem.getId();
                        intent.putExtra("id", id);
                    }

                    activity.startActivity(intent);
                }
            });
        }
    }
}
