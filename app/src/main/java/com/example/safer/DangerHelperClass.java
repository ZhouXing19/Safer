package com.example.safer;

import com.google.type.LatLng;
import org.parceler.Parcel;

@Parcel
public class DangerHelperClass {
    String Title, Time, timestamp, Description, Location, imageUrl, Category, UserId, videoUrl;
    double Latitude, Longitude;
    // Empty constructor
    public DangerHelperClass() {
    }



    // Overwritten constructor
    public DangerHelperClass(String time,
                             String timestamp,
                             String description,
                             String location,
                             String imageUrl,
                             String videoUrl,
                             String category,
                             String title,
                             String userid,
                             double latitude,
                             double longitude) {
        this.Category = category;
        this.Description = description;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.Latitude = latitude;
        this.Location = location;
        this.Longitude = longitude;
        this.Time = time;
        this.timestamp = timestamp;
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

    public String getTimestamp() { return timestamp; }

    public String getVideoUrl() { return videoUrl; }

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

    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
