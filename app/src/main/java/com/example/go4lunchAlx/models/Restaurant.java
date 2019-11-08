package com.example.go4lunchAlx.models;

import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private String id;
    private String name;
    private List<String> attendants;
    private LatLng location;
    private double rating;
    private String vicinity;
    private String opening;
    @Nullable
    private String photo;
    private int distance;

    public Restaurant(String id, List<String> attendants) {
        this.id = id;
        this.attendants = attendants;
    }

    public Restaurant(String id, String name, String photo, LatLng location, double rating, String vicinity, String opening, int distance) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.location = location;
        this.rating = rating;
        this.vicinity = vicinity;
        this.opening = opening;
        this.distance = distance;
        this.attendants = new ArrayList<>();
    }

    public String getId() {return id;}

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {return photo;}

    public List<String> getAttendants() {return attendants;}

    public void addAttendant(String name){attendants.add(name);}

    public void clearAttendants() {attendants.clear();}

    public int getDistance() {
        return distance;
    }

    public String getVicinity() {return vicinity;}

    public double getRating() {return rating;}

    public String getOpening() {return opening;}

    @Override
    public boolean equals(Object o) {
        Restaurant resto = (Restaurant) o;
        if (id.equals(resto.getId())) {
            return true;
        } else {
            return false;
        }

    }
}
