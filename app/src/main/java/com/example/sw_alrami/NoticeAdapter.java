package com.example.sw_alrami;

        import android.app.Activity;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import java.util.ArrayList;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private ArrayList<NoticeItem> dataArrayList;
    private Activity activity;

    public NoticeAdapter(Activity activity, ArrayList<NoticeItem> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_cus_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder holder, int position) {
        NoticeItem listItem = dataArrayList.get(position);

        holder.noticeText.setText(listItem.getTitle());

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noticeText;


        public ViewHolder(@NonNull View view) {
            super(view);

            noticeText = view.findViewById(R.id.noticeText);


            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), Notice_Page.class);

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        NoticeItem listItem = dataArrayList.get(pos);
                        String str = listItem.getTitle();
                        intent.putExtra("titleText", str);
                    }

                    activity.startActivity(intent);
                }
            });
        }
    }
}
