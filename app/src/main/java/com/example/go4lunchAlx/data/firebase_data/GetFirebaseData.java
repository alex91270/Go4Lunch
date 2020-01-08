package com.example.go4lunchAlx.data.firebase_data;

import android.content.res.Resources;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.helpers.ResultCode;
import com.example.go4lunchAlx.service.di.DI;
import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class GetFirebaseData {

    private OnFirebaseDataReadyCallback onFirebaseDataReadyCallback;
    private static String result;
    private RestApiService service = DI.getRestApiService();
    private static final String LOG_TAG = "Go4Lunch";

    //constructor with the callback as parameter
    public GetFirebaseData(OnFirebaseDataReadyCallback onFirebaseDataReadyCallback) {
        this.onFirebaseDataReadyCallback = onFirebaseDataReadyCallback;
    }

    public void downloadDataFromFirebase() {
        //Disables the detection of everything
       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);

       //if no issue, the result is gonna be "success
       result = "success";

       downloadUsers();
    }

    private void downloadUsers() {
        //get back collection users from Firebase
        CollectionReference userRef = FirebaseFirestore.getInstance().collection("users");

        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    service.clearFirebaseUsers();
                    for (DocumentSnapshot document : task.getResult()) {
                        //turn items into User objects and add them to service list
                        User user = document.toObject(User.class);

                        if (!service.getFirebaseUsers().contains(user)) {
                            service.addFirebaseUser(user);
                        }
                    }

                    downloadRatings();

                } else {
                    //if failed, call the callback with error message
                    result = Resources.getSystem().getString(R.string.failure_users);
                    Log.e(LOG_TAG, "Failed getting users");
                    onFirebaseDataReadyCallback.onFirebaseDataReady(result);
                }
            }
        });
    }

    public void downloadRatings() {
        CollectionReference ratingRef = FirebaseFirestore.getInstance().collection("ratings");

        ratingRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    service.clearListOfRatings();
                    for (DocumentSnapshot document : task.getResult()) {
                        Rating rating = document.toObject(Rating.class);
                        if (!service.getListOfRatings().contains(rating)){
                            service.addRating(rating);
                        }
                    }

                    result = "success";
                    onFirebaseDataReadyCallback.onFirebaseDataReady(result);

                } else {
                    result = Resources.getSystem().getString(R.string.failure_ratings);
                    onFirebaseDataReadyCallback.onFirebaseDataReady(result);
                }
            }
        });
    }
}



