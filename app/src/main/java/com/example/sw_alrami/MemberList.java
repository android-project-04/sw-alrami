package com.example.sw_alrami;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class MemberList extends AppCompatActivity {

    private ListView lvmemberlist;
    private ArrayAdapter<String> adapter;
    private List<String> titles;

    private String accesstoken, refreshtoken, authority;

    private String approvalUrl = "/api/admin/approval/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberlist_page);

        lvmemberlist = findViewById(R.id.memberlist);
        titles = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        lvmemberlist.setAdapter(adapter);

        Intent intent = getIntent();
        accesstoken = intent.getStringExtra("accesstoken");
        refreshtoken = intent.getStringExtra("refreshtoken");
        authority = intent.getStringExtra("authority");

        Log.d("Memberlist", "Authorization: " + accesstoken);
        Log.d("Memberlist", "REFRESH: " + refreshtoken);
        Log.d("Memberlist", "AUTHORITY: " + authority);

        MemberlistRequest memberlistRequest = ApiClient.getClient().create(MemberlistRequest.class);

        Call<MemberlistResponse> call = memberlistRequest.getMemeberlist("Bearer " + accesstoken);

        call.enqueue(new Callback<MemberlistResponse>() {
            @Override
            public void onResponse(Call<MemberlistResponse> call, Response<MemberlistResponse> response) {
                if (response.isSuccessful()) {
                    MemberlistResponse memberlist = response.body();
                    List<MemberlistResponseItem> values = memberlist.data.values;
                    for (MemberlistResponseItem value : values) {
                        String nickname = value.nickname;
                        titles.add(nickname);
                        //Log.d("Memberlist", "member list: " + nickname);
                    }
                    adapter.notifyDataSetChanged();

                    lvmemberlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MemberlistResponseItem selectedUser = values.get(position);

                            showPopup(selectedUser);

                            adapter.notifyDataSetChanged();
                        }
                    });

                } else {
                    // Handle unsuccessful login
                    Log.e("Memberlist", "List Load failed. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MemberlistResponse> call, Throwable t) {
                Log.e("MemberList", "List request failed. " + t.getMessage());
            }
        });
    }

    public interface MemberlistRequest {
        @GET("/api/admin/no/approval/members")
        Call<MemberlistResponse> getMemeberlist(@Header("Authorization") String token);
    }

    public class MemberlistResponse {
        @SerializedName("eventTime")
        private String eventTime;

        @SerializedName("status")
        private String status;

        @SerializedName("code")
        private int code;

        @SerializedName("data")
        private MemberlistResponseData data;
    }

    public class MemberlistResponseData {
        @SerializedName("values")
        private List<MemberlistResponseItem> values;

        @SerializedName("hasNext")
        private boolean hasNext;

        @SerializedName("lastIndex")
        private int lastIndex;
    }

    public class MemberlistResponseItem {
        @SerializedName("id")
        private int id;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("studentNumber")
        private int studentNumber;
    }

    private void showPopup(MemberlistResponseItem user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원 정보")
                .setMessage("Nickname: " + user.nickname + "\nStudent Number: " + user.studentNumber + "\nid: " + user.id)
                .setPositiveButton("회원 승인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApprovalRequest approvalRequest = ApiClient.getClient().create(ApprovalRequest.class);
                        Call<ApprovalResponse> call = approvalRequest.getApproval("Bearer " + accesstoken, user.id);
                        call.enqueue(new Callback<ApprovalResponse>() {
                            @Override
                            public void onResponse(Call<ApprovalResponse> call, Response<ApprovalResponse> response) {
                                if(response.isSuccessful()){
                                    Log.d("ApprovalActivity", "Success: " + response.body());
                                } else {
                                    Log.e("ApprovalActivity", "User Approval failed. Response code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<ApprovalResponse> call, Throwable t) {
                                Log.e("ApprovalActivity", "User Approval request failed." + t.getMessage());
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface ApprovalRequest {
        @PUT("/api/admin/approval/{id}")

        Call<ApprovalResponse> getApproval(@Header("Authorization") String token, @Path("id") int id);
    }

    public class ApprovalResponse {
        @SerializedName("eventTime")
        private String eventTime;

        @SerializedName("status")
        private String status;

        @SerializedName("code")
        private int code;

        @SerializedName("data")
        private int data;
    }

}
