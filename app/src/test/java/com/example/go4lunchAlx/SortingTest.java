package com.example.go4lunchAlx;

import com.example.go4lunchAlx.comparators.SortByAttendants;
import com.example.go4lunchAlx.comparators.SortByDistance;
import com.example.go4lunchAlx.comparators.SortByRate;
import com.example.go4lunchAlx.service.di.DI;
import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SortingTest {

    private Restaurant r1;
    private Restaurant r2;
    private Restaurant r3;

    private List<Restaurant> restaurants;
    private RestApiService service = DI.getRestApiService();

    @Before
    public void init() {
        restaurants = new ArrayList<>();
        r1 = new Restaurant("resto1");
        r2 = new Restaurant("resto2");
        r3 = new Restaurant("resto3");

        r1.addAttendant("user1");
        r1.addAttendant("user2");
        r2.addAttendant("user3");

        r1.setDistance(1000);
        r2.setDistance(10);
        r3.setDistance(100);

        restaurants.add(r1);
        restaurants.add(r2);
        restaurants.add(r3);

        service.addRating(new Rating("rating1", "resto3", "user1", 5));
        service.addRating(new Rating("rating2", "resto2", "user1", 4));
        service.addRating(new Rating("rating3", "resto1", "user1", 1));

    }

    @Test
    public void compare() {
        Collections.sort(restaurants, new SortByAttendants());
        assertEquals(r1, restaurants.get(0));
        Collections.sort(restaurants, new SortByDistance());
        assertEquals(r2, restaurants.get(0));
        Collections.sort(restaurants, new SortByRate());
        assertEquals(r3, restaurants.get(0));
    }
}