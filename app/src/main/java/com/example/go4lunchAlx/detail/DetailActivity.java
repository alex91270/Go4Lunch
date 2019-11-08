package com.example.go4lunchAlx.detail;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.api.RatingsHelper;
import com.example.go4lunchAlx.models.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    DetailPagerAdapter mPagerAdapterD;
    private String restoId;

    @BindView(R.id.containerD)
    ViewPager mViewPagerD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        restoId =  getIntent().getStringExtra("restoId");
        Log.i("alex", "activity resto id: " + restoId);

        Bundle bundle = new Bundle();
        bundle.putString("restoId", restoId);

        mPagerAdapterD = new DetailPagerAdapter(getSupportFragmentManager(), bundle);
        mViewPagerD.setAdapter(mPagerAdapterD);
    }
}
