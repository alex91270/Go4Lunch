package com.example.go4lunchAlx.detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DetailPagerAdapter extends FragmentPagerAdapter {
    Bundle bundle;

    public DetailPagerAdapter(FragmentManager fm) {
        super(fm);
        bundle = new Bundle();
    }
    public DetailPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        DetailFragment frag = DetailFragment.newInstance();
        frag.setArguments(bundle);
        return frag;
    }

    /**
     * get the number of pages
     * @return
     */
    @Override
    //Only one choice
    public int getCount() {
        return 1;
    }
}