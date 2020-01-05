package com.example.go4lunchAlx.helpers;

import com.example.go4lunchAlx.models.Rating;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RatingsHelper {

    private static final String COLLECTION_NAME = "ratings";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRatingsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createRating(String rID, String userID, String restaurantID, int rate) {
        // 1 - Create Obj
        Rating ratingToCreate = new Rating(rID, userID, restaurantID, rate);

        return RatingsHelper.getRatingsCollection().document(rID).set(ratingToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRating(String rID){
        return RatingsHelper.getRatingsCollection().document(rID).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateRate(int rate, String rID) {
        return RatingsHelper.getRatingsCollection().document(rID).update("rate", rate);
    }

}
