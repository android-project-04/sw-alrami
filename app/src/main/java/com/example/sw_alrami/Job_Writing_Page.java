package com.example.sw_alrami;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Job_Writing_Page extends AppCompatActivity {
    Button commitBtn;
    EditText jobTitle;
    EditText mainText;
    private String authToken;
    private String urlStr = "http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/employment-community";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_writing_page);

        authToken = getIntent().getStringExtra("auth");
        commitBtn = findViewById(R.id.commitBtn);
        jobTitle = findViewById(R.id.jobTitle);
        mainText = findViewById(R.id.mainText);

        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new Task().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    public class Task extends AsyncTask<String, Void, String> {
        String str, receiveMsg;
        String boundary = "yLfiI0vfP2PCPPUrappX2Jr0rdF9TyKrCqxk";
        String charset = "UTF-8";

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(urlStr);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setRequestProperty("Authorization", "Bearer " + authToken);
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setConnectTimeout(1000);

                String titleText = jobTitle.getText().toString();
                String descriptionText = mainText.getText().toString();
                JSONObject requestCommunity = new JSONObject();
                requestCommunity.put("title", titleText);
                requestCommunity.put("description", descriptionText);

                PrintWriter writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), charset), true);
                writer.append("--" + boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"requestCommunity\"").append("\r\n");
                writer.append("Content-Type: application/json; charset=" + charset).append("\r\n");
                writer.append("\r\n").append(requestCommunity.toString());
                writer.append("\r\n").flush();
                writer.append("--" + boundary + "--").append("\r\n").flush();

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
                else {
                    Log.i("Result", "Didn't get msg");
                    Log.i("Result", Integer.toString(code));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }
}
