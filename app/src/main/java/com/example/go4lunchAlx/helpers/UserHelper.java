package com.example.go4lunchAlx.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        // 1 - Create Obj

        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("username", username);
        data.put("urlPicture", urlPicture);

        return UserHelper.getUsersCollection().document(uid)
                .set(data, SetOptions.merge());

    }

    // --- UPDATE ---

    public static Task<Void> updateSelectedRestaurant(String uid, String selectedRestaurant) {
        return UserHelper.getUsersCollection().document(uid).update
                ("selectedRestaurant", selectedRestaurant,
                        "dateSelection", new Date().getTime());
    }

}
