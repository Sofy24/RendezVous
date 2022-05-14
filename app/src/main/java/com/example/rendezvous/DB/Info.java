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
    private final String title;

    @NonNull
    private final String description;

    private final String imageURL;

    public Info(final String title, final String description, final String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Integer getI_ID(){
        return this.I_ID;
    }
    public void setI_ID(@NonNull Integer i_ID) {
        I_ID = i_ID;
    }
}
