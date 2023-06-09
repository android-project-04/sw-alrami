package com.example.sw_alrami;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {

    private EditText et_loginID, et_password, et_nickname, et_studentNumber;
    private String filePath;
    private Button btn_signup, btn_camera;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_loginID = findViewById(R.id.signup_id);
        et_password = findViewById(R.id.signup_pw);
        et_nickname = findViewById(R.id.signup_nickname);
        et_studentNumber = findViewById(R.id.signup_stdid);

        btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_signup = findViewById(R.id.btn_signup_ok);
        btn_signup.setOnClickListener(view -> {
            RequestSignup signup = new RequestSignup();
            signup.start();
        });

    }

    class RequestSignup extends Thread {
        @Override
        public void run() {

            try {
                URL url = new URL("http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/api/member/signup");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "multipart/form-data");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                File file = new File(filePath);
                FileInputStream fileInputStream = new FileInputStream(file);

                // Start writing the request body
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());


                String jsonPayload = "{\"loginId\":\"" + et_loginID.getText().toString() + "\",\"password\":\"" + et_password.getText().toString() +
                        "\",\"studentNumber\":\"" + Integer.parseInt(et_studentNumber.getText().toString()) + "\",\"nickname\":\"" + et_nickname.getText().toString() + "\"}";

                Log.d("jsonArray", jsonPayload.toString());

                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"request\"\r\n");
                outputStream.writeBytes("Content-Type: application/json\r\n\r\n");
                outputStream.write(jsonPayload.getBytes("UTF-8"));
                outputStream.writeBytes("\r\n");

                // Write the image file
                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n");
                outputStream.writeBytes("Content-Type: image/jpeg\r\n\r\n");

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.writeBytes("\r\n");

                // End of multipart/form-data
                outputStream.writeBytes("--" + boundary + "--\r\n");

                // Clean up resources
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();

                // Get the response from the server
                int responseCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Print the response
                System.out.println("Response Code: " + responseCode);
                System.out.println("Response Message: " + response.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
