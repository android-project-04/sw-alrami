package com.example.sw_alrami;

import android.app.Application;

public class ResponseToken extends Application {
    
    private static String accessToken;
    
    @Override
    public void onCreate() {
        super.onCreate();
        accessToken = "";
    }
    
    public void Init() {    //변수 초기화
        accessToken = "";
    }
    
    public void setAccessToken(String s) {  //값 초기화
        this.accessToken = s;
    }
    
    public String getAccessToken() {    //값 불러오기
        return accessToken;
    }
    
}
