package com.example.go4lunchAlx.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.fragment.app.Fragment;
import com.example.go4lunchAlx.R;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private Spinner spinner;
    private Context context;
    private Switch mSwitch;
    private SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {
        return (new SettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        context = view.getContext();
        spinner = view.findViewById(R.id.spinner_sort_list);
        mSwitch = view.findViewById(R.id.switch_notifications);
        sharedPreferences = context.getSharedPreferences("com.example.go4lunchAlx", Context.MODE_PRIVATE);

        setupSpinner();
        setupSwitch();

        return view;
    }


    private void setupSpinner() {

        final List<String> spinnerArray =  new ArrayList<>();
        //fills spinner
        spinnerArray.add(context.getString(R.string.sort_dist));
        spinnerArray.add(context.getString(R.string.sort_rate));
        spinnerArray.add(context.getString(R.string.sort_attendants));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_sort_list, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(Integer.valueOf(sharedPreferences.getString("sortPrefs", "1")));

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

    private void setupSwitch() {

        if (sharedPreferences.getString("notifPrefs", "on").equals("on")) {
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreferences.edit().putString("notifPrefs", "on").apply();
                } else {
                    sharedPreferences.edit().putString("notifPrefs", "off").apply();
                }
            }
        });
    }
}