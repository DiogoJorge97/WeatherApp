package pt.ua.icm.weatherapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import Models.DistritsData;
import Models.WeatherData;

@Database(entities = {WeatherData.class, DistritsData.class}, version = 1)
public abstract class WeatherRoomDatabase extends RoomDatabase {

    public abstract WeatherDao weatherDao();

    public abstract DistritDao distritDao();

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