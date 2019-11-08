package com.example.go4lunchAlx.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunchAlx.data.firebase_data.GetFirebaseData;
import com.example.go4lunchAlx.data.firebase_data.OnFirebaseDataReadyCallback;
import com.example.go4lunchAlx.data.nearby_places.GetNearbyPlaces;
import com.example.go4lunchAlx.data.nearby_places.OnNearbyPlacesReadyCallback;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.viewmodel.DataViewModel;
import com.facebook.all.All;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GetDatas {

    private RestApiService service = DI.getRestApiService();
    SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yy");
    private PlacesClient mPlacesClient;
    private List<Restaurant> allRestaurantsWithInfo = new ArrayList<>();
    private Context mContext;
    private DataViewModel dataViewModel;
    private List<Restaurant> AllRestaurantsWithIdOnly;

    public GetDatas(DataViewModel dataViewModel){
        this.dataViewModel = dataViewModel;
    }

    public void process(Context mContext, LatLng location, String apiKey) {
        this.mContext = mContext;

        service.clearAllRestaurants();

        getNearbyPlacesData(location, apiKey);
    }


    private void getNearbyPlacesData(LatLng location, String apiKey) {

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(new OnNearbyPlacesReadyCallback() {
            @Override
            public void OnNearbyPlacesReady(String result) {
                Log.i("alex", "le download  de place est fini et result: " + result);
                Log.i("alex", "number of restaurants nearby: " + service.getNearbyRestaurants().size());

                getFirebaseData(location);
            }
        });
        getNearbyPlaces.downloadNearbyRestaurants(apiKey, location);
    }


    public void getFirebaseData(LatLng location) {
            GetFirebaseData getFirebaseData = new GetFirebaseData(new OnFirebaseDataReadyCallback() {
                @Override
                public void onFirebaseDataReady(String result) {
                    Log.i("alex", "le download  de firebase est fini et result: " + result);
                    Log.i("alex", "call back after firebase in map fragment");
                    Log.i("alex", "userlist size: " + service.getFirebaseUsers().size());
                    Log.i("alex", "ratinglist size: " + service.getListOfRatings().size());

                    mergeDatas();
                }
            });
            getFirebaseData.downloadDataFromFirebase();

    }

    public void mergeDatas() {

        AllRestaurantsWithIdOnly = new ArrayList<>();

        AllRestaurantsWithIdOnly.addAll(service.getNearbyRestaurants());

        Restaurant selectedRestaurant;
        ArrayList<String> attendants = new ArrayList<>();

        for (User user: service.getFirebaseUsers()) {
            if (user.getDateSelection()!= null && getToday().equals(getDayFromLong(user.getDateSelection()))) {
                Log.i("alex", "today: " + getToday() + " day selection: " + getDayFromLong(user.getDateSelection()));
                if (user.getSelectedRestaurant()!=null) {
                    attendants.add(user.getUid());
                    selectedRestaurant = new Restaurant(user.getSelectedRestaurant(), attendants);
                    if (!AllRestaurantsWithIdOnly.contains(selectedRestaurant)) {
                        AllRestaurantsWithIdOnly.add(selectedRestaurant);
                    } else {
                       service.addAttendantToRestaurant(user.getSelectedRestaurant(), user.getUid());
                    }
                }
            }
        }

        for(Restaurant resto : AllRestaurantsWithIdOnly) {
           fetchRestaurant(resto);
        }



        //service.setAllRestaurantsList(allRestaurantsWithInfo);
        //dataViewModel.updateViewModel();
        //Log.i("alex", "viewmodel of getData is; " + dataViewModel);

    }

    public String getDayFromLong(Long dateLong) {
        Date date = new Date(dateLong);
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(date);
        String simpleDate = ft.format(date);
        return simpleDate;
    }

    public String getToday() {
        Date date = new Date();
        String simpleDate = ft.format(date);
        return simpleDate;
    }

    public  void fetchRestaurant(Restaurant resto) {
        //Log.i("alex", "inside fetch");


        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS, Place.Field.OPENING_HOURS);
        //Log.i("alex", "restoId: " + resto.getId());
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(resto.getId(), placeFields);
        //Log.i("alex", "request: " + request);

        mPlacesClient = Places.createClient(mContext);

        mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

            String name = place.getName();
            String vicinity = place.getAddress();
            LatLng location = place.getLatLng();
            //Log.i("alex", "resto id: " + resto.getId() + "  location: " + location);


            String photoRef = "no_pic";
            if (place.getPhotoMetadatas() != null){
                //photoRef = place.getPhotoMetadatas().get(0);
                String photoString = place.getPhotoMetadatas().get(0).toString();
                photoRef = photoString.substring(
                                photoString.indexOf("photoReference=")+15, photoString.length()-1);

                Log.i("alex", "photo full string is: " + photoString);
                Log.i("alex", "photo splitetd string is: " + photoRef);
            }
            String opening = "No opening hours";
            if (place.getOpeningHours() != null){
                opening = place.getOpeningHours().toString();
            }
            int distance = calculateDistance(service.getCurrentLocation(), location);

            Restaurant restaurantToadd = new Restaurant(resto.getId(), name, photoRef, location,0,vicinity,opening,distance );
            //allRestaurantsWithInfo.add(restaurantToadd);
            Log.i("alex", "adding resto from fetch");


            //***********
            if (!service.getAllRestaurants().contains(restaurantToadd)) {
                service.getAllRestaurants().add(restaurantToadd);
                }
            //service.addRestaurantToAll(restaurantToadd);
            //dataViewModel.updateViewModel();

            if (place.getId().equals(AllRestaurantsWithIdOnly.get(AllRestaurantsWithIdOnly.size()-1).getId())) {
                Log.i("alex", "finished fetching last resto");
                Log.i("alex", "list restaurantwithId size: " + AllRestaurantsWithIdOnly.size());
                Log.i("alex", "list restaurantwithinfo size: " + allRestaurantsWithInfo.size());
                Log.i("alex", "list restaurant of service size: " + service.getAllRestaurants().size());
                dataViewModel.updateViewModel();
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
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
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        //Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
        //        + " Meter   " + meterInDec);

        return (int) Math.floor(Radius * c * 1000);
    }
}
