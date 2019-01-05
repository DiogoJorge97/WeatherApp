package pt.ua.icm.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.DistritsData;
import Models.WeatherData;
import Models.WeatherIdentifierData;
import Models.WindSpeedData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag MainActivity";

    private LinearLayout linearLayout;
    private Map<String, Integer> distritMap;
    private List<String> distritList;
    private Map<Integer, String> windMap;
    private Map<Integer, String> identifierMap;
    private int currentCity;

    private WeatherViewModel weatherViewModel;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentCity = 1010500;

        distritMap = new HashMap<>();
        distritList = new ArrayList<>();
        windMap = new ArrayMap<>();
        identifierMap = new ArrayMap<>();


        linearLayout = findViewById(R.id.linear);


        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        weatherViewModel.refreshAll();
        weatherViewModel.getAllWindSpeed().observe(this, this::createWindMap);

        weatherViewModel.getAllDistritData().observe(this, this::saveDistritMap);


    }

    private void createWindMap(List<WindSpeedData> windSpeedDataList) {
        for (WindSpeedData windSpeedData: windSpeedDataList){
            windMap.put(windSpeedData.getClassWindSpeed() ,windSpeedData.getDescClassWindSpeedDailyPT());
        }
        weatherViewModel.getAllWeatherIdentifier().observe(this, this::createIdentifierMap);

    }

    private void createIdentifierMap(List<WeatherIdentifierData> weatherIdentifierDataList) {
        for (WeatherIdentifierData weatherIdentifierData: weatherIdentifierDataList){
            identifierMap.put(weatherIdentifierData.getIdWeatherType(), weatherIdentifierData.getDescIdWeatherTypePT());
        }
        weatherViewModel.getWeatherData(currentCity).observe(this, this::updateUI);

    }





    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateUI(List<WeatherData> weatherDataList) {
        if(linearLayout.getChildCount() > 0){
            linearLayout.removeAllViews();
        }

        if (weatherDataList.isEmpty()){
            View view = getLayoutInflater().inflate(R.layout.empty_item, null);
            linearLayout.addView(view);
        } else {
            for (WeatherData weatherData: weatherDataList){
                View view = getLayoutInflater().inflate(R.layout.item, null);
                TextView weatherType = view.findViewById(R.id.weather_type);
                TextView tMax = view.findViewById(R.id.t_max);
                TextView tMin = view.findViewById(R.id.t_min);
                TextView windSpeed = view.findViewById(R.id.wind_speed);
                TextView windDiretion = view.findViewById(R.id.wind_direction);
                TextView preipitationProb = view.findViewById(R.id.prec_prob);
                TextView date = view.findViewById(R.id.date);

                weatherData.setWeatherIdentifier(identifierMap.get(weatherData.getIdWeatherType()));
                weatherData.setWindSpeed(windMap.get(weatherData.getClassWindSpeed()));
                weatherType.setText(weatherData.getWeatherIdentifier());
                tMax.setText(getString(R.string.Tmax) + weatherData.getTMax());
                tMin.setText(getString(R.string.Tmin) + weatherData.getTMin());
                windSpeed.setText(weatherData.getWindSpeed());
                windSpeed.setText(weatherData.getWindSpeed());
                windDiretion.setText(weatherData.getPredWindDir());
                preipitationProb.setText(weatherData.getPrecipitaProb() + getString(R.string.percentage));
                date.setText(weatherData.getForecastDate());


                Log.d(TAG, weatherData.toString());
                linearLayout.addView(view);
            }
        }
    }

    private void saveDistritMap(List<DistritsData> distritsDataList) {
        int localId;
        String localName;
        for (DistritsData distritsData: distritsDataList){
            localId = distritsData.getGlobalIdLocal();
            localName = distritsData.getLocal();
            distritMap.put(localName, localId);
        }
        distritList.clear();
        for (Map.Entry entry: distritMap.entrySet()){
            distritList.add(entry.getKey().toString());
        }

        autoCompleteTextView = findViewById(R.id.auto_complete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, distritList);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, arg1, pos, id) -> {
            Toast.makeText(MainActivity.this,"Distrito selecionado: " + adapter.getItem(pos), Toast.LENGTH_SHORT).show();
            currentCity = distritMap.get(adapter.getItem(pos));
            weatherViewModel.getWeatherData(currentCity).observe(MainActivity.this, MainActivity.this:: updateUI);
            hideKeyboard();


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_all:
                weatherViewModel.refreshAll();
                return true;
        }
        return true;
    }
}
