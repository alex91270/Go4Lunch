package com.example.go4lunchAlx.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.data.GetDatas;
import com.example.go4lunchAlx.data.restaurant_search.OnSearchedRestaurantAdded;
import com.example.go4lunchAlx.ui.detail.DetailActivity;
import com.example.go4lunchAlx.service.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.ui.FragmentSearchViewAutocomplete;
import com.example.go4lunchAlx.data.viewmodel.DataViewModel;
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
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MapViewFragment extends FragmentSearchViewAutocomplete implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, EasyPermissions.PermissionCallbacks {
    private Context mContext;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FusedLocationProviderClient mFusedLocation;
    private RestApiService service = DI.getRestApiService();
    private View mapView;
    private static String apiKey;
    private GetDatas getDatas;
    private LatLng selectedLocation;
    private OnSearchedRestaurantAdded onSearchedRestaurantAdded;
    private InputMethodManager imm;
    private boolean afterResearch;
    private final int RC_LOCATION_PERM = 123;

    public static MapViewFragment newInstance() {
        return (new MapViewFragment());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        afterResearch = false;
        //callback triggered when a new place has been added through a research
        onSearchedRestaurantAdded = new OnSearchedRestaurantAdded() {
            @Override
            public void onRestaurantAdded() {
                afterResearch = true;
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                placeMarkers();
            }
        };

        mContext = this.getActivity();
        apiKey = mContext.getString(R.string.google_maps_key);
        //initialize Places with the API key provided
        if (!Places.isInitialized()) {
            Places.initialize(mContext, apiKey);
        }
        DataViewModel dataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        //set up the viewModel to observe any change that occurred to the restaurants list
        dataViewModel.getRestoList().observe(this, new Observer<List<Restaurant>>() {
            public void onChanged(@Nullable List<Restaurant> restos) {
               if (service.getAllRestaurants().size() > 0) placeMarkers();
            }
        });
        getDatas = new GetDatas(dataViewModel);
        View root = inflater.inflate(R.layout.fragment_map_view, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //checks and manages location permission
        if (hasLocationPermission()) {
            getMap();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location), RC_LOCATION_PERM, Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        super.setSuggestionsListener(onSearchedRestaurantAdded);
    }


    @SuppressWarnings({"MissingPermission"})
    private void getMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_widget);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //when the location changes, the data retrieval is started
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                service.setCurrentLocation(userLocation);
                selectedLocation = userLocation;
                getDatas.process(mContext, userLocation, apiKey);
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
                //the location button centers the map and starts a new data retrieval
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

        mFusedLocation = LocationServices.getFusedLocationProviderClient(mContext);
        Task<Location> task = mFusedLocation.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when getting back the last known location, a data retrieval is started
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                service.setCurrentLocation(userLocation);
                selectedLocation = userLocation;
                getDatas.process(mContext, userLocation, apiKey);
            }
        });
    }

    private void placeMarkers() {

        if (mMap != null) {
            mMap.clear();


        int maxDistance = 0;

        for(Restaurant r : service.getAllRestaurants()) {

            if(r.getDistance()>maxDistance) maxDistance = r.getDistance();

            //a green marker is created by place, red if at least one attendant
            if (r.getAttendants().size() > 0) {
                mMap.addMarker(new MarkerOptions().position(r.getLocation()).title(r.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_red_s)));
            } else {
                if (r.getLocation() != null && mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(r.getLocation()).title(r.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_green_s)));
                }
            }
        }
        //the zoom value is adjusted so that all places fit on the screen
        int zoomValue = 24-((int)(Math.log(maxDistance) / Math.log(2)));
        if (afterResearch) {
            afterResearch = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(service.getAllRestaurants()
                    .get(service.getAllRestaurants().size() - 1).getLocation(), 20));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, zoomValue)); //between 1 and 20
        }
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("restoId", service.getRestaurantIdByName(marker.getTitle()));
        mContext.startActivity(intent);

        return false;
    }

    private Boolean hasLocationPermission() {
        return EasyPermissions
                .hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        getMap();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(mContext, getString(R.string.perm_denied), Toast.LENGTH_LONG).show();
    }

}