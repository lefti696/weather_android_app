package com.example.r2d2.weather_r1;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pwittchen.weathericonview.library.WeatherIconView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
If you check source of ActionBarActivity
you will see that ActionBarActivity extends
FragmentActivity. So you only need to extend ActionBarActivity.
 */
public class MainActivity extends ActionBarActivity implements ThirdFragment.TextClicked{

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    final Context context = this;
    Weather weather;
    int thirdFragmentStatus = 0;

    private String city = "lodz";
    private char units = 'c';

    private static final String TAG = "tomek";
    ArrayList<Fragment> aFragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // detect if app is running on a tablet and lock position
        if (getResources().getBoolean(R.bool.is_tablet)) {
//            Toast.makeText(MainActivity.this, "tablet mode", Toast.LENGTH_LONG).show();
            Log.v(TAG, "tablet mode");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//            FirstFragment firstFragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1);
        }
        else{
//            Toast.makeText(MainActivity.this, "phone mode", Toast.LENGTH_LONG).show();
            Log.v(TAG, "phone mode");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mPager = (ViewPager) findViewById(R.id.pager);
            // how many instances to keep in memory
            mPager.setOffscreenPageLimit(10);
            mPager.setAdapter(mPagerAdapter);

        }

        Log.v(TAG, "end of onCreate from MainActivity");
    }

    private void getWeather() throws JSONException {
        if(isOnline()){
            Log.v(TAG, "online mode");
            try {
                weather = parseWeather(getJSONfromUrl());
                updateFragments();
                saveToFile();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "City: " + city + " not found !", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Log.v(TAG, "offline mode");
                if( readFromFile() == null){

                }
            else{
                    weather = readFromFile();
                    updateFragments();
                }
        }
    }

    private void updateFragments(){

        for(int i=0; i < aFragmentList.size(); i++){
            if(aFragmentList.get(i) instanceof FirstFragment) ((FirstFragment) aFragmentList.get(i)).updateWeather(weather);
            else if(aFragmentList.get(i) instanceof SecondFragment) ((SecondFragment) aFragmentList.get(i)).updateWeather(weather);
            else if((aFragmentList.get(i) instanceof ThirdFragment) && thirdFragmentStatus > 0) ((ThirdFragment) aFragmentList.get(i)).updateWeather(weather);
        }

    }

    private Weather readFromFile() {

//        File file = new File(context.getFilesDir(), city+".txt");
//        Log.v(TAG,context.getFilesDir().toString());
        String filename = getExternalCacheDir()+"/"+city+".txt";
        File file = new File(filename);
        Log.v(TAG,"czytam");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "cannot display weather, turn wifi on", Toast.LENGTH_LONG).show();
        } else {
            try {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
                weather = null;
                weather = (Weather) input.readObject();
                Toast.makeText(this, "Weather for: " + city + " loaded.", Toast.LENGTH_SHORT).show();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No weather for: " + city, Toast.LENGTH_SHORT).show();
            }
        }
        return weather;
    }

    private void saveToFile() {
//        File file = new File(context.getFilesDir(), city+".txt");
//        Log.v(TAG,context.getFilesDir().toString());
        String filename = getExternalCacheDir()+"/"+city+".txt";
        File file = new File(filename);
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(weather);
            out.close();
            Log.v(TAG,"Weather saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.v(TAG, String.valueOf(fragment) + " Created");
        aFragmentList.add(fragment);
//        if(aFragmentList.size() > 2){
//            Log.v(TAG,"all fragments loaded time for init");
//            try {
//                getWeather();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }


      @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // obsluga pola search w action bar
        menu.findItem(R.id.action_search).getActionView();

        // set default text to shearch field
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.string_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                city = s;
//                Toast.makeText(MainActivity.this, "Text submit: " + city, Toast.LENGTH_LONG).show();
                try {
                    getWeather();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // application logo in action bar
        android.support.v7.app.ActionBar aBar = getSupportActionBar();
        aBar.setDisplayShowHomeEnabled(true);
        aBar.setLogo(R.mipmap.ic_launcher);
        aBar.setDisplayUseLogoEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(MainActivity.this, "refresh pressed", Toast.LENGTH_LONG).show();
                try {
                    getWeather();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_units:

                // sekcja popup
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                builder.setPositiveButton("C", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Celsius selected", Toast.LENGTH_SHORT).show();
                        units = 'c';
                        try {
                            getWeather();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("F", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Fahrenheit selected", Toast.LENGTH_SHORT).show();
                        units = 'f';
                        try {
                            getWeather();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void sendText(String text) {
        Log.v(TAG,"hurra!");
        thirdFragmentStatus = 1;
//        if(aFragmentList.get(2) instanceof ThirdFragment) ((ThirdFragment) aFragmentList.get(2)).updateWeather(weather);
        try {
            getWeather();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */


    public static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        //getItem will be called whenever the adapter needs a fragment and the fragment does not exist
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + (position + 1);
        }
    }

    private String getJSONfromUrl() {
        // to avoid erroer Error StrictMode$AndroidBlockGuardPolicy.onNetwork
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                city.replaceAll(" ", "%20") + "%22)and%20u%3D%22" + units + "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        InputStream is = null;
        String jsonStr = "";
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonStr = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
//        Log.v(TAG,jsonStr);
        return jsonStr;
    }

    private Weather parseWeather (String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject channel = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
        String created = jsonObject.getJSONObject("query").getString("created");
        String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        DateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(created);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String city = channel.getJSONObject("location").getString("city");
        String country = channel.getJSONObject("location").getString("country");
        int code = channel.getJSONObject("item").getJSONObject("condition").getInt("code");
        double lat = channel.getJSONObject("item").getDouble("lat");
        double lng = channel.getJSONObject("item").getDouble("long");
        String time = channel.getJSONObject("item").getJSONObject("condition").getString("date");
        int temp = channel.getJSONObject("item").getJSONObject("condition").getInt("temp");
        int pressure = channel.getJSONObject("atmosphere").getInt("pressure");
        String description = channel.getJSONObject("item").getJSONObject("condition").getString("text");
        int windDirection = channel.getJSONObject("wind").getInt("direction");
        int windSpeed = channel.getJSONObject("wind").getInt("speed");
        int humidity = channel.getJSONObject("atmosphere").getInt("humidity");
        double visibility = channel.getJSONObject("atmosphere").getDouble("visibility");
        String sunrise = channel.getJSONObject("astronomy").getString("sunrise");
        String sunset = channel.getJSONObject("astronomy").getString("sunset");

        // weather for 5 days
        Gson gson = new Gson();
        JSONArray forecast = channel.getJSONObject("item").getJSONArray("forecast");
        Type listType = new TypeToken<List<Weather.ShortWeather>>(){}.getType();
        List<Weather.ShortWeather> nextDays = gson.fromJson(forecast.toString(), listType);
//        List<Weather.ShortWeather> nextDays = null;

        return new Weather(city, country, code, lat, lng, time, temp, pressure, description, windDirection, windSpeed,
                humidity, visibility, sunrise, sunset, nextDays);

    }

}