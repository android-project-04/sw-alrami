package com.example.sw_alrami;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {

    private EditText et_loginID, et_password;
    private Button btn_login, btn_signup;

    private ApiService apiService;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_loginID = findViewById(R.id.login_id);
        et_password = findViewById(R.id.login_pw);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        sharedPreferences = getSharedPreferences("HeaderPrefs", MODE_PRIVATE);

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-3-39-25-103.ap-northeast-2.compute.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create ApiService instance
        apiService = retrofit.create(ApiService.class);

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

                    final ResponseToken accessToken = (ResponseToken)getApplication();
                    accessToken.Init();

                    accessToken.setAccessToken(response.headers().get("Authorization"));
                    Log.d("LoginActivity", "Authorization: " + accessToken.getAccessToken());
                    //String authorization = response.headers().get("Authorization");
                    //String refresh = response.headers().get("REFRESH");
                    //String authority = response.headers().get("AUTHORITY");

                    // Save headers in SharedPreferences
                    //saveHeaders(authorization, refresh, authority);

                    //Log.d("LoginActivity", "Authorization: " + authorization);
                    //Log.d("LoginActivity", "REFRESH: " + refresh);
                    //Log.d("LoginActivity", "AUTHORITY: " + authority);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    private void saveHeaders(String authorization, String refresh, String authority) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Authorization", authorization);
        editor.putString("REFRESH", refresh);
        editor.putString("AUTHORITY", authority);
        editor.apply();
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
