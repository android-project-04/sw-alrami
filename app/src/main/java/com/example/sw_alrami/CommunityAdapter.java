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

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private ArrayList<CommunityItem> dataArrayList;
    private Activity activity;

    public CommunityAdapter(Activity activity, ArrayList<CommunityItem> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_cus_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.ViewHolder holder, int position) {
        CommunityItem listItem = dataArrayList.get(position);

        holder.communityText.setText(listItem.getCommunity());
        holder.viewText.setText(String.valueOf(listItem.getViews()));
        holder.dateText.setText(listItem.getDate());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void filterList(ArrayList<CommunityItem> filteredList) {
        dataArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView communityText;
        TextView dateText;
        TextView viewText;

        public ViewHolder(@NonNull View view) {
            super(view);

            communityText = view.findViewById(R.id.communityText);
            dateText = view.findViewById(R.id.dateText);
            viewText = view.findViewById(R.id.viewText);

            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), Community_Text_Page.class);

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        CommunityItem listItem = dataArrayList.get(pos);
                        String str = listItem.getCommunity();
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
