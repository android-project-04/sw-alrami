package com.example.sw_alrami;


public class Value {
    private int id;
    private String title;
    private String url;

    // getters and setters

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
