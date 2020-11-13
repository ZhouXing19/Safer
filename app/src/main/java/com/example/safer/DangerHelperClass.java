package com.example.safer;

public class DangerHelperClass {
    String Time, Description, Location;

    // Empty constructor
    public DangerHelperClass() {
    }

    // Overwritten constructor
    public DangerHelperClass(String time, String description, String location) {
        this.Time = time;
        this.Description = description;
        this.Location = location;
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
}
