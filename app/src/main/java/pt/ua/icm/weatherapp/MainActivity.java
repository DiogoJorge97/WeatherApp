package pt.ua.icm.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;

import Models.WeatherData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button);


        WeatherViewModel weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        weatherViewModel.getmAllWeatherData().observe(this, weatherData -> updateUI(weatherData));




    }

    private void updateUI(List<WeatherData> weatherData) {

    }
}
