package com.example.rendezvous;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class which represents every card item with its information (image, place, data, description)
 */
@Entity(tableName = "item")
public class CardItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    private int id;

    @ColumnInfo(name = "item_image")
    private final String imageResource;
    @ColumnInfo(name = "item_name")
    private final String placeName;
    @ColumnInfo(name = "item_description")
    private final String placeDescription;
    @ColumnInfo(name = "item_date")
    private final String date;

    public CardItem(String imageResource, String placeName, String placeDescription, String date) {
        this.imageResource = imageResource;
        this.placeName = placeName;
        this.placeDescription = placeDescription;
        this.date = date;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
