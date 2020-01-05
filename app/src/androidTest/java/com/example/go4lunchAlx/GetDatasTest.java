package com.example.go4lunchAlx;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;
import com.example.go4lunchAlx.data.firebase_data.GetFirebaseData;
import com.example.go4lunchAlx.data.firebase_data.OnFirebaseDataReadyCallback;
import com.example.go4lunchAlx.data.nearby_places.GetNearbyPlaces;
import com.example.go4lunchAlx.data.nearby_places.OnNearbyPlacesReadyCallback;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.service.di.DI;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class GetDatasTest {
    private RestApiService service = DI.getRestApiService();
    private static String apiKey;
    private CountDownLatch latch;
    private LatLng location;
    private Context mContext;


    @Before
    public void setup(){
        mContext= getInstrumentation().getTargetContext();
        apiKey = mContext.getString(R.string.google_api_key);
        location = new LatLng(48.8603735,2.3363348);
        service.setCurrentLocation(location);
    }

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void getFirebaseDataTest() throws InterruptedException {

        latch = new CountDownLatch(1);

        GetFirebaseData getFirebaseData = new GetFirebaseData(new OnFirebaseDataReadyCallback() {
            @Override
            public void onFirebaseDataReady(String result) {
                assertTrue(service.getFirebaseUsers().size() > 0);
                latch.countDown();
            }
        });
        getFirebaseData.downloadDataFromFirebase();

        latch.await();
    }

    @Test
    public void getNearbyPlacesTest() throws InterruptedException {

        latch = new CountDownLatch(1);

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(new OnNearbyPlacesReadyCallback() {
            @Override
            public void OnNearbyPlacesReady(String result) {
                assertTrue(service.getRestaurants().size() > 0);
                latch.countDown();
            }
        });
        getNearbyPlaces.downloadNearbyRestaurants(apiKey, location);

        latch.await();
    }

}
