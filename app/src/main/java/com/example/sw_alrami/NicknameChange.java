package com.example.sw_alrami;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class NicknameChange extends AppCompatActivity {

    private Button btnok, btncancel;
    private EditText changeNickname;
    private String accesstoken, refreshtoken, authority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nickname_change);

        changeNickname = findViewById(R.id.changeNickname);
        btnok = findViewById(R.id.btn_change_ok);
        btncancel = findViewById(R.id.btn_change_cancel);

        Intent intent = getIntent();
        accesstoken = intent.getStringExtra("accesstoken");
        refreshtoken = intent.getStringExtra("refreshtoken");
        authority = intent.getStringExtra("authority");

        btncancel.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.putExtra("accesstoken", accesstoken);
            intent1.putExtra("refreshtoken", refreshtoken);
            intent1.putExtra("authority", authority);
            startActivity(intent1);
        });

        btnok.setOnClickListener(view -> {
            String change = changeNickname.getText().toString();

            Log.d("nicknamechange", "input nick: " + change);

            Log.d("nicknamechange", "input nick: " + "?nickname=" + change);

            NicknamechangeRequest nicknamechangeRequest = ApiClient.getClient().create(NicknamechangeRequest.class);

            Call<NicknamechangeResponse> call = nicknamechangeRequest.getNicknamechange("Bearer " + accesstoken, change);

            call.enqueue(new Callback<NicknamechangeResponse>() {
                @Override
                public void onResponse(Call<NicknamechangeResponse> call, Response<NicknamechangeResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("NicknameChange", "Nickname Change success. " + response.body().data.nickname);
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        intent1.putExtra("accesstoken", accesstoken);
                        intent1.putExtra("refreshtoken", refreshtoken);
                        intent1.putExtra("authority", authority);
                        startActivity(intent1);
                    } else {
                        Log.e("NicknameChange", "Nickname Change failed. " + response.code());
                        Toast.makeText(getApplicationContext(), "닉네임을 바꾼지 30일이 지나지 않았습니다.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<NicknamechangeResponse> call, Throwable t) {
                    Log.e("NicknameChange", "Nickname Change request failed. " + t.getMessage());
                }
            });
        });


    }

    public interface NicknamechangeRequest {
        @PUT("/api/member/nickname?")
        Call<NicknamechangeResponse> getNicknamechange(@Header("Authorization") String token, @Query("nickname") String change);
    }

    public class NicknamechangeResponse {
        @SerializedName("eventTime")
        private String eventTime;

        @SerializedName("status")
        private String status;

        @SerializedName("code")
        private int code;

        @SerializedName("data")
        private NicknamechangeResponseData data;
    }

    public class NicknamechangeResponseData {
        @SerializedName("id")
        private int id;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("studentNumber")
        private int studentNumber;
    }

}
