package com.example.go4lunchAlx.service;

import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;

public interface ApiService {

    /**
     * Get the user currently logged
     *
     * @return {@link User}
     */
    User getCurrentUser();


    /**
     * Sets the user currently logged
     *
     * @param user
     */
    void setCurrentUser(User user);


    /**
     * Get all the known restaurants
     * @return {@link List}
     */
    List<Restaurant> getAllRestaurants();

    /**
     * Adds a restaurant to the list of restaurants already chosen by users
     * @param restaurant
     */
    void addFirestoreRestaurant(Restaurant restaurant);

    /**
     * Clears the list of restaurants already chosen by users
     */
    void clearFirestoreRestaurants();

    /**
     * Adds a restaurant to the list of restaurants found nearby the current location
     * @param restaurant
     */
    void addNearbyRestaurant(Restaurant restaurant);

    /**
     * Clears the list of restaurants found nearby the current location
     */
    void clearNearbyRestaurants();

    /**
     * Get the last current location
     *
     * @return {@link User}
     */
    LatLng getCurrentLocation();


    /**
     * Sets the user currently logged
     *
     * @param latLng
     */
    void setCurrentLocation(LatLng latLng);
}
