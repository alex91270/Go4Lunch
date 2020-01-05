package com.example.go4lunchAlx.ui.detail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DetailPagerAdapter extends FragmentPagerAdapter {
    Bundle bundle;

    DetailPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        DetailFragment frag = DetailFragment.newInstance();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    //Only one choice
    public int getCount() {
        return 1;
    }
}