package com.example.go4lunchAlx.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunchAlx.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private String mId;
    private String mName;
    private String mPhone;
    private Uri mWebsite;
    private String mAddress;
    private String mPicture;
    private PlacesClient mPlacesClient;
    private final int PERMISSION_REQUEST_CALL = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        mId = getIntent().getStringExtra("id");
        mPicture = getIntent().getStringExtra("pictureRef");
        mPlacesClient = Places.createClient(this);
        Log.i("alex", "detail oncreate");

        fetchRestaurant();
    }

    public void fetchRestaurant() {
        Log.i("alex", "inside fetch");
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(mId, placeFields);

        mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            mName = place.getName();
            Log.i("alex", "get back name");
            mAddress = place.getAddress();
            mPhone = place.getPhoneNumber();
            mWebsite = place.getWebsiteUri();
            Log.i("alex", "Place found: " + mPhone);

            assignValues();

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e("alex", "Place not found: " + exception.getMessage());
            }
        });
    }

    public void assignValues() {
        TextView textViewName = findViewById(R.id.restaurant_name);
        textViewName.setText(mName);
        Log.i("alex", "assign textviewname");
        TextView textViewAddress = findViewById(R.id.restaurant_address);
        textViewAddress.setText(mAddress);
        ImageView mRestaurantPhoto = findViewById(R.id.restaurant_picture);
        Log.i("alex", "picture is: " + mPicture);

        if (mPicture.equals("no_pic")) {
            mRestaurantPhoto.setImageResource(R.drawable.resto_sign300);
            Log.i("alex", "set up resto sign 300 ");

        } else {
            Log.i("alex", "glide ");
            String apiKey = this.getString(R.string.google_maps_key);
            String photoURL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + mPicture + "&sensor=false&maxheight=300&maxwidth=300&key=" + apiKey;

            Glide.with(this)
                    .load(photoURL)
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestaurantPhoto);
        }
    }

    public void callNumberIfPermitted(View view){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CALL);
        } else {
            dialPhoneNumber();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialPhoneNumber();
                } else {
                   Toast.makeText(this, "Permission denied, no phone call...", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void dialPhoneNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mPhone));
        startActivity(callIntent);
    }

    public void visitWebSite(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, mWebsite);
        startActivity(browserIntent);
    }
}
