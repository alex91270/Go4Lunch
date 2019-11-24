package com.example.go4lunchAlx.service;

import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

public class RestApiService implements ApiService {

    private List<User> firebaseUsers = new ArrayList<>();
   //private List<Restaurant> firestoreRestaurants = new ArrayList<>();
    //private List<Restaurant> nearbyRestaurants = new ArrayList<>();
    private User currentUser = null;
    private LatLng currentLocation;
    private List<Rating> listOfRatings = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
    private String currentUserId = null;
    private String myEmailAddress;


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
    public User getUserById(String id){
        for (User user : firebaseUsers) {
            if (user.getUid().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void setUserSelectedRestaurant(String userId, String selectedRestaurant) {
        getUserById(userId).setSelectedRestaurant(selectedRestaurant);
    }

    @Override
    public List<Restaurant> getRestaurants() {
       //allRestaurants.addAll(firestoreRestaurants);
        //allRestaurants.addAll(nearbyRestaurants);
        return restaurants;
    }

    @Override
    public Restaurant getRestaurantById(String id){
        for (Restaurant restaurant : restaurants) {
            if ( restaurant.getId().equals(id)) {
                return restaurant;
            }
        }
        return null;
    }

    @Override
    public String getRestaurantIdByName(String name) {
        for (Restaurant restaurant : restaurants) {
            if ( restaurant.getName().equals(name)) {
                return restaurant.getId();
            }
        }
        return null;
    }

    @Override
    public void setRestaurantsList(List<Restaurant> RestaurantsList) {
        restaurants = RestaurantsList;
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
    restaurants.set(restaurants.indexOf(restaurant), restaurant);
    }

    @Override
    public void clearRestaurants() {
        restaurants.clear();
    }

    /**@Override
    public void addFirestoreRestaurant(Restaurant restaurant) {
        firestoreRestaurants.add(restaurant);
    }
     */

    @Override
    public void addFirebaseUser(User user) {
        firebaseUsers.add(user);
    }

    @Override
    public void clearFirebaseUsers() {firebaseUsers.clear();}

    @Override
    public List<User> getFirebaseUsers() {return firebaseUsers;}

    /**@Override
    public void clearFirestoreRestaurants() {
        firestoreRestaurants.clear();
    }
     */

    /**@Override
    public void addNearbyRestaurant(Restaurant restaurant) {
        nearbyRestaurants.add(restaurant);
    }
     */

    /**@Override
    public List<Restaurant> getNearbyRestaurants() {
        return nearbyRestaurants;
    }
     */

    /**@Override
    public void clearNearbyRestaurants() {
        nearbyRestaurants.clear();
    }
     */

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
    public void updateRating(Rating rating) {
        listOfRatings.set(listOfRatings.indexOf(rating), rating);
    }

    @Override
    public void clearListOfRatings() { listOfRatings.clear();}

    @Override
    public void addAttendantToRestaurant(String restoId, String attendantId) {
        for (Restaurant restaurant : restaurants) {
            if ( restaurant.getId().equals(restoId)) {
                if (!restaurant.getAttendants().contains(attendantId)) {
                    restaurant.addAttendant(attendantId);
                }
            }
        }
    }

    @Override
    public void removeAttendantFromRestaurant(String attendantId, String restoId) {
        for (Restaurant restaurant : restaurants) {
            if ( restaurant.getId().equals(restoId)) {
                restaurant.deleteAttendant(attendantId);
            }
        }
    }

    @Override
    public String getMyEmailAddress() {
        return myEmailAddress;
    }

    @Override
    public void setMyEmailAddress(String email) {
        myEmailAddress = email;
    }
}
