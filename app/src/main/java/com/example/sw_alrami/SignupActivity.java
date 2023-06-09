package com.example.sw_alrami;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class SignupActivity extends AppCompatActivity {

    private EditText et_loginID, et_password, et_nickname, et_studentNumber;
    private File imageFile = new File("/sdcard/Pictures/IMG_20230528_111505.jpg");

    private String imageFilePath;
    private Button btn_signup, btn_camera;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        btn_signup = findViewById(R.id.btn_signup_ok);
        btn_signup.setOnClickListener(view -> {
            signup();
        });

    }

    private void signup() {
        String loginId = et_loginID.getText().toString();
        String password = et_password.getText().toString();
        Double studentNumber = Double.parseDouble(et_studentNumber.getText().toString());
        String nickname = et_nickname.getText().toString();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        SignupApi signupApi = ApiClient.getClient().create(SignupApi.class);

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/png"), imageFilePath);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "image.png", fileRequestBody);

        RequestBody requestRequestBody = RequestBody.create(JSON, "{\"loginId\":\"" + loginId + "\",\"password\":\"" + password + "\",\"studentNumber\":" + studentNumber + ",\"nickname\":\"" + nickname + "\"}");


        // Call login API using Retrofit
        Call<ResponseBody> call = signupApi.signup(filePart.body(), requestRequestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle successful login
                    Log.e("SignupActivity", "response: " + response.code());

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                } else {
                    // Handle unsuccessful login
                    Log.e("SignupActivity", "request: " + call.toString());
                    Log.e("SignupActivity", "Signup failed. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Log.e("SignupActivity", "Signup request failed. " + t.getMessage());
            }
        });
    }

    // Define API service interface
    private interface SignupApi {
        @Multipart
        @POST("/api/member/signup")
        Call<ResponseBody> signup(@Part("file\"; filename=\"image.png\"") RequestBody file,
                                  @Part("request") RequestBody request);
    }

    // Define login request model
    public class SignupRequest {

        private String loginId;
        private String password;
        private Number studentNumber;
        private String nickname;

        public SignupRequest(String loginId, String password, Number studentNumber, String nickname) {
            this.loginId = loginId;
            this.password = password;
            this.studentNumber = studentNumber;
            this.nickname = nickname;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageFilePath = imageUri.getPath();
        }
    }

}
