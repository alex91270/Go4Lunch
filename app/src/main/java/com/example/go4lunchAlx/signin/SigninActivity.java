package com.example.go4lunchAlx.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.main.MainActivity;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.go4lunchAlx.helpers.UserHelper;
import java.util.Arrays;
import java.util.List;

public class SigninActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private ConstraintLayout constraintLayout;
    private RestApiService service = DI.getRestApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        constraintLayout = findViewById(R.id.signin_constraint_layout);

        //startActivity(new Intent(this, MainActivity.class));
        startSignInActivity();
    }

    private void startSignInActivity(){ //METHOD FOR FIREBASE-UI 4.3.2
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                showSnackBar(this.constraintLayout, getString(R.string.connection_succeed));
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            else { // ERROR LOGGING
                showSnackBar(this.constraintLayout, getString(R.string.connection_failed));
                startSignInActivity();
            }
        }
    }

    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();

            service.setCurrentUser(new User(uid, username, urlPicture));
            service.setCurrentUserId(uid);
            service.setMyEmailAddress(this.getCurrentUser().getEmail());
            UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void showSnackBar(ConstraintLayout constraintLayout, String message){
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
}
