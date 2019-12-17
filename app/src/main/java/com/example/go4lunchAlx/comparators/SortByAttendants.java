package com.example.go4lunchAlx.comparators;

import com.example.go4lunchAlx.models.Restaurant;

import java.util.Comparator;

public class SortByAttendants implements Comparator<Restaurant> {

    @Override
    public int compare(Restaurant r1, Restaurant r2) {

        return (int) (r2.getAttendants().size() - r1.getAttendants().size());
    }
}
