package com.example.sw_alrami;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("api/notification/list")
    Call<ApiResponse> getData();

}