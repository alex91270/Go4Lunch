package com.example.go4lunchAlx.data.restaurant_search;

import android.content.Context;
import android.util.Log;

import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.helpers.OpeningHelper;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RestaurantSearchedAdding {

    private OnSearchedRestaurantAdded onSearchedRestaurantAdded;
    private static String result;
    private PlacesClient mPlacesClient;
    private Context mContext;
    private RestApiService service = DI.getRestApiService();
    private Date date;
    private Calendar calendar;

    public RestaurantSearchedAdding(OnSearchedRestaurantAdded onSearchedRestaurantAdded) {
        this.onSearchedRestaurantAdded = onSearchedRestaurantAdded;
        calendar = Calendar.getInstance();
        date = calendar.getTime();
    }

    public void addSearchedRestaurantToList(Context mContext, String restoId) {
        this.mContext = mContext;
        mPlacesClient = Places.createClient(mContext);
        service.addRestaurantToSearched(new Restaurant(restoId));
        Log.i("alex", "addSearchedRestaurantToList");
        Log.i("alex", "resto ID: " + restoId);

        Restaurant resto = service.getRestaurantById(restoId);

        Log.i("alex", "resto: " + resto.getId());

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS, Place.Field.OPENING_HOURS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(resto.getId(), placeFields);

        mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

            resto.setName(place.getName());
            Log.i("alex", "resto ID: " + place.getId());
            Log.i("alex", "resto name: " + place.getName());
            resto.setVicinity(place.getAddress());
            LatLng location = place.getLatLng();
            resto.setLocation(place.getLatLng());

            String photoRef = "no_pic";
            if (place.getPhotoMetadatas() != null){
                String photoString = place.getPhotoMetadatas().get(0).toString();
                photoRef = photoString.substring(
                        photoString.indexOf("photoReference=")+15, photoString.length()-1);
            }
            resto.setPhoto(photoRef);

            String opening = "No opening hours";
            if (place.getOpeningHours() != null){
                List<Period> listPeriods = place.getOpeningHours().getPeriods();
                opening = new OpeningHelper().getOpeningString(listPeriods, date);
            }
            resto.setOpening(opening);

            int distance = calculateDistance(service.getCurrentLocation(), location);
            resto.setDistance(distance);

            if (resto.getAttendants()==null) resto.setAttendants(new ArrayList<>());

            service.updateSearchedRestaurant(resto);

            onSearchedRestaurantAdded.onRestaurantAdded();

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                Log.e("alex", "Place not found: " + exception.getMessage());
            }
        });



    }

    public int calculateDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) Math.floor(Radius * c * 1000);
    }
}
