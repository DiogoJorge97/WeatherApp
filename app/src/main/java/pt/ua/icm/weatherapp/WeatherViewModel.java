package pt.ua.icm.weatherapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import Models.DistritsData;
import Models.WeatherData;
import Models.WeatherIdentifierData;
import Models.WindSpeedData;

public class WeatherViewModel extends AndroidViewModel {

    private WeaterRepository mRepository;


    public WeatherViewModel (Application application) {
        super(application);
        mRepository = new WeaterRepository(application);
    }

    LiveData<List<DistritsData>> getAllDistritData() { return mRepository.getDistrits();}

    LiveData<List<WindSpeedData>> getAllWindSpeed() { return mRepository.getWindSpeedData();}

    LiveData<List<WeatherIdentifierData>> getAllWeatherIdentifier() { return mRepository.getWeatherIdentifier();}


    LiveData<List<WeatherData>> getWeatherData(int globalid) { return mRepository.getWeatherData(globalid); }



    public void insert(WeatherData weatherData) { mRepository.insertWeather(weatherData); }

    protected void refreshAll() { mRepository.refreshAll(); }
}