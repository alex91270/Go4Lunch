package com.example.go4lunchAlx.data.restaurant_search;

import android.content.Context;
import com.example.go4lunchAlx.helpers.DistanceHelper;
import com.example.go4lunchAlx.service.di.DI;
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
    private DistanceHelper distanceHelper = new DistanceHelper();

//builds a Restaurant found via a research by the SearchView and adds it to the service
    public RestaurantSearchedAdding(OnSearchedRestaurantAdded onSearchedRestaurantAdded) {
        this.onSearchedRestaurantAdded = onSearchedRestaurantAdded;
        calendar = Calendar.getInstance();
        date = calendar.getTime();
    }

    public void addSearchedRestaurantToList(Context mContext, String restoId) {
        this.mContext = mContext;
        mPlacesClient = Places.createClient(mContext);
        service.addRestaurantToSearched(new Restaurant(restoId));

        //adds restaurant to service, with it's ID only
        Restaurant resto = service.getRestaurantById(restoId);

        //enrich restaurants details with a fetchPlace request
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS, Place.Field.OPENING_HOURS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(resto.getId(), placeFields);

        mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

            resto.setName(place.getName());
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
                opening = new OpeningHelper().getOpeningString(mContext, listPeriods, date);
            }
            resto.setOpening(opening);

            int distance = distanceHelper.calculateDistance(service.getCurrentLocation(), location);
            resto.setDistance(distance);

            if (resto.getAttendants()==null) resto.setAttendants(new ArrayList<>());

            resto.setPhoneNumber(place.getPhoneNumber());
            resto.setWebsite(place.getWebsiteUri());

            service.updateSearchedRestaurant(resto);

            onSearchedRestaurantAdded.onRestaurantAdded();

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
            }
        });
    }
}
