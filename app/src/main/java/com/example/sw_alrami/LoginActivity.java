package com.example.sw_alrami;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {

    private EditText et_loginID, et_password;
    private Button btn_login, btn_signup;
    private ApiService apiService;
    private String accesstoken, refreshtoken, authority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_loginID = findViewById(R.id.login_id);
        et_password = findViewById(R.id.login_pw);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        // Create ApiService instance
        apiService = ApiClient.getClient().create(ApiService.class);

        btn_login.setOnClickListener(view -> {
            // Call login API
            login();
        });

        btn_signup.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String loginId = et_loginID.getText().toString();
        String password = et_password.getText().toString();

        // Create login request object
        LoginRequest request = new LoginRequest(loginId, password);

        // Call login API using Retrofit
        Call<Void> call = apiService.login(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful login

                    accesstoken = response.headers().get("Authorization");
                    refreshtoken = response.headers().get("REFRESH");
                    authority = response.headers().get("AUTHORITY");

                    Log.d("LoginActivity", "Authorization: " + accesstoken);
                    Log.d("LoginActivity", "REFRESH: " + refreshtoken);
                    Log.d("LoginActivity", "AUTHORITY: " + authority);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("accesstoken", accesstoken);
                    intent.putExtra("refreshtoken", refreshtoken);
                    intent.putExtra("authority", authority);
                    startActivity(intent);
                } else {
                    // Handle unsuccessful login
                    Log.e("LoginActivity", "Login failed. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("LoginActivity", "Login request failed. " + t.getMessage());
            }
        });
    }

    // Define API service interface
    private interface ApiService {
        @POST("/api/member/login")
        Call<Void> login(@Body LoginRequest request);
    }

    // Define login request model
    public class LoginRequest {

        private String loginId;
        private String password;

        public LoginRequest(String loginId, String password) {
            this.loginId = loginId;
            this.password = password;
        }
    }

}
