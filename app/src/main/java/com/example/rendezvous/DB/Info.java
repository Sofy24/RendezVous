package com.example.rendezvous.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"title", "description"})
public class Info {

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
}
