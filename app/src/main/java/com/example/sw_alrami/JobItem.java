package com.example.sw_alrami;

public class JobItem {
    String job;
    String date;
    int views;
    String mainText;
    int id;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getMainText() { return mainText; }

    public void setMainText(String mainText) { this.mainText = mainText; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
