package com.example.go4lunchAlx.nearby_places;

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
import java.util.ArrayList;

public class GetNearbyPlaces {

    private OnNearbyPlacesReadyCallback onNearbyPlacesReadyCallback;
    private static String result;

    private ListView mListView;
    private static final String API_KEY = "AIzaSyAhwPQxQ6UU4V7VQ7IxYsvFa3WzoNJ2qDg";

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

    public void downloadNearbyRestaurants() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
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

                String latitude = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                String longitude = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                LatLng location = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                String reference = predsJsonArray.getJSONObject(i).getString("reference");
                String name = predsJsonArray.getJSONObject(i).getString("name");
                //String rating = predsJsonArray.getJSONObject(i).getString("rating");
                String vicinity = predsJsonArray.getJSONObject(i).getString("vicinity");
                //String opening = predsJsonArray.getJSONObject(i).getString("opening_hours");

                String rating = "no rating";
                if (!predsJsonArray.getJSONObject(i).isNull("rating")) {
                    rating = predsJsonArray.getJSONObject(i).getString("rating");
                }

                String opening = "no opening hours";
                if (!predsJsonArray.getJSONObject(i).isNull("opening_hours")) {
                    opening = predsJsonArray.getJSONObject(i).getString("opening_hours");
                }



                Restaurant restaurant = new Restaurant(reference, name, "url", location, rating, vicinity, opening);
                Log.i("alex", name + " " + opening);

                service.addNearbyRestaurant(restaurant);
            }
            Log.i("alex", "list size: " + String.valueOf(service.getAllRestaurants().size()));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }


        result = "mon resultat";
        onNearbyPlacesReadyCallback.OnNearbyPlacesReady(result);
    }
}



