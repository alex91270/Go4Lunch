package com.example.go4lunchAlx.nearby_places;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GetNearbyPlaces {

    private OnNearbyPlacesReadyCallback onNearbyPlacesReadyCallback;
    private static String result;

    private ListView mListView;
    //private static final String API_KEY = "AIzaSyAhwPQxQ6UU4V7VQ7IxYsvFa3WzoNJ2qDg";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    //private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    //private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListRest";
    private RestApiService service = DI.getRestApiService();

    public GetNearbyPlaces(OnNearbyPlacesReadyCallback onNearbyPlacesReadyCallback) {
        this.onNearbyPlacesReadyCallback = onNearbyPlacesReadyCallback;
    }

    public void downloadNearbyRestaurants(String API_KEY) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        result = "success";

        //LatLng loc1 = new LatLng(48, 2);
        //service.setCurrentLocation(loc1);
        LatLng latLng = service.getCurrentLocation();

        Log.i("alex", "loc: " + latLng.toString());


        Double lat = service.getCurrentLocation().latitude;
        Double lng = service.getCurrentLocation().longitude;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
            //sb.append("location=" + latLng.toString());
            //sb.append("&radius=" + String.valueOf(radius));
            sb.append("&type=restaurant");
            sb.append("&rankby=distance");
            sb.append("&key=" + API_KEY);
            Log.i("alex", sb.toString());


            URL url = new URL(sb.toString());
            Log.i("alex", "opening connection");
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                Log.i("alex", "reading buffer");
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            result = "Error processing Places API URL";
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            result = "Error connecting to Places API";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            Log.i("alex", "creating JSON");
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.i("alex", "JSON got: " + jsonObj);
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the descriptions from the results
            Log.i("alex", "extracting results");
            //resultList = new ArrayList<Place>(predsJsonArray.length());

            service.clearNearbyRestaurants();

            for (int i = 0; i < predsJsonArray.length(); i++) {

                JSONObject jsonObject = predsJsonArray.getJSONObject(i);
                Restaurant resto = buildRestaurantFromJson(jsonObject);
                service.addNearbyRestaurant(resto);
            }
            Log.i("alex", "list size: " + String.valueOf(service.getAllRestaurants().size()));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
            result = "Error processing JSON result";
        }

        onNearbyPlacesReadyCallback.OnNearbyPlacesReady(result);
    }

    private Restaurant buildRestaurantFromJson(JSONObject jsonObject) {

        try {
            String latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng");
            LatLng location = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            String reference = jsonObject.getString("reference");
            String name = jsonObject.getString("name");
            String vicinity = jsonObject.getString("vicinity");
            int distance = calculateDistance(location, service.getCurrentLocation());
            String photo;

            try{
                photo = jsonObject.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            } catch (JSONException e) {
                Log.i("alex", "no photo array ");
                photo = "no_pic";
            }
            //JSONObject photo1 = photosArray.getJSONObject(0);
            //String photo = photo1.getString("photo_reference");

            //if (jsonObject.getJSONArray("photos").isNull(0)) {
                //Log.i("alex", "no photo array ");
            //}
                //String photo = jsonObject.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                //Log.i("alex", "photoref: " + photo);




            double rating = 0;
            if (!jsonObject.isNull("rating")) {
                rating = Double.valueOf(jsonObject.getString("rating"));
            }

            String opening = "No opening hours";
            if (!jsonObject.isNull("opening_hours")) {
                if( jsonObject.getJSONObject("opening_hours").getString("open_now") == "true") {
                    opening = "opened";
                }
                if( jsonObject.getJSONObject("opening_hours").getString("open_now") == "false") {
                    opening = "closed";
                }
        }
            return new Restaurant(reference, name, photo, location, rating, vicinity, opening, distance);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error processing JSON results", e);
            result = "Error extracting JSON restaurant";
        }

        return null;
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
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return (int) Math.floor(Radius * c * 1000);
    }
}



