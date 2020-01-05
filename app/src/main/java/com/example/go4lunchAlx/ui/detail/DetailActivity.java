package com.example.go4lunchAlx.ui.detail;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.go4lunchAlx.R;
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

        Bundle bundle = new Bundle();
        bundle.putString("restoId", restoId);

        mPagerAdapterD = new DetailPagerAdapter(getSupportFragmentManager(), bundle);
        mViewPagerD.setAdapter(mPagerAdapterD);
    }
}
