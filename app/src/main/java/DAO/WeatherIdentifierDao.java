package DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import Models.WeatherIdentifierData;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherIdentifierDao {
    @Insert(onConflict = REPLACE)
    void save(WeatherIdentifierData weatherIdentifierData);

    @Query("DELETE FROM weather_identifier_table")
    void deleteAll();

    @Query("SELECT * from weather_identifier_table")
    LiveData<List<WeatherIdentifierData>> getAllWeatherIdentifierData();


    @Query("SELECT * FROM weather_identifier_table LIMIT 1")
    WeatherIdentifierData hasData();
}
