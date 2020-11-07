package com.example.safer.models;

public class Danger {
    private String title;
    private String description;
    // TODO: add a user class
    private String user;
    private long unixTime;

    public Danger(String title, String description, String user, long unixTime) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.unixTime = unixTime;
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
}
