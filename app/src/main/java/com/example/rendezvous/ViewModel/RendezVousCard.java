package com.example.rendezvous.ViewModel;

public class RendezVousCard {
    private String title;

    private String imageUri;

    private Integer I_ID;

    private final String description;

    private final Double latitude;

    private final Double longitude;

    public RendezVousCard(String title, String imageUri, Integer i_id, String description, Double latitude, Double longitude) {
        this.title = title;
        this.imageUri = imageUri;
        this.I_ID = i_id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    public Double getLongitude(){
        return this.longitude;
    }

    public String getDescription(){
        return this.description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Integer getI_ID() {
        return I_ID;
    }

    public void setI_ID(Integer i_ID) {
        I_ID = i_ID;
    }

    @Override
    public String toString() {
        return "RendezVousCard{" +
                "title='" + title + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", I_ID=" + I_ID +
                '}';
    }
}
