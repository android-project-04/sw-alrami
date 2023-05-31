package com.example.sw_alrami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private Context context;
    private List<ApiResponse> dataList;

    public RecyclerAdapter(Context context, List<ApiResponse> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_cus_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ApiResponse item = dataList.get(position);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView eventTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.dataNoticeTitle);
            eventTime = itemView.findViewById(R.id.dataEventTime);
        }
    }
}
