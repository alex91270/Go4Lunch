package com.example.go4lunchAlx.ui.yourlunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.go4lunchAlx.R;


public class YourLunchFragment extends Fragment {

    public static YourLunchFragment newInstance() {
        return (new YourLunchFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_yourlunch, container, false);
    }
}