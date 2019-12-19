package com.example.go4lunchAlx.data;

import android.app.Activity;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.viewmodel.DataViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class GetDatasTest {

    private RestApiService service = DI.getRestApiService();
    SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yy");
    private PlacesClient mPlacesClient;
    private Context mContext;
    private DataViewModel dataViewModel;
    private Calendar calendar;
    private Date date;

    @Before
    void setup() {
        mContext = InstrumentationRegistry.getInstrumentation().getContext();
        LatLng location = new LatLng(222,222);
    }


    @Test
    public void process() {
    }
}