package com.example.go4lunchAlx;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Unused {

    private Context mContext;
    private PlacesClient mPlacesClient;

    public void nearby() {

        //only 20 results
        //can't define type
        //can't define center neither radius

        // Use fields to define the data types to return.
        //List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ID, Place.Field.TYPES);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(mContext, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    FindCurrentPlaceResponse response = task.getResult();

                    Log.i("alex", "list size: " + String.valueOf(response.getPlaceLikelihoods().size()));

                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        //Log.i("alex", String.format("Place '%s' has likelihood: %f",
                        //      placeLikelihood.getPlace().getName(),
                        //      placeLikelihood.getLikelihood()));
                        Log.i("alex", "name: " + placeLikelihood.getPlace().getName());

                    }

                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;

                        Log.i("alex", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            //getLocationPermission();
        }

    }

    public void nearby2() {

        //Can't specify restaurant, number of results limited to 5

        LatLng location2 = new LatLng(48.691864, 2.391782);
        LatLng location1 = new LatLng(48.679185, 2.435053);

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        RectangularBounds bounds = RectangularBounds.newInstance(location1,location2);
        FindAutocompletePredictionsRequest request =
                FindAutocompletePredictionsRequest.builder()
                        .setLocationBias(bounds)
                        .setCountry("FR")
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setSessionToken(token)
                        .setQuery("restaurant")
                        .build();

        mPlacesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(
                        (response) -> {
                            Log.i("alex", "number of results in search places response: "
                                    +response.getAutocompletePredictions().size());
                            StringBuilder sb = new StringBuilder();
                            for (AutocompletePrediction prediction :
                                    response.getAutocompletePredictions()) {
                                Log.i("alex", prediction.getPrimaryText(null).toString());
                                sb.append(prediction.getPrimaryText(null).toString());
                                sb.append("\n");
                            }
                            //searchResults.setText(sb.toString());

                        })
                .addOnFailureListener((exception) -> {
                    exception.printStackTrace();
                });
    }

    public void nearby3() {

        List<Place.Field> placeFields = Collections.singletonList(Place.Field.TYPES);

        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(placeFields);
        Task<FindCurrentPlaceResponse> currentPlaceTask =
                mPlacesClient.findCurrentPlace(currentPlaceRequest);

        currentPlaceTask.addOnSuccessListener(
                (response) ->

                        Log.i("alex", response.toString()));
        //responseView.setText(StringUtil.stringify(response, isDisplayRawResultsChecked())));

        currentPlaceTask.addOnFailureListener(
                (exception) -> {
                    exception.printStackTrace();
                    //responseView.setText(exception.getMessage());
                });
    }
}
