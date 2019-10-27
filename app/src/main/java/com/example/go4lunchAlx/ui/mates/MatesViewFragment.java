package com.example.go4lunchAlx.ui.mates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.go4lunchAlx.R;

public class MatesViewFragment extends Fragment {

    public static MatesViewFragment newInstance() {
        return (new MatesViewFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}