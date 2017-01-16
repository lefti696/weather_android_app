package com.example.r2d2.weather_r1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pwittchen.weathericonview.library.WeatherIconView;

import org.w3c.dom.Document;


public class FirstFragment extends Fragment {

    WeatherIconView weatherIconView;
    int position;
    String title;
    public static final String ARG_PAGE = "page";
    private static TextView cityText;
    private static TextView tempText;
    private static TextView geoText;
    private static TextView timeText;
    private static TextView pressureText;
    private static TextView description;
    private static final String TAG = "tomek";

    public static FirstFragment create(int position, String name) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        args.putString(ARG_PAGE, name);
        fragment.setArguments(args);
        return fragment;
    }
    public FirstFragment() {
        // Required empty public constructor
        Log.v(TAG, "empty constructor from FirstFragment 1");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate from FirstFragment 1");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PAGE);
            title = getArguments().getString(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(TAG, "onCreateView FirstFragment 1");

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        cityText = (TextView) view.findViewById(R.id.current_city);
        tempText = (TextView) view.findViewById(R.id.temp);
        geoText = (TextView) view.findViewById(R.id.geo);
        timeText = (TextView) view.findViewById(R.id.time);
        pressureText = (TextView) view.findViewById(R.id.pressure);
        description = (TextView) view.findViewById(R.id.description);

        return view;
    }

    public void updateWeather(Weather weather){
        weatherIconView = (WeatherIconView) getView().findViewById(R.id.my_weather_icon);
        String icon = setWeatherIcon(weather.code);
        // weatherIconView.setIconResource(getString(R.string.wi_day_sunny));
        int resID = getResources().getIdentifier("" + icon, "string", getActivity().getPackageName());

        weatherIconView.setIconResource(getString(resID));

        cityText.setText(weather.city.toString() + ", " + weather.country.toString());
        tempText.setText(String.valueOf(weather.temp));
        geoText.setText(String.valueOf(weather.lat) + " : " + String.valueOf(weather.lng));
        timeText.setText(String.valueOf(weather.time));
        pressureText.setText(String.valueOf(weather.pressure));

        description.setText(Html.fromHtml(weather.description));
    }

    public void setCity(String city){
        Log.v(TAG, "setCity");
        cityText.setText(city);
    }

    private String setWeatherIcon(int code) {
        switch(code) {
            case 0: return "wi_tornado";
            case 1: return "wi_storm_showers";
            case 2: return "wi_tornado";
            case 3: return "wi_thunderstorm";
            case 4: return "wi_thunderstorm";
            case 5: return "wi_snow";
            case 6: return "wi_rain_mix";
            case 7: return "wi_rain_mix";
            case 8: return "wi_sprinkle";
            case 9: return "wi_sprinkle";
            case 10: return "wi_hail";
            case 11: return "wi_showers";
            case 12: return "wi_showers";
            case 13: return "wi_snow";
            case 14: return "wi_storm_showers";
            case 15: return "wi_snow";
            case 16: return "wi_snow";
            case 17: return "wi_hail";
            case 18: return "wi_hail";
            case 19: return "wi_cloudy_gusts";
            case 20: return "wi_fog";
            case 21: return "wi_fog";
            case 22: return "wi_fog";
            case 23: return "wi_cloudy_gusts";
            case 24: return "wi_cloudy_windy";
            case 25: return "wi_thermometer";
            case 26: return "wi_cloudy";
            case 27: return "wi_night_cloudy";
            case 28: return "wi_day_cloudy";
            case 29: return "wi_night_cloudy";
            case 30: return "wi_day_cloudy";
            case 31: return "wi_night_clear";
            case 32: return "wi_day_sunny";
            case 33: return "wi_night_clear";
            case 34: return "wi_day_sunny_overcast";
            case 35: return "wi_hail";
            case 36: return "wi_day_sunny";
            case 37: return "wi_thunderstorm";
            case 38: return "wi_thunderstorm";
            case 39: return "wi_thunderstorm";
            case 40: return "wi_storm_showers";
            case 41: return "wi_snow";
            case 42: return "wi_snow";
            case 43: return "wi_snow";
            case 44: return "wi_cloudy";
            case 45: return "wi_lightning";
            case 46: return "wi_snow";
            case 47: return "wi_thunderstorm";
            case 3200: return  "wi_cloud";
            default: return  "wi_cloud";
        }
    }

}
