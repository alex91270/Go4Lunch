package com.example.go4lunchAlx.data.firebase_data;

import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunchAlx.di.DI;
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

    public GetFirebaseData(OnFirebaseDataReadyCallback onFirebaseDataReadyCallback) {
        this.onFirebaseDataReadyCallback = onFirebaseDataReadyCallback;
    }

    public void downloadDataFromFirebase() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        result = "success";

       downloadUsers();
    }

    public void downloadUsers() {
        CollectionReference userRef = FirebaseFirestore.getInstance().collection("users");

        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    service.clearFirebaseUsers();
                    for (DocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        //Log.i("alex", "user: " + user.getUid() + "  " + user.getUsername());
                        //for (User u : service.getFirebaseUsers()) {
                            //Log.i("alex", "same ?: " + user.equals(u));
                        //}
                        if (!service.getFirebaseUsers().contains(user)) {
                            service.addFirebaseUser(user);
                            //Log.i("alex", "One user added. user list size: " + service.getFirebaseUsers().size());
                        }

                    }

                    downloadRatings();

                } else {
                    Log.d("alex", "failure getting users");
                    result = "failure getting users";
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
                        //Log.i("alex", "rating: " + rating.getRestaurantID());
                        if (!service.getListOfRatings().contains(rating)){
                            service.addRating(rating);
                        }
                    }

                    result = "success";
                    onFirebaseDataReadyCallback.onFirebaseDataReady(result);


                } else {
                    Log.d("alex", "failure getting ratings");
                    result = "failure getting ratings";
                    onFirebaseDataReadyCallback.onFirebaseDataReady(result);
                }
            }
        });
    }
}



