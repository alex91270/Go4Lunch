package com.example.go4lunchAlx.service;

import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

public class RestApiService implements ApiService {

    private List<User> firebaseUsers = new ArrayList<>();
   private List<Restaurant> firestoreRestaurants = new ArrayList<>();
    private List<Restaurant> nearbyRestaurants = new ArrayList<>();
    private User currentUser = null;
    private LatLng currentLocation;
    private List<Rating> listOfRatings = new ArrayList<>();
    private List<Restaurant> allRestaurants = new ArrayList<>();
    private String currentUserId = null;


    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        currentUser = user;
    }


    @Override
    public void setCurrentUserId(String uid) {
        currentUserId = uid;
    }

    @Override
    public String getCurrentUserId() {
        return currentUserId;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {


       //allRestaurants.addAll(firestoreRestaurants);
        //allRestaurants.addAll(nearbyRestaurants);

        return allRestaurants;
    }

    @Override
    public Restaurant getRestaurantById(String id){
        for (Restaurant restaurant : allRestaurants) {
            if ( restaurant.getId().equals(id)) {
                return restaurant;
            }
        }
        return null;
    }

    @Override
    public void setAllRestaurantsList(List<Restaurant> allRestaurantsList) {
        allRestaurants = allRestaurantsList;
    }

    @Override
    public void addRestaurantToAll(Restaurant restaurant) {
        allRestaurants.add(restaurant);
    }

    @Override
    public void clearAllRestaurants() {
        allRestaurants.clear();
    }

    @Override
    public void addFirestoreRestaurant(Restaurant restaurant) {
        firestoreRestaurants.add(restaurant);
    }

    @Override
    public void addFirebaseUser(User user) {
        firebaseUsers.add(user);
    }

    @Override
    public void clearFirebaseUsers() {firebaseUsers.clear();}

    @Override
    public List<User> getFirebaseUsers() {return firebaseUsers;}

    @Override
    public void clearFirestoreRestaurants() {
        firestoreRestaurants.clear();
    }

    @Override
    public void addNearbyRestaurant(Restaurant restaurant) {
        nearbyRestaurants.add(restaurant);
    }

    @Override
    public List<Restaurant> getNearbyRestaurants() {
        return nearbyRestaurants;
    }

    @Override
    public void clearNearbyRestaurants() {
        nearbyRestaurants.clear();
    }

    @Override
    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public void setCurrentLocation(LatLng latLng) {
        currentLocation = latLng;
    }

    @Override
    public List<Rating> getListOfRatings() { return listOfRatings;}

    @Override
    public void addRating(Rating rating) { listOfRatings.add(rating);}

    @Override
    public void clearListOfRatings() { listOfRatings.clear();}

    @Override
    public void addAttendantToRestaurant(String restoId, String attendantId) {
        for (Restaurant restaurant : allRestaurants) {
            if ( restaurant.getId().equals(restoId)) {
                restaurant.addAttendant(attendantId);
            }
        }
    }
}
