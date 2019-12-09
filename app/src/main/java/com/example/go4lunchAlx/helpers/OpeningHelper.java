package com.example.go4lunchAlx.helpers;

import android.util.Log;

import java.text.SimpleDateFormat;
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


    public String getOpeningString(List<Period> listPeriods, Date dateNow) {

        result = "Closed today";

        dayString = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateNow.getTime());

        for (Period period : listPeriods) {

            if (period.getClose() == null) {
                result = "Opened 24/7";
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
                    result = "closed";
                }
                if (dateNow.getTime() < openingDate.getTime()) {
                    result = "opens at " + period.getOpen().getTime().getHours() + "h" + period.getOpen().getTime().getMinutes();
                }
                if (dateNow.getTime() > openingDate.getTime() && dateNow.getTime() < closingDate.getTime()) {
                    result = "closes at " + period.getClose().getTime().getHours() + "h" + period.getOpen().getTime().getMinutes();
                    if ((closingDate.getTime() - dateNow.getTime()) < 900000) {
                        result = "closing soon";
                    }
                }
            }
        }

        return result;
    }
}
