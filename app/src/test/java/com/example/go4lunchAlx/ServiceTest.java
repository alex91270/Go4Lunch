package com.example.go4lunchAlx;

import com.example.go4lunchAlx.helpers.DistanceHelper;
import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.service.di.DI;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class ServiceTest {
    private RestApiService service = DI.getRestApiService();
    private User user1;
    private User user2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private Rating rating1;
    private Rating rating2;
    private Rating rating3;

    @Before
    public void setup() {
        user1 = new User("U1", "John Doe", "pictureUrl");
        user2 = new User("U2", "Jane Doe", "pictureUrl");
        restaurant1 = new Restaurant("R1", "restaurant 1", "photoUrl",
                new LatLng(1,1), "address1", "closed", 1);
        restaurant2 = new Restaurant("R2", "restaurant 2", "photoUrl",
                new LatLng(1,1), "address2", "closed", 2);
        rating1 = new Rating("Ra1", "R1", "U1", 2 );
        rating2 = new Rating("Ra2", "R1", "U2", 4);
        rating3 = new Rating("Ra3", "R1", "U2", 3 );
    }

    @Test
    public void setGetCurrentUser() {
        service.setCurrentUser(user1);
        assertEquals (service.getCurrentUser(), user1);
    }

    @Test
    public void setGetCurrentUserId() {
        service.setCurrentUserId("1");
        assertEquals (service.getCurrentUserId(), "1");
    }

    @Test
    public void getUserById() {
        service.addFirebaseUser(user1);
        assertEquals(service.getUserById("U1"), user1);
    }

    @Test
    public void setGetUserSelectedRestaurant() {
        service.setCurrentUserId("U1");
        service.addFirebaseUser(user1);
        service.setUserSelectedRestaurant("U1", "R1" );

        User user = service.getUserById(service.getCurrentUserId());
        String selectedResto = user.getSelectedRestaurant();
        Restaurant resto = service.getRestaurantById(selectedResto);

        assertEquals(resto, restaurant1);
    }

    @Test
    public void setGetRestaurants() {
        service.clearRestaurants();
        service.clearSearchedRestaurants();
        service.addRestaurant(restaurant1);
        service.addRestaurantToSearched(restaurant2);
        assertEquals(service.getRestaurants().size(), 1);
        assertEquals(service.getAllRestaurants().size(), 2);
    }

    @Test
    public void getRestaurantById() {
        assertEquals(service.getRestaurantById("R1"), restaurant1);
    }

    @Test
    public void getRestaurantIdByName() {
        assertEquals(service.getRestaurantIdByName("restaurant 1"), "R1");
    }

    @Test
    public void updateRestaurant() {
        service.clearRestaurants();
        service.addRestaurant(restaurant1);
        restaurant1 = new Restaurant("R1", "restaurant 1", "photoUrlBis",
                new LatLng(1,1), "address1", "closed", 1);
        service.updateRestaurant(restaurant1);
        assertEquals(service.getRestaurantById("R1").getPhoto(), "photoUrlBis");
    }

    @Test
    public void updateSearchedRestaurant() {
        service.clearSearchedRestaurants();
        service.addRestaurantToSearched(restaurant2);
        restaurant2 = new Restaurant("R2", "restaurant 2", "photoUrlBis",
                new LatLng(1,1), "address2", "closed", 2);
        service.updateSearchedRestaurant(restaurant2);

        assertEquals(service.getRestaurantById("R2").getPhoto(), "photoUrlBis");
    }

    @Test
    public void clearRestaurants() {
        service.clearRestaurants();
        assertEquals(service.getRestaurants().size(), 0);
    }

    @Test
    public void addGetFirebaseUser() {
        service.addFirebaseUser(user1);
        service.addFirebaseUser(user2);
        assertEquals(service.getFirebaseUsers().size(), 2);
    }

    @Test
    public void clearFirebaseUsers() {
        service.addFirebaseUser(user1);
        service.clearFirebaseUsers();
        assertEquals(service.getFirebaseUsers().size(), 0);
    }

    @Test
    public void setGetCurrentLocation() {
        LatLng location = new LatLng(2, 2);
        service.setCurrentLocation(location);
        assertTrue(service.getCurrentLocation() != null);

        DistanceHelper distanceHelper = new DistanceHelper();
        assertEquals(distanceHelper.calculateDistance(service.getCurrentLocation(), location), 0);
    }


    @Test
    public void addClearGetRatings() {
        service.addRating(rating1);
        service.clearListOfRatings();
        service.addRating(rating2);

        assertEquals(service.getListOfRatings().size(), 1);
    }

    @Test
    public void updateRatingAndGetRate() {
        service.clearRestaurants();
        service.addRestaurant(restaurant1);
        service.addRating(rating1);
        service.addRating(rating3);

        service.updateRating(rating2);

        assertTrue(service.getRate(service.getRestaurantById("R1")) == 3);
    }

    @Test
    public void addRemoveAttendantToRestaurant() {
        service.clearRestaurants();
        service.addRestaurant(restaurant1);
        service.addAttendantToRestaurant("R1", "U1");
        service.addAttendantToRestaurant("R1", "U2");

        service.removeAttendantFromRestaurant("U2", "R1");

        assertEquals(service.getRestaurantById("R1").getAttendants().size(), 1);
    }
}