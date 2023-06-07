package com.example.sw_alrami;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Notice_Page extends Fragment {
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<NoticeItem> dataArrayList = new ArrayList<>();
    private NoticeAdapter adapter;

    private int page = 1;
    private int limit = 10;

    String authtoken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkMXM0MSIsImF1dGgiOiJBRE1JTiIsImV4cCI6MTY4NjEyNjI2N30.bmZEME-V-OX65YmtAiPQdKGYtmzaC_FhKHK7hEPTpv8";
    String urlStr = "http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/notification/list";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_page, container, false);

        nestedScrollView = view.findViewById(R.id.scroll_view);
        recyclerView = view.findViewById(R.id.notice_recycler_view);
        progressBar = view.findViewById(R.id.notice_progress_bar);

        adapter = new NoticeAdapter(getActivity(), dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        try {
            new Task().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Spinner spinner = view.findViewById(R.id.notice_sort_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        new Task().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });




        return view;
    }

    public class Task extends AsyncTask<String, Void, String> {
        String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "Bearer " + authtoken);

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();

                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                        builder.append("\n");
                    }

                    receiveMsg = builder.toString();
                    JSONObject jsonObject = new JSONObject(receiveMsg);
                    JSONObject postObject = jsonObject.getJSONObject("data");


                    JSONArray jsonArray = postObject.getJSONArray("values"); 



                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        NoticeItem noticeItem = new NoticeItem();
                        noticeItem.setTitle(jsonObject1.getString("title"));
                        noticeItem.setUrl(jsonObject1.getString("url"));
                        dataArrayList.add(noticeItem);
                    }

                    adapter.notifyDataSetChanged();

                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}