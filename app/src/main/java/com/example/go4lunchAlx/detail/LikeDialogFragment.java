package com.example.go4lunchAlx.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.helpers.RatingsHelper;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.service.RestApiService;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

public class LikeDialogFragment extends DialogFragment {

    private Button buttonRate;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private int rateGiven = 6;
    private RestApiService service = DI.getRestApiService();
    private List<Rating> listOfRatings;
    private String restoId;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        listOfRatings = service.getListOfRatings();
        return inflater.inflate(R.layout.dialog_like, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restoId = getArguments().getString("restoId");

        buttonRate = view.findViewById(R.id.buttonRate);
        star1 = view.findViewById(R.id.star1);
        star2 = view.findViewById(R.id.star2);
        star3 = view.findViewById(R.id.star3);
        star4 = view.findViewById(R.id.star4);
        star5 = view.findViewById(R.id.star5);

        star1.setOnClickListener((View v) -> {
            rateGiven = 1;
            star1.setImageResource(R.drawable.star30yellow);
            star2.setImageResource(R.drawable.star30);
            star3.setImageResource(R.drawable.star30);
            star4.setImageResource(R.drawable.star30);
            star5.setImageResource(R.drawable.star30);
        });
        star2.setOnClickListener((View v) -> {
            rateGiven = 2;
            star1.setImageResource(R.drawable.star30yellow);
            star2.setImageResource(R.drawable.star30yellow);
            star3.setImageResource(R.drawable.star30);
            star4.setImageResource(R.drawable.star30);
            star5.setImageResource(R.drawable.star30);
        });
        star3.setOnClickListener((View v) -> {
            rateGiven = 3;
            star1.setImageResource(R.drawable.star30yellow);
            star2.setImageResource(R.drawable.star30yellow);
            star3.setImageResource(R.drawable.star30yellow);
            star4.setImageResource(R.drawable.star30);
            star5.setImageResource(R.drawable.star30);
        });
        star4.setOnClickListener((View v) -> {
            rateGiven = 4;
            star1.setImageResource(R.drawable.star30yellow);
            star2.setImageResource(R.drawable.star30yellow);
            star3.setImageResource(R.drawable.star30yellow);
            star4.setImageResource(R.drawable.star30yellow);
            star5.setImageResource(R.drawable.star30);
        });
        star5.setOnClickListener((View v) -> {
            rateGiven = 5;
            star1.setImageResource(R.drawable.star30yellow);
            star2.setImageResource(R.drawable.star30yellow);
            star3.setImageResource(R.drawable.star30yellow);
            star4.setImageResource(R.drawable.star30yellow);
            star5.setImageResource(R.drawable.star30yellow);
        });
        buttonRate.setOnClickListener((View v) -> {
            Rating rating;
            if (rateGiven == 6) {
                Toast.makeText(getContext(), "Pick up a number of stars", Toast.LENGTH_SHORT).show();
            } else {
                String rid = service.getCurrentUserId() + restoId;
                rating = new Rating( rid, restoId, service.getCurrentUserId(), rateGiven );
                if(service.getListOfRatings().contains(rating)) {
                    Rating oldRating = listOfRatings.get((listOfRatings.indexOf(rating)));
                    RatingsHelper.updateRate(rateGiven, oldRating.getrID());

                    service.updateRating(rating);
                } else {
                    RatingsHelper.createRating(rid, restoId, service.getCurrentUserId(),
                            rateGiven).addOnFailureListener(onFailureListener());

                    service.addRating(rating);

                }
                dismiss();
            }
        });

    }

    private OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }
   }
