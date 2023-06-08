package com.example.sw_alrami;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Job_Page extends Fragment {
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<JobItem> dataArrayList = new ArrayList<>();
    private JobAdapter adapter;
    private Button btnWrite;
    private Button btnRefresh;
    //postman에서 authorization 임시로 가져온 값
    private String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkMXM0MSIsImF1dGgiOiJBRE1JTiIsImV4cCI6MTY4NjIzNzY0OX0.Qs_uHnrO_QCQZuUnak3osZbPP1DZPunkALomPrCwpaM";
    private String urlStr = "http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/employment-community/cursor";
    private String urlStr2 = "http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/employment-community/old/cursor";
    private int nextIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_page, container, false);

        nestedScrollView = view.findViewById(R.id.scroll_view);
        recyclerView = view.findViewById(R.id.job_recycler_view);
        progressBar = view.findViewById(R.id.job_progress_bar);

        adapter = new JobAdapter(getActivity(), dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        try {
            new Task().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Spinner spinner = view.findViewById(R.id.sortSpinner);

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
                }
                else if (spinnerAdapter.getItem(i).equals("오래된순")) {
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

        btnWrite = view.findViewById(R.id.writeBtn);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Job_Writing_Page.class);
                intent.putExtra("auth", authToken);
                startActivity(intent);
            }
        });

        btnRefresh = view.findViewById(R.id.refreshBtn);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataArrayList.clear();
                adapter.notifyDataSetChanged();

                try {
                    new Task().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
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
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + authToken);

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
                        JobItem jobItem = new JobItem();
                        jobItem.setJob(jsonObject1.getString("title"));
                        jobItem.setDate(jsonObject1.getString("createdAt"));;
                        jobItem.setViews(Integer.parseInt(jsonObject1.getString("count")));
                        jobItem.setMainText(jsonObject1.getString("description"));
                        jobItem.setId(jsonObject1.getInt("id"));
                        dataArrayList.add(jobItem);
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
                conn.setRequestProperty("Authorization", "Bearer " + authToken);

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
                        JobItem jobItem = new JobItem();
                        jobItem.setJob(jsonObject1.getString("title"));
                        jobItem.setDate(jsonObject1.getString("createdAt"));;
                        jobItem.setViews(Integer.parseInt(jsonObject1.getString("count")));
                        jobItem.setMainText(jsonObject1.getString("description"));
                        jobItem.setId(jsonObject1.getInt("id"));
                        dataArrayList.add(jobItem);
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
                conn.setRequestProperty("Authorization", "Bearer " + authToken);

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
                        JobItem jobItem = new JobItem();
                        jobItem.setJob(jsonObject1.getString("title"));
                        jobItem.setDate(jsonObject1.getString("createdAt"));;
                        jobItem.setViews(Integer.parseInt(jsonObject1.getString("count")));
                        jobItem.setMainText(jsonObject1.getString("description"));
                        jobItem.setId(jsonObject1.getInt("id"));
                        dataArrayList.add(jobItem);
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

    public class Task4 extends AsyncTask<String, Void, String> {
        String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(urlStr2 + "?next=" + nextIndex);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + authToken);

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
                        JobItem jobItem = new JobItem();
                        jobItem.setJob(jsonObject1.getString("title"));
                        jobItem.setDate(jsonObject1.getString("createdAt"));;
                        jobItem.setViews(Integer.parseInt(jsonObject1.getString("count")));
                        jobItem.setMainText(jsonObject1.getString("description"));
                        jobItem.setId(jsonObject1.getInt("id"));
                        dataArrayList.add(jobItem);
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
}