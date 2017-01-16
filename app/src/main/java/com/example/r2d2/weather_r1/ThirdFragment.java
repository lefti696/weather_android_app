package com.example.r2d2.weather_r1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pwittchen.weathericonview.library.WeatherIconView;


public class ThirdFragment extends Fragment {

    int position;
    private static final String TAG = "tomek";
    public static final String ARG_PAGE = "page";
    private static TextView day1Text;
    private static TextView day2Text;
    private static TextView day3Text;
    private static TextView day4Text;
    private static TextView day5Text;

    WeatherIconView weatherIconView1;
    WeatherIconView weatherIconView2;
    WeatherIconView weatherIconView3;
    WeatherIconView weatherIconView4;
    WeatherIconView weatherIconView5;

    TextClicked mCallback;

    public interface TextClicked{
        public void sendText(String text);
    }


    public ThirdFragment() {
        // Required empty public constructor
        Log.v(TAG, "empty constructor from ThirdFragment 3");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate ThirdFragment 3");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView ThirdFragment 3");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third2, container, false);

        day1Text = (TextView) view.findViewById(R.id.day1);
        day2Text = (TextView) view.findViewById(R.id.day2);
        day3Text = (TextView) view.findViewById(R.id.day3);
        day4Text = (TextView) view.findViewById(R.id.day4);
        day5Text = (TextView) view.findViewById(R.id.day5);

        weatherIconView1 = (WeatherIconView) view.findViewById(R.id.day1_icon);
        weatherIconView2 = (WeatherIconView) view.findViewById(R.id.day2_icon);
        weatherIconView3 = (WeatherIconView) view.findViewById(R.id.day3_icon);
        weatherIconView4 = (WeatherIconView) view.findViewById(R.id.day4_icon);
        weatherIconView5 = (WeatherIconView) view.findViewById(R.id.day5_icon);

        mCallback.sendText("");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TextClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }


    public void updateWeather(Weather weather){

        Log.v(TAG, "updateWeather from ThirdFragment 3");

        String icon1 = setWeatherIcon(weather.nextDays.get(0).code);
        String icon2 = setWeatherIcon(weather.nextDays.get(1).code);
        String icon3 = setWeatherIcon(weather.nextDays.get(2).code);
        String icon4 = setWeatherIcon(weather.nextDays.get(3).code);
        String icon5 = setWeatherIcon(weather.nextDays.get(4).code);

        int resID1 = getResources().getIdentifier("" + icon1, "string", getActivity().getPackageName());
        int resID2 = getResources().getIdentifier("" + icon2, "string", getActivity().getPackageName());
        int resID3 = getResources().getIdentifier("" + icon3, "string", getActivity().getPackageName());
        int resID4 = getResources().getIdentifier("" + icon4, "string", getActivity().getPackageName());
        int resID5 = getResources().getIdentifier("" + icon5, "string", getActivity().getPackageName());

        weatherIconView1.setIconResource(getString(resID1));
        weatherIconView2.setIconResource(getString(resID2));
        weatherIconView3.setIconResource(getString(resID3));
        weatherIconView4.setIconResource(getString(resID4));
        weatherIconView5.setIconResource(getString(resID5));

        day1Text.setText(weather.nextDays.get(0).toString());
        day2Text.setText(weather.nextDays.get(1).toString());
        day3Text.setText(weather.nextDays.get(2).toString());
        day4Text.setText(weather.nextDays.get(3).toString());
        day5Text.setText(weather.nextDays.get(4).toString());
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
