package com.example.go4lunchAlx.comparators;

import com.example.go4lunchAlx.models.Restaurant;

import java.util.Comparator;

//comparator for sorting by distance

public class SortByDistance implements Comparator<Restaurant> {

    @Override
    public int compare(Restaurant r1, Restaurant r2) {

        return (int) (r1.getDistance() - r2.getDistance());
    }
}