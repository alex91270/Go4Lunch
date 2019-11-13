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

    public Restaurant(String id) {
        this.id = id;
        this.attendants = new ArrayList<>();
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getPhoto() {return photo;}

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getAttendants() {return attendants;}

    public void addAttendant(String name){attendants.add(name);}

    public void deleteAttendant(String name) {
        attendants.remove(name);
    }

    public void clearAttendants() {attendants.clear();}

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getVicinity() {return vicinity;}

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getRating() {return rating;}

    public void setRating(double rating){
        this.rating = rating;
    }

    public String getOpening() {return opening;}

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public void setAttendants(ArrayList<String> attendants) {
        this.attendants = attendants;
    }

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
