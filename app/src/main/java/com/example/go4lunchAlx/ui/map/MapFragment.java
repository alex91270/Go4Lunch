package com.example.go4lunchAlx.ui.map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.main.MainActivity;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.nearby_places.GetNearbyPlaces;
import com.example.go4lunchAlx.nearby_places.OnNearbyPlacesReadyCallback;
import com.example.go4lunchAlx.service.RestApiService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.opencensus.internal.StringUtil;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private Context mContext;
    private PlacesClient mPlacesClient;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FusedLocationProviderClient mFusedLocation;
    private RestApiService service = DI.getRestApiService();
    private View mapView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("alex", "on create view");
        mContext = this.getActivity();

        if (!Places.isInitialized()) {
            Places.initialize(mContext, "AIzaSyAhwPQxQ6UU4V7VQ7IxYsvFa3WzoNJ2qDg");
        }
        Log.i("alex", "Places initialized: " + String.valueOf(Places.isInitialized()));

        mPlacesClient = Places.createClient(mContext);

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        LatLng loc1 = new LatLng(48, 2);
        //service.setCurrentLocation(loc1);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("alex", "on view created");
        getMap();
    }



    @SuppressWarnings({"MissingPermission"})
    public void getMap() {

        Log.i("alex", "get map");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_widget);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("alex", "location changed: " + location.toString());
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                service.setCurrentLocation(userLocation);

                centerMapOnLocation(userLocation);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates("gps", 1000, 100, locationListener);
    }

    public void centerMapOnLocation(LatLng userLocation) {

        if (userLocation != null) {
            Log.i("alex", "centering location");
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title("me").icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location_s)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12)); //between 1 and 20



            GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(new OnNearbyPlacesReadyCallback() {
                @Override
                public void OnNearbyPlacesReady(String result) {
                    Log.i("alex", "le download est fini et result: " + result);
                    Log.i("alex", "number of restaurants: " + service.getAllRestaurants().size());
                    int maxDistance = 0;
                    for(Restaurant r : service.getAllRestaurants()) {
                        if(r.getDistance()>maxDistance) maxDistance = r.getDistance();
                        Log.i("alex", "add marker: " + r.getName() + " " + r.getDistance());
                        mMap.addMarker(new MarkerOptions().position(r.getLocation()).title(r.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_green_s)));
                    }
                    int zoomValue = 24-((int)(Math.log(maxDistance) / Math.log(2)));
                    Log.i("alex", "max distance: " + maxDistance);
                    Log.i("alex", "zoom value: " + zoomValue);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(service.getCurrentLocation(), zoomValue)); //between 1 and 20
                }
            });
            getNearbyPlaces.downloadNearbyRestaurants();


        } else {
            Log.i("alex", "no location");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i("alex", "Map ready ");

        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(mContext, R.raw.mapstyle_retro);
        mMap.setMapStyle(style);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            Log.i("alex", "in the if");
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 100);
            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            //layoutParams.setMargins(0, 0, 180, 180);
        }



        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_widget);

        //Log.i("alex", "voila le complete: " + autocompleteFragment.toString());

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setHint("Search restaurants");
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Toast.makeText(mContext, "Place: " + place.getName() + ", " + place.getId(), Toast.LENGTH_LONG).show();
                LatLng userLocation = place.getLatLng();
                Log.i("alex", "latlng selected: " + userLocation.toString());
                centerMapOnLocation(userLocation);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });




        mFusedLocation = LocationServices.getFusedLocationProviderClient(mContext);
        Task<Location> task = mFusedLocation.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Log.i("alex", "last location retrieved");
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i("alex", "last location retrieved");
                Log.i("alex", userLocation.toString());
                service.setCurrentLocation(userLocation);
                centerMapOnLocation(userLocation);
            }
        });
    }





}