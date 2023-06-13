package com.example.sw_alrami;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Community_Text_Page extends AppCompatActivity {
    int id;
    String urlStr = "http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/community-bookmark";
    String authToken;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_text_page);

        TextView titleText = findViewById(R.id.communityTitle);
        TextView mainText = findViewById(R.id.mainText);


        Intent intent1 = getIntent();

        authToken = intent1.getStringExtra("accesstoken");
        String refreshtoken = intent1.getStringExtra("refreshtoken");
        String authority = intent1.getStringExtra("authority");         //access 토큰 가져오기


        Intent intent = getIntent();
        titleText.setText(intent.getStringExtra("titleText"));
        mainText.setText(intent.getStringExtra("mainText"));
        id = intent.getIntExtra("id", 0);

        Button addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new addBookmark().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public class addBookmark extends AsyncTask<String, Void, String> {
        String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(urlStr + "/" + id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setRequestProperty("Authorization", "Bearer " + authToken);
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(3000);

                int code = conn.getResponseCode();

                if (code == HttpURLConnection.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                        builder.append("\n");
                    }
                    receiveMsg = builder.toString();
                    Log.i("Result", receiveMsg);

                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    public class deleteBookmark extends AsyncTask<String, Void, String> {
        String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(urlStr + "/" + id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("DELETE");
                conn.setUseCaches(false);
                conn.setRequestProperty("Authorization", "Bearer " + authToken);
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(3000);

                int code = conn.getResponseCode();

                if (code == HttpURLConnection.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                        builder.append("\n");
                    }
                    receiveMsg = builder.toString();
                    Log.i("Result", receiveMsg);

                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }
}