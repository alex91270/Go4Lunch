
# Go4Lunch

This application's purpose is to be used by th
e coworkers of a same company to help them choose where and who to lunch with.
The app provides the user a list of restaurants found nearby, then he can consult the details of each place, and pick one for lunch, or see his workmates selection to join some.

# Files

All the source files can be downloaded or cloned from GitHub, at [**[[https://github.com/alex91270/Go4Lunch](https://github.com/alex91270/Go4Lunch)]**

## Compiling and running
In order to run the app, you need AndroidStudio.
- If you downloaded the sources package, then click on File/Open, then select the directory where you saved it.
- Otherwise, you can click on: File/New/Project from version control/Git, then, fill in the Url of the package to clone it straight forward.
	> To run the app, you can either use an emulator, or a real Android device connected in USB (Device with API 19 minimum).

-  To properly run the application, many dependencies are required in your gradle file, necessary to use GoogleMaps, GooglePlaces, Firebase authentication, database, firestore, and Facebook authentication. Some releases of those dependencies may cause conflicts. The version below works fine, so you can put it this way:
   > // MAPS  
implementation 'com.google.android.gms:play-services-maps:17.0.0'  
implementation 'com.google.android.gms:play-services-location:17.0.0'  
  
   > //PLACES  
implementation 'com.google.android.libraries.places:places:2.0.0'  
  
   > // FIREBASE  
implementation "com.google.firebase:firebase-core:17.2.0"  
implementation 'com.google.firebase:firebase-messaging:20.0.0'  
implementation 'com.google.firebase:firebase-database:19.1.0'  
implementation 'com.google.firebase:firebase-auth:19.1.0'  
implementation 'com.google.android.gms:play-services-auth:17.0.0'  
  
   > // FIREBASE-UI  
implementation 'com.firebaseui:firebase-ui-storage:4.3.2'  
implementation 'com.firebaseui:firebase-ui-auth:4.3.2'  
implementation 'com.firebaseui:firebase-ui-firestore:4.3.2'  
  
   > // FACEBOOK LOGIN SUPPORT  
implementation 'com.facebook.android:facebook-android-sdk:[4,5)'
}


## Pull requests

No pull request desired. The evolutions of the app are managed by it's genuine dev team.