package pt.ua.icm.weatherapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import DAO.DistritDao;
import DAO.WeatherDao;
import DAO.WeatherIdentifierDao;
import DAO.WindSpeedDao;
import Models.DistritsData;
import Models.WeatherData;
import Models.WeatherIdentifierData;
import Models.WindSpeed;
import Models.WindSpeedData;

@Database(entities = {WeatherData.class, DistritsData.class, WeatherIdentifierData.class, WindSpeedData.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class WeatherRoomDatabase extends RoomDatabase {

    public abstract WeatherDao weatherDao();

    public abstract DistritDao distritDao();

    public abstract WindSpeedDao windSpeedDao();

    public abstract WeatherIdentifierDao weatherIdentifierDao();

    private static volatile WeatherRoomDatabase INSTANCE;

    static WeatherRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherRoomDatabase.class, "weather_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}