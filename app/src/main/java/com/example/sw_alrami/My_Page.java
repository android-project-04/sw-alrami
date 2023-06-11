package com.example.sw_alrami;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class My_Page extends Fragment {

    private TextView tvNickname;
    private Button btnadminpage, btnlogout, btnnicknamechange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_page, container, false);

        tvNickname = view.findViewById(R.id.nickname);
        btnadminpage = view.findViewById(R.id.btnAdminpage);
        btnlogout = view.findViewById(R.id.btnLogout);
        btnnicknamechange = view.findViewById(R.id.nickname_change);

        btnlogout.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        Bundle bundle = getArguments();

        if (bundle != null) {
            String accesstoken = bundle.getString("accesstoken");
            String refreshtoken = bundle.getString("refreshtoken");
            String authority = bundle.getString("authority");

            if (authority.equals("ADMIN")) {
                btnadminpage.setVisibility(View.VISIBLE);
            } else {
                btnadminpage.setVisibility(View.INVISIBLE); //ADMIN 계정이 아닌경우 버튼 비활성화
            }

            btnnicknamechange.setOnClickListener(view1 -> {
                Intent intent = new Intent(getActivity(), NicknameChange.class);
                intent.putExtra("accesstoken", accesstoken);
                intent.putExtra("refreshtoken", refreshtoken);
                intent.putExtra("authority", authority);
                startActivity(intent);
            });

            btnadminpage.setOnClickListener(view1 -> {
                Intent intent1 = new Intent(getActivity(), MemberList.class);
                intent1.putExtra("accesstoken", accesstoken);
                intent1.putExtra("refreshtoken", refreshtoken);
                intent1.putExtra("authority", authority);
                startActivity(intent1);
            });

            MypageRequeset mypageRequeset = ApiClient.getClient().create(MypageRequeset.class);

            Call<MypageResponse> call = mypageRequeset.getMypage("Bearer " + accesstoken);

            call.enqueue(new Callback<MypageResponse>() {
                @Override
                public void onResponse(Call<MypageResponse> call, Response<MypageResponse> response) {
                    if(response.isSuccessful()){
                        MypageResponse mypageinfo = response.body();
                        String mynickname = mypageinfo.data.nickname;

                        tvNickname.setText(mynickname);
                    } else {
                        Log.d("Mypage", "Mypage Load failed. Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<MypageResponse> call, Throwable t) {
                    Log.d("Mypage", "Mypage Request failed" + t.getMessage());
                }
            });
        } else {
            Log.e("Mypage", "bundle data is null!");
        }

        return view;
    }

    public interface MypageRequeset {
        @GET("/api/member/mypage")
        Call<MypageResponse> getMypage(@Header("Authorization") String token);

    }

    public class MypageResponse {
        @SerializedName("eventTime")
        private String eventTime;

        @SerializedName("status")
        private String status;

        @SerializedName("code")
        private int code;

        @SerializedName("data")
        private MypageResponseData data;
    }

    public class MypageResponseData {
        @SerializedName("nickname")
        private String nickname;

        @SerializedName("studentNumber")
        private int studentNumber;

        @SerializedName("responseMyPageCommunities")
        private List<MypageResponseCommunity> responseMyPageCommunities;
    }

    public class MypageResponseCommunity {
        @SerializedName("communityId")
        private int communityId;

        @SerializedName("title")
        private String title;
    }

}