package com.example.go4lunchAlx.comparators;

import android.util.Log;

import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;

import java.util.Comparator;

public class SortByRate implements Comparator<Restaurant> {

    private RestApiService service = DI.getRestApiService();

    @Override
    public int compare(Restaurant r1, Restaurant r2) {
        int result = (int) ((service.getRate(r2)*1000) - (service.getRate(r1)*1000));

        return result;
    }
}
