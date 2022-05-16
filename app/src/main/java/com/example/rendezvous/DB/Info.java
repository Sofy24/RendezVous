package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Info {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer I_ID;

    @NonNull
    private String title;

    private String description;

    private String imageURL;

    private Double latitude;

    private Double longitude;

    public Info(@NonNull final String title, final String description, final String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        longitude = 0.0;
        longitude = 0.0;
    }

    @NonNull
    public String getTitle() { return title; }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() { return latitude; }

    public String getImageURL() {
        return imageURL;
    }

    @NonNull
    public Integer getI_ID(){
        return this.I_ID;
    }
    public void setI_ID(@NonNull Integer i_ID) {
        I_ID = i_ID;
    }
}
