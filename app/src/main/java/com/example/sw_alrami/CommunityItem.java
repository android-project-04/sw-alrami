package com.example.sw_alrami;

public class CommunityItem {
    String community;
    String date;
    int views;
    String mainText;

    int id;

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
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