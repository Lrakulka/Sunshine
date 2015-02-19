package com.task.krabiysok.sunshine;

import org.json.JSONException;

/**
 * Created by KrabiySok on 2/19/2015.
 */
public class WeatherDataParser {
    /**
     * Given a string of the form returned by the api call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * retrieve the maximum temperature for the day indicated by dayIndex
     * (Note: 0-indexed, so 0 would refer to the first day).
     */
    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
        throws JSONException {
        int begin = 0, end;
        String maxTemp = "0";
        for (int i = 0; i < dayIndex + 1 && begin != -1; ++i) {
            begin = weatherJsonStr.indexOf("max\":", begin) + 5;
        }
        if (begin != -1) {
            end = weatherJsonStr.indexOf(",", begin);
            if (begin != -1 && end != -1) {
                maxTemp = weatherJsonStr.substring(begin, end);
            }
        }
        return Double.valueOf(maxTemp);
    }
}
