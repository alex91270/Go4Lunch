package com.example.go4lunchAlx.models;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Restaurant {

    private String id;
    private String name;
    private List<String> attendants;
    private LatLng location;
    private String rating;
    private String vicinity;
    private String opening;
    @Nullable
    private String urlPicture;

    public Restaurant() { }

    public Restaurant(String id, String name, String urlPicture, LatLng location, String rating, String vicinity, String opening) {
        this.id = id;
        this.name = name;
        this.urlPicture = urlPicture;
        this.location = location;
        this.rating = rating;
        this.vicinity = vicinity;
        this.opening = opening;
    }
}
