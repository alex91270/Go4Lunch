package com.example.go4lunchAlx.helpers;

import android.content.Context;
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

    //processes the list of Period returned by the fetchPlaces request to return the string to be
    //displayed in the listViewFragment recyclerView
    public String getOpeningString(Context context, List<Period> listPeriods, Date dateNow) {

        result = context.getString(R.string.closed_today);

        dayString = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateNow.getTime());

        for (Period period : listPeriods) {

            //if no closing time specified, it means the place is opened 24/7
            //(Google Developper documentation)
            if (period.getClose() == null) {
                result = context.getString(R.string.opened247);

                return result;
            }
            //if one Period concerns the current day of week, this is the one we need for today
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
                    result = context.getString(R.string.opens_at) + addZero(period.getOpen().getTime().getHours()) + "h" + addZero(period.getOpen().getTime().getMinutes());
                }
                if (dateNow.getTime() > openingDate.getTime() && dateNow.getTime() < closingDate.getTime()) {
                    result = context.getString(R.string.closes_at) + " " +  addZero(period.getClose().getTime().getHours()) + "h" + addZero(period.getOpen().getTime().getMinutes());
                    if ((closingDate.getTime() - dateNow.getTime()) < 900000) {
                        result = context.getString(R.string.closing_soon);
                    }
                }
            }
        }

        return result;
    }

    private String addZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
