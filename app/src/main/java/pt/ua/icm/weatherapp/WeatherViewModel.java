package pt.ua.icm.weatherapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import Models.WeatherData;

public class WeatherViewModel extends AndroidViewModel {

    private WeaterRepository mRepository;


    public WeatherViewModel (Application application) {
        super(application);
        mRepository = new WeaterRepository(application);
    }

    LiveData<List<WeatherData>> getmAllWeatherData() { return mRepository.getAllWeatherData(1010500); }

    public void insert(WeatherData weatherData) { mRepository.insert(weatherData); }
}