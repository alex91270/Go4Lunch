package com.example.go4lunchAlx.models;
import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    @Nullable private String urlPicture;
    @Nullable private String selectedRestaurant;
    @Nullable private long dateSelection;

    public User() {
    }

    public User(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
       }

    public User(String uid, String username, String urlPicture, String selectedRestaurant, long dateSelection) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.selectedRestaurant = selectedRestaurant;
        this.dateSelection = dateSelection;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getSelectedRestaurant() { return selectedRestaurant; }
    public Long getDateSelection() { return dateSelection;}

    // --- SETTERS ---
    public void setSelectedRestaurant(String restaurantID) { this.selectedRestaurant = restaurantID; }

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return (uid.equals(user.getUid()));
    }
}

