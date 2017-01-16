package com.example.r2d2.weather_r1;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

/**
 * Created by R2D2 on 2015-06-22.
 */
public class Weather implements Serializable{
    private static final String TAG = "tomek";

    String city;
    String country;
    int code;
    double lat;
    double lng;
    String time;
    int temp;
    int pressure;
    String description;
    int windDirection;
    int windSpeed;
    int humidity;
    double visibility;
    String sunrise;
    String sunset;
    List<ShortWeather> nextDays;

    public Weather(String city, String country, int code, double lat, double lng, String time, int temp, int pressure,
                   String description, int windDirection, int windSpeed, int humidity,
                   double visibility, String sunrise, String sunset, List<ShortWeather> nextDays) {
        this.city = city;
        this.code = code;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.temp = temp;
        this.pressure = pressure;
        this.description = description;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.visibility = visibility;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.country = country;
        this.nextDays = nextDays;
//        Log.v(TAG,"ok: " + nextDays.size());
    }

    public class ShortWeather implements Serializable{
        String date;
        String day;
        int high;
        int low;
        String text;
        int code;

        ShortWeather(int code, String date, String day, int high, int low, String text) {
            this.date = date;
            this.day = day;
            this.high = high;
            this.low = low;
            this.text = text;
            this.code = code;

        }

        @Override
        public String toString(){
            return date + " (" + day + "). " + "Temp range: " + low + "-" + high + " " + text;
        }
    }
}