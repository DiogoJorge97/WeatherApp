package pt.ua.icm.weatherapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


import java.util.List;

import Models.WeatherData;


@Dao
public interface WeatherDao {

    @Insert(onConflict = REPLACE)
    void save(WeatherData weatherData);

    @Insert
    void insert(WeatherData weatherData);

    @Query("DELETE FROM weather_table")
    void deleteAll();

    @Query("SELECT * from weather_table WHERE globalIdLocal = :globalId  ORDER BY globalIdLocal ASC")
    LiveData<List<WeatherData>> getAllWeatherData(int globalId);


}