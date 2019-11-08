package com.example.go4lunchAlx.models;
import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    @Nullable private String urlPicture;
    @Nullable private String selectedRestaurant;
    @Nullable private long dateSelection;

    public User() { }

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
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setSelectedRestaurant(String restaurantID) { this.selectedRestaurant = restaurantID; }
    public void setDateSelection(Long dateSelection) { this.dateSelection = dateSelection;}

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        if (uid.equals(user.getUid())) {
            return true;
        } else {
            return false;
        }
    }
}

