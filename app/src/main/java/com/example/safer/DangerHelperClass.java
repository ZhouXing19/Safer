package com.example.safer;

import com.google.type.LatLng;

public class DangerHelperClass {
    String Title, Time, Description, Location, imageUrl, Category, UserId;
    double Latitude, Longitude;
    // Empty constructor
    public DangerHelperClass() {
    }

    // Overwritten constructor
    public DangerHelperClass(String time, String description, String location, String imageUrl,
                             String category, String title, String userid, double latitude, double longitude) {
        this.Category = category;
        this.Description = description;
        this.imageUrl = imageUrl;
        this.Latitude = latitude;
        this.Location = location;
        this.Longitude = longitude;
        this.Time = time;
        this.Title = title;
        this.UserId = userid;
    }

    // Getter for all attributes
    public String getTime() {
        return Time;
    }

    public String getDescription() {
        return Description;
    }

    public String getLocation() {
        return Location;
    }

    public String getImageUrl() { return imageUrl;}

    public String getTitle() { return Title; }

    public String getCategory() { return Category; }

    public String getUserId() { return UserId; }

    public double getLatitude() { return Latitude; }

    public double getLongitude() { return Longitude; }

    public void setTitle(String title) {
        Title = title;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
