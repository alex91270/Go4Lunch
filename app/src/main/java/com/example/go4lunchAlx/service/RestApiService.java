package com.example.go4lunchAlx.service;

import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

public class RestApiService implements ApiService {

    private List<Restaurant> firestoreRestaurants = new ArrayList<>();
    private List<Restaurant> nearbyRestaurants = new ArrayList<>();
    private User currentUser = null;
    private LatLng currentLocation;



    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> allRestaurants = new ArrayList<>();

        allRestaurants.addAll(firestoreRestaurants);
        allRestaurants.addAll(nearbyRestaurants);

        return allRestaurants;
    }

    @Override
    public void addFirestoreRestaurant(Restaurant restaurant) {
        firestoreRestaurants.add(restaurant);
    }

    @Override
    public void clearFirestoreRestaurants() {
        firestoreRestaurants.clear();
    }

    @Override
    public void addNearbyRestaurant(Restaurant restaurant) {
        nearbyRestaurants.add(restaurant);
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
}
