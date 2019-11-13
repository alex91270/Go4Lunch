package com.example.go4lunchAlx.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel providing liveData of the restaurant's list from the service.
 */

public class DataViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> restoList;
    private RestApiService service = DI.getRestApiService();

    public DataViewModel() {
       restoList = new MutableLiveData<>();
        restoList.setValue(service.getRestaurants());
    }

    public LiveData<List<Restaurant>> getRestoList() {
        return restoList;
    }


    public void updateViewModel() {
        restoList.setValue(service.getRestaurants());
        Log.i("alex", "updateViewModel");
    }
}




