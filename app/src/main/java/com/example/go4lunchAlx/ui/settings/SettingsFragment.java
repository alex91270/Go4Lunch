package com.example.go4lunchAlx.ui.settings;

import android.app.TimePickerDialog;
import android.content.Context;
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
    private int hoursSet;
    private int minutesSet;

    public static SettingsFragment newInstance() {
        return (new SettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        context = view.getContext();
        spinner = view.findViewById(R.id.spinner_sort_list);
        textViewHourChoice = view.findViewById(R.id.textview_hour_choice);

        setTimeListener();
        setupSpinner();

        return view;
    }

    public void setTimeListener() {
        textViewHourChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timeDialog = new TimePickerDialog(
                        context,
                        R.style.TimeDialogTheme,
                        timeSetListener,
                        12, 00,
                        android.text.format.DateFormat.is24HourFormat(context));
                timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timeDialog.show();
            }
        });

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String date = addZero(hourOfDay) + "h" + addZero(minute);
                textViewHourChoice.setText(date);
                minutesSet = minute;
                hoursSet = hourOfDay;
            }
        };
    }

    public void setupSpinner() {


        final List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Trier par proximité");
        spinnerArray.add("Trier par note");
        spinnerArray.add("Trier par nombre de participants");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_sort_list, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //numberGuests = spinnerArray.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public String addZero(int number) {
        String result;
        if (number < 10) {
            result = "0" + String.valueOf(number);
        }else{
            result = String.valueOf(number);
        }
        return result;
    }
}