package com.example.safer.models;

public class Danger {
    private String title;
    private String description;
    // TODO: add a user class
    private String user;
    private long unixTime;
    private String imgUrl;
    public Danger(String title, String description, String user, long unixTime, String imgUrl) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.unixTime = unixTime;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public String getImgUrl() {
        // TODO
        return "https://firebasestorage.googleapis.com/v0/b/safer-9d216.appspot.com/o/Safer%2Fimages%2FDDk6uaP7klgU01zQjlNMtGuA8gp1%2F-MM3cyecCAjc_4WBGDdM_0.jpg?alt=media&token=d8e313df-fc73-42a1-b1f9-a4fa54e93373";
//        return imgUrl;
    }
}
