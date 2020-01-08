package com.example.go4lunchAlx.comparators;

import com.example.go4lunchAlx.service.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;

import java.util.Comparator;

//comparator for sorting by rate

public class SortByRate implements Comparator<Restaurant> {

    private RestApiService service = DI.getRestApiService();

    @Override
    public int compare(Restaurant r1, Restaurant r2) {
        int result = (int) ((service.getRate(r2)*1000) - (service.getRate(r1)*1000));

        return result;
    }
}
