package com.example.go4lunchAlx.api;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.go4lunchAlx.models.User;

import java.util.Date;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        // 1 - Create Obj
        User userToCreate = new User(uid, username, urlPicture, "restaurant", 1);
        Log.i("alex", "userhelper create user");
        Log.i("alex", "new user selected rest: " + userToCreate.getSelectedRestaurant());

        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

    // --- UPDATE ---

    public static Task<Void> updateSelectedRestaurant(String uid, String selectedRestaurant) {
        return UserHelper.getUsersCollection().document(uid).update
                ("selectedRestaurant", selectedRestaurant,
                        "dateSelection", new Date().getTime());
    }

}
