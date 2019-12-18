package com.example.go4lunchAlx.models;

public class Rating {

    private String rID;
    private String restaurantID;
    private String userID;
    private int rate;

    public Rating(){}

    public Rating(String rID, String restaurantID, String userID, int rate) {
        this.rID = rID;
        this.restaurantID = restaurantID;
        this.userID = userID;
        this.rate = rate;
    }

    public String getRestaurantID() {return restaurantID;}

    private String getUserID() {return userID;}

    public String getrID() {return rID;}

    public int getRate() {return rate;}

    @Override
    public boolean equals(Object o) {
        Rating rating = (Rating) o;
        if (restaurantID.equals(rating.getRestaurantID()) && userID.equals(rating.getUserID())) {
            return true;
        } else {
            return false;
        }
    }
}
