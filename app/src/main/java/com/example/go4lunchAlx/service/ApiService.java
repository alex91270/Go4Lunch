package com.example.go4lunchAlx.service;

import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;

public interface ApiService {

    /**
     * Adds a user to the list of users gotten from firebase
     * @param user
     */
    void addFirebaseUser(User user);



    /**
     * Clears the list of users gotten from firebase
     */
    void clearFirebaseUsers();

    /**
     * Get the list of users retrieved from firebase
     * @return {@link User}
     */
    List<User> getFirebaseUsers();



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

    void setCurrentUserId(String uid);

    String getCurrentUserId();


    /**
     * Get all the known restaurants
     * @return {@link List}
     */
    List<Restaurant> getRestaurants();

    /**
     * Get a specific restaurant by it's id
     * @return {@link Restaurant}
     */
    Restaurant getRestaurantById(String id);

    /**
     * Adds a restaurant to the list of all restaurants
     * @param restaurant
     */
    void addRestaurant(Restaurant restaurant);

    /**
     * Sets the list of all restaurants
     * @param allRestaurantsList
     */
    void setRestaurantsList(List<Restaurant> allRestaurantsList);

    /**
     * Clears the list of all restaurants
     */
    void clearRestaurants();

    void updateRestaurant(Restaurant restaurant);

    /**
     * Adds a restaurant to the list of restaurants already chosen by users
     * @param restaurant
     */
    //void addFirestoreRestaurant(Restaurant restaurant);

    /**
     * Clears the list of restaurants already chosen by users
     */
    //void clearFirestoreRestaurants();


    /**
     * Get the list of restaurants found nearby
     * @return {@link Restaurant}
     */
    //List<Restaurant> getNearbyRestaurants();

    /**
     * Adds a restaurant to the list of restaurants found nearby the current location
     * @param restaurant
     */
    //void addNearbyRestaurant(Restaurant restaurant);

    /**
     * Clears the list of restaurants found nearby the current location
     */
    //void clearNearbyRestaurants();

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

    /**
     * Get the list of ratings retrieved from firebase
     * @return {@link Rating}
     */
   List<Rating> getListOfRatings();

    /**
     * Adds a rating to the list of ratings retrieved from firebase
     * @param rating
     */
   void addRating(Rating rating);

    /**
     * Clears the list of of ratings retrieved from firebase
     */
   void clearListOfRatings();


    void updateRating(Rating rating);

    /**
     * Adds an attendant to a specific restaurant
     * @param restoId
     * @param attendantId
     */
    void addAttendantToRestaurant(String restoId, String attendantId);

    void removeAttendantFromRestaurant(String attendantId, String restoId);

    User getUserById(String id);

    void setUserSelectedRestaurant(String userId, String selectedRestaurant);

    String getRestaurantIdByName(String name);
}
