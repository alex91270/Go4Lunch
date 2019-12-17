package com.example.go4lunchAlx.ui.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.fragment.app.Fragment;
import com.example.go4lunchAlx.R;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private Spinner spinner;
    private TextView textViewHourChoice;
    private Context context;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {
        return (new SettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        context = view.getContext();
        spinner = view.findViewById(R.id.spinner_sort_list);
        sharedPreferences = context.getSharedPreferences("com.example.go4lunchAlx", Context.MODE_PRIVATE);

        setupSpinner();

        return view;
    }


    public void setupSpinner() {

        final List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Trier par proximité");
        spinnerArray.add("Trier par note");
        spinnerArray.add("Trier par nombre de participants");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_sort_list, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(Integer.valueOf(sharedPreferences.getString("sortPrefs", "")));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sharedPreferences.edit().putString("sortPrefs", String.valueOf(position)).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}