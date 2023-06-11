package com.example.sw_alrami;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class Notice_Page extends Fragment implements TextWatcher {
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<NoticeItem> dataArrayList = new ArrayList<>();

    private ArrayList<NoticeItem> filteredList = new ArrayList<>();
    private NoticeAdapter adapter;

    private EditText searchText;

    private ArrayList<NoticeItem> originalList = new ArrayList<>();                                     ////////
    private int nextIndex;


    String authtoken;

    String urlStr = "http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/notification/list";

    String urlStr2 = "http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/notification/old/list";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_page, container, false);

        nestedScrollView = view.findViewById(R.id.scroll_view);
        recyclerView = view.findViewById(R.id.notice_recycler_view);
        progressBar = view.findViewById(R.id.notice_progress_bar);
        searchText = view.findViewById(R.id.searchText);
        searchText.addTextChangedListener(this);

        adapter = new NoticeAdapter(getActivity(), dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        Bundle bundle = getArguments();

        authtoken = bundle.getString("accesstoken");
        String refreshtoken = bundle.getString("refreshtoken");
        String authority = bundle.getString("authority");           //access 토큰 가져오기


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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerAdapter.getItem(i).equals("최신순")) {
                    dataArrayList.clear();
                    adapter.notifyDataSetChanged();

                    try {
                        new Task().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                                progressBar.setVisibility(View.VISIBLE);
                                try {
                                    new Task2().execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else if (spinnerAdapter.getItem(i).equals("오래된순")) {
                    dataArrayList.clear();
                    adapter.notifyDataSetChanged();

                    try {
                        new Task3().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                                progressBar.setVisibility(View.VISIBLE);
                                try {
                                    new Task4().execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        new Task2().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = searchText.getText().toString();
        searchFilter(text);
    }

    public void searchFilter(String text) {
   
        if (!(text.equals(""))) {
            filteredList.clear();

            for (int i = 0; i < dataArrayList.size(); i++) {
                if (dataArrayList.get(i).getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(dataArrayList.get(i));
                }
            }

            adapter.filterList(filteredList);
        }
        else {
            filteredList = dataArrayList;
            adapter.filterList(filteredList);
        }
    }

    public class Task extends AsyncTask<String, Void, String> {
        String str, receiveMsg;


        @Override
        protected String doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
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

                    nextIndex = postObject.getInt("lastIndex");

                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class Task2 extends AsyncTask<String, Void, String> {
        String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(urlStr + "?next=" + nextIndex);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
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


                    nextIndex = postObject.getInt("lastIndex");

                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    public class Task3 extends AsyncTask<String, Void, String> {
        String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(urlStr2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
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
                    nextIndex=postObject.getInt("lastIndex");

                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class Task4 extends AsyncTask<String, Void, String> {
        String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(urlStr2 + "?next=" + nextIndex);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
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

                    nextIndex= postObject.getInt("lastIndex");

                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}