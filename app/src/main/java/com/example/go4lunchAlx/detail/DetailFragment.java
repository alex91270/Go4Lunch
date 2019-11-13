package com.example.go4lunchAlx.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.api.UserHelper;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.ui.list.ListRecyclerViewAdapter;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private Restaurant mRestaurant;
    private String mPhone;
    private Uri mWebsite;
    //private String mAddress;
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
    private String restoId;
    private RecyclerView mRecyclerView;
    private DetailRecyclerViewAdapter myAdapter;
    private List<User> mListAttendants;

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
        mRecyclerView = view.findViewById(R.id.list_attendants);
        fab = view.findViewById(R.id.FABselect);

        mListAttendants = new ArrayList<>();
        for (String userId : mRestaurant.getAttendants()) {
            mListAttendants.add(service.getUserById(userId));
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        myAdapter = new DetailRecyclerViewAdapter(mListAttendants);
        mRecyclerView.setAdapter(myAdapter);

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
                updateLocally();
                Toast.makeText(context, "Restaurant selectionné", Toast.LENGTH_SHORT).show();
            }
        });
        setValues();

        return view;
    }


    public void setValues(){
        textViewName.setText(mRestaurant.getName());
        textViewAddress.setText(mRestaurant.getVicinity());
        mPicture = mRestaurant.getPhoto();
        if (mPicture.equals("no_pic")) {
            mRestaurantPhoto.setImageResource(R.drawable.resto_sign300);
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

    private void dialPhoneNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mPhone));
        startActivity(callIntent);
    }

    private void updateLocally() {
        //add current user to this restaurant's attendant's list
        service.addAttendantToRestaurant(restoId, service.getCurrentUserId());

        if(service.getUserById(service.getCurrentUserId()).getSelectedRestaurant() != null) {

            String previousSelection = service.getUserById(service.getCurrentUserId()).getSelectedRestaurant();

            //remove current user from previously selected restaurant attendants list
            service.removeAttendantFromRestaurant(service.getCurrentUserId(), previousSelection);


            //update the selected restaurant of the current user in the service list
            service.setUserSelectedRestaurant(service.getCurrentUserId(), restoId);
        }
    }
}
