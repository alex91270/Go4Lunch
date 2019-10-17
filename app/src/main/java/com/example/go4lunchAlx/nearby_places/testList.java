package com.example.go4lunchAlx.nearby_places;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.go4lunchAlx.R;

public class testList extends AppCompatActivity {


    private ListView mListView;
    private static final String API_KEY = "AIzaSyAhwPQxQ6UU4V7VQ7IxYsvFa3WzoNJ2qDg";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListRest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        //Intent intent = getIntent();
        //String longitude = intent.getStringExtra("long");
        //String latitude = intent.getStringExtra("lat");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Double lng = Double.parseDouble(longitude);
        //Double lat = Double.parseDouble(latitude);
        Double lat = 48.8404;
        Double lng = 2.32419;

        int radius = 100;

        ArrayList<Place> list = search(lat, lng, radius);

        if (list != null)
        {
            mListView = (ListView) findViewById(R.id.listview);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list);
            mListView.setAdapter(adapter);
        }
    }

    public static ArrayList<Place> search(double lat, double lng, int radius) {
        ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
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
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
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
            resultList = new ArrayList<Place>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                Place place = new Place();
                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
                place.name = predsJsonArray.getJSONObject(i).getString("name");
                Log.i("alex", place.name);
                resultList.add(place);
            }
            Log.i("alex", String.valueOf(resultList.size()));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
    }


    //Value Object for the ArrayList
    public static class Place {
        private String reference;
        private String name;

        public Place(){
            super();
        }
        @Override
        public String toString(){
            return this.name; //This is what returns the name of each restaurant for array list
        }
    }
}
