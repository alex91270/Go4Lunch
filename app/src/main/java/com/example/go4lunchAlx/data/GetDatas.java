package com.example.go4lunchAlx.data;

import android.content.Context;
//import android.util.Log;
import com.example.go4lunchAlx.helpers.OpeningHelper;
import com.example.go4lunchAlx.data.firebase_data.GetFirebaseData;
import com.example.go4lunchAlx.data.firebase_data.OnFirebaseDataReadyCallback;
import com.example.go4lunchAlx.data.nearby_places.GetNearbyPlaces;
import com.example.go4lunchAlx.data.nearby_places.OnNearbyPlacesReadyCallback;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.viewmodel.DataViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetDatas {

    private RestApiService service = DI.getRestApiService();
    SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yy");
    private PlacesClient mPlacesClient;
    //private List<Restaurant> allRestaurantsWithInfo = new ArrayList<>();
    private Context mContext;
    private DataViewModel dataViewModel;
    //private List<Restaurant> AllRestaurantsWithIdOnly;
    private Calendar calendar;
    private Date date;

    public GetDatas(DataViewModel dataViewModel){
        this.dataViewModel = dataViewModel;
    }

    public void process(Context mContext, LatLng location, String apiKey) {
        this.mContext = mContext;
        mPlacesClient = Places.createClient(mContext);
        service.clearRestaurants();
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        getNearbyPlacesData(location, apiKey);
    }


    private void getNearbyPlacesData(LatLng location, String apiKey) {

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(new OnNearbyPlacesReadyCallback() {
            @Override
            public void OnNearbyPlacesReady(String result) {
                getFirebaseData();
            }
        });
        getNearbyPlaces.downloadNearbyRestaurants(apiKey, location);
    }


    private void getFirebaseData() {
            GetFirebaseData getFirebaseData = new GetFirebaseData(new OnFirebaseDataReadyCallback() {
                @Override
                public void onFirebaseDataReady(String result) {
                    mergeDatas();
                }
            });
            getFirebaseData.downloadDataFromFirebase();

    }

    private void mergeDatas() {

        Restaurant selectedRestaurant;
        ArrayList<String> attendants = new ArrayList<>();

        for (User user: service.getFirebaseUsers()) {
            attendants.clear();
            if (user.getDateSelection()!= null && getToday().equals(getDayFromLong(user.getDateSelection()))) {

                if (user.getSelectedRestaurant()!= null) {
                    attendants.add(user.getUid());
                    selectedRestaurant = new Restaurant(user.getSelectedRestaurant(), attendants);
                    if (!service.getRestaurants().contains(selectedRestaurant)) {
                        service.addRestaurant(selectedRestaurant);
                    } else {
                        service.addAttendantToRestaurant(user.getSelectedRestaurant(), user.getUid());                    }
                }
            }
        }

        for(Restaurant resto : service.getRestaurants()) {
           fetchRestaurantInfos(resto.getId());
        }
    }

    private void fetchRestaurantInfos(String restoId) {

        Restaurant resto = service.getRestaurantById(restoId);
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

            int distance = calculateDistance(service.getCurrentLocation(), location);
            resto.setDistance(distance);

            if (resto.getAttendants()==null) resto.setAttendants(new ArrayList<>());

            service.updateRestaurant(resto);

            if (place.getId().equals(service.getRestaurants().get(service.getRestaurants().size()-1).getId())) {
                dataViewModel.updateViewModel();
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
            }
        });    }


    private int calculateDistance(LatLng StartP, LatLng EndP) {
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

    private String getDayFromLong(Long dateLong) {
        return ft.format(new Date(dateLong));
    }

    private String getToday() {
        return ft.format(new Date());
    }
}
