package com.example.go4lunchAlx.service;

import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;

public interface ApiService {

    void addFirebaseUser(User user);

    void clearFirebaseUsers();

    List<User> getFirebaseUsers();

    User getCurrentUser();

    void setCurrentUser(User user);

    void setCurrentUserId(String uid);

    String getCurrentUserId();

    List<Restaurant> getRestaurants();

    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurantById(String id);

    void addRestaurant(Restaurant restaurant);

    void addRestaurantToSearched(Restaurant restaurant);

    void clearRestaurants();

    void updateRestaurant(Restaurant restaurant);

    void updateSearchedRestaurant(Restaurant restaurant);

    LatLng getCurrentLocation();

    void setCurrentLocation(LatLng latLng);

    List<Rating> getListOfRatings();

   void addRating(Rating rating);

   void clearListOfRatings();

    void updateRating(Rating rating);

    void addAttendantToRestaurant(String restoId, String attendantId);

    void removeAttendantFromRestaurant(String attendantId, String restoId);

    User getUserById(String id);

    void setUserSelectedRestaurant(String userId, String selectedRestaurant);

    String getRestaurantIdByName(String name);

    String getMyEmailAddress();

    void setMyEmailAddress(String email);
}
