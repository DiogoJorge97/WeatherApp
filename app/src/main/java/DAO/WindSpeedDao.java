package DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


import Models.WindSpeedData;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface WindSpeedDao {
    @Insert(onConflict = REPLACE)
    void save(WindSpeedData windSpeedData);

    @Query("DELETE FROM wind_table")
    void deleteAll();

    @Query("SELECT * from wind_table")
    LiveData<List<WindSpeedData>> getAllWind();


    @Query("SELECT * FROM wind_table LIMIT 1")
    WindSpeedData hasData();
}
