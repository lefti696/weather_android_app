package com.example.r2d2.weather_r1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SecondFragment extends Fragment {

    int position;
    private static final String TAG = "tomek";
    public static final String ARG_PAGE = "page";
    private static TextView windDirectionText;
    private static TextView windSpeedText;
    private static TextView humidityText;
    private static TextView visibilityText;
    private static TextView sunriseText;
    private static TextView sunsetText;

    public SecondFragment() {
        // Required empty public constructor
        Log.v(TAG, "empty constructor from SecondFragment 2");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate from SecondFragment 2");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView SecondFragment 2");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        windDirectionText = (TextView) view.findViewById(R.id.wind_direction);
        windSpeedText = (TextView) view.findViewById(R.id.wind_speed);
        humidityText = (TextView) view.findViewById(R.id.humidity);
        visibilityText = (TextView) view.findViewById(R.id.visibility);
        sunriseText = (TextView) view.findViewById(R.id.sunrise);
        sunsetText = (TextView) view.findViewById(R.id.sunset);

        return view;
    }

    public void updateWeather(Weather weather){
        Log.v(TAG, "updateWeather from SecondFragment 2");
        windDirectionText.setText(String.valueOf(weather.windDirection));
        windSpeedText.setText(String.valueOf(weather.windSpeed));
        humidityText.setText(String.valueOf(weather.humidity));
        visibilityText.setText(String.valueOf(weather.visibility));
        sunriseText.setText(weather.sunrise.toString());
        sunsetText.setText(weather.sunset.toString());
    }


}
