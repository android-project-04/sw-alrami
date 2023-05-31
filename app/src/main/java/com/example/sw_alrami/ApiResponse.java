package com.example.sw_alrami;

public class ApiResponse {
    private String eventTime;
    private String status;
    private Number code;
    private Data data;

    // getters and setters

    @Override
    public String toString() {
        return "ApiResponse{" +
                "eventTime='" + eventTime + '\'' +
                ", status='" + status + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}