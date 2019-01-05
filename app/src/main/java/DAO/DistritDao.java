package DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import Models.DistritsData;
import Models.WeatherData;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DistritDao {
    @Insert(onConflict = REPLACE)
    void save(DistritsData distritsData);

    @Query("DELETE FROM distrit_table")
    void deleteAll();

    @Query("SELECT * from distrit_table  ORDER BY globalIdLocal ASC")
    LiveData<List<DistritsData>> getAllDistrits();


    //TODO Add last Refres to te query
    @Query("SELECT * FROM distrit_table LIMIT 1")
    DistritsData hasData();
}
