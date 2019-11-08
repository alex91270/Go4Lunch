package com.example.go4lunchAlx.detail;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.api.RatingsHelper;
import com.example.go4lunchAlx.api.UserHelper;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class DetailFragment extends Fragment {

    private Restaurant mRestaurant;

    private String mId;
    private String mName;
    private String mPhone;
    private Uri mWebsite;
    private String mAddress;
    private String mPicture;
    private PlacesClient mPlacesClient;
    private final int PERMISSION_REQUEST_CALL = 123;
    private AlertDialog dialog = null;
    private Context context;
    private TextView textViewName;
    private TextView textViewAddress;
    private ImageView mRestaurantPhoto;
    private FloatingActionButton fab;
    private RestApiService service = DI.getRestApiService();
    String restoId;

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restoId = getArguments().getString("restoId");
        mRestaurant = service.getRestaurantById(restoId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Context context = view.getContext();
        mPlacesClient = Places.createClient(context);
        textViewName = view.findViewById(R.id.restaurant_name);
        textViewAddress = view.findViewById(R.id.restaurant_address);
        mRestaurantPhoto = view.findViewById(R.id.restaurant_picture);
        fab = view.findViewById(R.id.FABselect);

        mRestaurantPhoto .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment myDialogFragment = new LikeDialogFragment();
                myDialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        ImageView imageViewCall = view.findViewById(R.id.imageViewCall);
        imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL);
                } else {
                    dialPhoneNumber();
                }
            }
        });
        ImageView imageViewLike = view.findViewById(R.id.imageViewLike);
        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment myDialogFragment = new LikeDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putString("restoId", restoId);
                myDialogFragment.setArguments(bundle);

                myDialogFragment.show(getFragmentManager(), "dialog");
            }
        });
        ImageView imageViewWebsite = view.findViewById(R.id.imageViewWebsite);
        imageViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, mWebsite);
                startActivity(browserIntent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHelper.updateSelectedRestaurant(service.getCurrentUserId(), restoId);
                service.addAttendantToRestaurant(restoId, service.getCurrentUserId());
                Toast.makeText(context, "Restaurant selectionné", Toast.LENGTH_SHORT).show();
            }
        });
        setValues();

        return view;
    }


    public void setValues(){
        textViewName.setText(mRestaurant.getName());
        Log.i("alex", "assign textviewname");
        textViewAddress.setText(mRestaurant.getVicinity());
        mPicture = mRestaurant.getPhoto();
        if (mPicture.equals("no_pic")) {
            mRestaurantPhoto.setImageResource(R.drawable.resto_sign300);
            Log.i("alex", "set up resto sign 300 ");

        } else {
            Log.i("alex", "glide ");
            String apiKey = this.getString(R.string.google_maps_key);
            String photoURL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + mRestaurant.getPhoto() + "&sensor=false&maxheight=300&maxwidth=300&key=" + apiKey;

            Glide.with(this)
                    .load(photoURL)
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestaurantPhoto);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialPhoneNumber();
                } else {
                    Toast.makeText(context, "Permission denied, no phone call...", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void dialPhoneNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mPhone));
        startActivity(callIntent);
    }

    public void likeIt(View view) {

    }

    private void getLikeDialog() {

        FragmentManager fm = getFragmentManager();
        LikeDialogFragment likeDialogFragment = new LikeDialogFragment();
        likeDialogFragment.show(fm, "fragment_like");

    }

    private void onLikeButtonClick(DialogInterface dialogInterface) {

    }
}
