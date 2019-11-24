package com.example.go4lunchAlx.ui.map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.go4lunchAlx.R;

import com.example.go4lunchAlx.data.GetDatas;
import com.example.go4lunchAlx.detail.DetailActivity;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.data.firebase_data.GetFirebaseData;
import com.example.go4lunchAlx.data.firebase_data.OnFirebaseDataReadyCallback;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.data.nearby_places.GetNearbyPlaces;
import com.example.go4lunchAlx.data.nearby_places.OnNearbyPlacesReadyCallback;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.viewmodel.DataViewModel;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Context mContext;
    //private PlacesClient mPlacesClient;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FusedLocationProviderClient mFusedLocation;
    private RestApiService service = DI.getRestApiService();
    private View mapView;
    private static String apiKey;
    private GetDatas getDatas;
    private LatLng selectedLocation;

    public static MapViewFragment newInstance() {
        return (new MapViewFragment());
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = this.getActivity();
        apiKey = mContext.getString(R.string.google_maps_key);

        if (!Places.isInitialized()) {
            Places.initialize(mContext, apiKey);
        }
        DataViewModel dataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        //Log.i("alex", "viewmodel of fragment is; " + dataViewModel);
        dataViewModel.getRestoList().observe(this, new Observer<List<Restaurant>>() {
            public void onChanged(@Nullable List<Restaurant> restos) {
                //Log.i("alex", "change observed, list size: " + service.getAllRestaurants().size());
                if (service.getRestaurants().size() > 0) placeMarkers();
            }
        });
        getDatas = new GetDatas(dataViewModel);
        View root = inflater.inflate(R.layout.fragment_map_view, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.i("alex", "map view on resume");
        if (service.getRestaurants().size() > 0) placeMarkers();
    }


    @SuppressWarnings({"MissingPermission"})
    public void getMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_widget);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i("alex", "location changed: " + location.toString());
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                service.setCurrentLocation(userLocation);
                selectedLocation = userLocation;

                getDatas.process(mContext, userLocation, apiKey);
                //centerMapOnLocation(userLocation);
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
        locationManager.requestLocationUpdates("gps", 1000, 50, locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(mContext, R.raw.mapstyle_retro);
        mMap.setMapStyle(style);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMarkerClickListener(this);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getDatas.process(mContext, service.getCurrentLocation(), apiKey);
                return true;
            }
        });

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 50, 150);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_widget);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setHint("Search restaurants");
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Toast.makeText(mContext, "Place: " + place.getName() + ", " + place.getId(), Toast.LENGTH_LONG).show();
                LatLng userLocation = place.getLatLng();
                //Log.i("alex", "latlng selected: " + userLocation.toString());
                selectedLocation = userLocation;
                getDatas.process(mContext, userLocation, apiKey);


             /**
                service.addRestaurant(new Restaurant(place.getId()));
                getDatas.fetchRestaurantInfos(place.getId());
              */
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

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //Log.i("alex", "last location retrieved");
                //Log.i("alex", userLocation.toString());
                service.setCurrentLocation(userLocation);
                selectedLocation = userLocation;
                getDatas.process(mContext, userLocation, apiKey);
            }
        });
    }

    public void placeMarkers() {

        //Log.i("alex", "place markers");

        if (mMap != null) {
            mMap.clear();
        }


        int maxDistance = 0;
        for(Restaurant r : service.getRestaurants()) {
            if(r.getDistance()>maxDistance) maxDistance = r.getDistance();

            if (r.getAttendants().size() > 0) {
                mMap.addMarker(new MarkerOptions().position(r.getLocation()).title(r.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_red_s)));
            } else {
                if (r.getLocation() != null) {
                    mMap.addMarker(new MarkerOptions().position(r.getLocation()).title(r.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_green_s)));
                }
            }
        }
        int zoomValue = 24-((int)(Math.log(maxDistance) / Math.log(2)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, zoomValue)); //between 1 and 20
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

       //Log.i("alex", "marker clicked: " + marker.getTitle());

        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("restoId", service.getRestaurantIdByName(marker.getTitle()));
        mContext.startActivity(intent);

        return false;
    }
}