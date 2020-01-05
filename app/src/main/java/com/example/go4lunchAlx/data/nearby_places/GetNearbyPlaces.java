package com.example.go4lunchAlx.data.nearby_places;

import android.os.StrictMode;
import android.util.Log;
import com.example.go4lunchAlx.service.di.DI;
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

public class GetNearbyPlaces {

    private OnNearbyPlacesReadyCallback onNearbyPlacesReadyCallback;
    private static String result;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListRest";
    private RestApiService service = DI.getRestApiService();
    private LatLng location;

    public GetNearbyPlaces(OnNearbyPlacesReadyCallback onNearbyPlacesReadyCallback) {
        this.onNearbyPlacesReadyCallback = onNearbyPlacesReadyCallback;
    }

    public void downloadNearbyRestaurants(String API_KEY, LatLng loc) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        result = "success";
        location = loc;
        Double lat = location.latitude;
        Double lng = location.longitude;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
            sb.append("&type=restaurant");
            sb.append("&rankby=distance");
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
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
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            for (int i = 0; i < predsJsonArray.length(); i++) {
                JSONObject jsonObject = predsJsonArray.getJSONObject(i);
                Restaurant resto = buildRestaurantFromJson(jsonObject);
                service.addRestaurant(resto);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
            result = "Error processing JSON result";
        }

        onNearbyPlacesReadyCallback.OnNearbyPlacesReady(result);
    }

    private Restaurant buildRestaurantFromJson(JSONObject jsonObject) {

        try {
            String reference = jsonObject.getString("reference");
            return new Restaurant(reference);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error processing JSON results", e);
            result = "Error extracting JSON restaurant";
        }

        return null;
    }
}



