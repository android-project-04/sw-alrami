package com.example.sw_alrami;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Notice_Page extends Fragment {

    ApiResponse dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_page, container, false);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse> call = apiInterface.getData();

        call.enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                //dataList = response.body();
                //Log.d("Notice_Page", dataList.toString());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Notice_Page", t.toString());
            }
        });

        return view;
    }
}

