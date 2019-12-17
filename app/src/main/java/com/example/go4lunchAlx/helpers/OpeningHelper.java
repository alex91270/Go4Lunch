package com.example.go4lunchAlx.helpers;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;

import com.example.go4lunchAlx.R;
import com.google.android.libraries.places.api.model.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OpeningHelper {

    private String dayString;
    private String result;
    private Date openingDate;
    private Date closingDate;
    private Calendar calendar;


    public String getOpeningString(Context context, List<Period> listPeriods, Date dateNow) {

        result = context.getString(R.string.closed_today);

        dayString = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateNow.getTime());

        for (Period period : listPeriods) {

            if (period.getClose() == null) {
                result = context.getString(R.string.opened247);

                return result;
            }
            if (period.toString().contains(dayString.toUpperCase())) {

                calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, period.getOpen().getTime().getHours());
                calendar.set(Calendar.MINUTE, period.getOpen().getTime().getMinutes());
                openingDate = calendar.getTime();

                calendar.set(Calendar.HOUR_OF_DAY, period.getClose().getTime().getHours());
                calendar.set(Calendar.MINUTE, period.getClose().getTime().getMinutes());
                closingDate = calendar.getTime();

                if (dateNow.getTime() > closingDate.getTime()) {
                    result = context.getString(R.string.now_closed);
                }
                if (dateNow.getTime() < openingDate.getTime()) {
                    result = context.getString(R.string.opens_at) + period.getOpen().getTime().getHours() + "h" + period.getOpen().getTime().getMinutes();
                }
                if (dateNow.getTime() > openingDate.getTime() && dateNow.getTime() < closingDate.getTime()) {
                    result = context.getString(R.string.closes_at) + period.getClose().getTime().getHours() + "h" + period.getOpen().getTime().getMinutes();
                    if ((closingDate.getTime() - dateNow.getTime()) < 900000) {
                        result = context.getString(R.string.closing_soon);
                    }
                }
            }
        }

        return result;
    }
}
