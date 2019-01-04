package pt.ua.icm.weatherapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Models.Distrit;
import Models.DistritsData;
import Models.Weather;
import Models.WeatherData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeaterRepository {
    private static int FRESH_TIMEOUT_IN_MINUTES = 15;
    public static final String BASE_URL = "http://api.ipma.pt/open-data/";


    private final RemoteDataSource remoteDataSource;
    private final Executor executor;

    private WeatherDao weatherDao;
    private DistritDao distritDao;
    private LiveData<List<WeatherData>> mAllWeather;

    public WeaterRepository(Application application) {
        WeatherRoomDatabase db = WeatherRoomDatabase.getDatabase(application);
        weatherDao = db.weatherDao();
        distritDao = db.distritDao();

        //mAllWeather = weatherDao.getAllWeatherData(1010500);

        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        remoteDataSource = retrofit.create(RemoteDataSource.class);
        executor = Executors.newSingleThreadExecutor();

    }


    public LiveData<List<DistritsData>> getAllDistrits(){
        refresDistrit(); // try to refresh data if possible from Api
        LiveData<List<DistritsData>> distritDataList = distritDao.getAllDistrits(); // return a LiveData directly from the database
        return distritDataList;
    }


    private void refresDistrit() {
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean dataExists = (distritDao.hasData() != null);
            Log.d("MyTag", "Distrit data exists in Room: " + dataExists);
            // If user have to be updated
            if (!dataExists) {
                remoteDataSource.getCities().enqueue(new Callback<Distrit>() {
                    @Override
                    public void onResponse(Call<Distrit> call, Response<Distrit> response) {
                        executor.execute(() -> {
                            List<DistritsData> dataList = response.body().getData();
                            for (DistritsData distritsData : dataList) {
                                distritDao.save(distritsData);
                                insertDistrit(distritsData);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Distrit> call, Throwable t) {

                    }


                });
            }
        });
    }

    private void insertDistrit(DistritsData distritsData) {
        new insertAsyncTaskDistrit(distritDao).execute(distritsData);

    }

    private static class insertAsyncTaskDistrit extends AsyncTask<DistritsData, Void, Void> {

        private DistritDao mAsyncTaskDao;

        insertAsyncTaskDistrit(DistritDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DistritsData... params) {
            mAsyncTaskDao.save(params[0]);
            return null;
        }
    }


    public LiveData<List<WeatherData>> getAllWeatherData(int globalID) {
        refreshWeather(globalID); // try to refresh data if possible from Api
        LiveData<List<WeatherData>> weatherData = weatherDao.getAllWeatherData(globalID); // return a LiveData directly from the database
        return weatherData;
    }

    private void refreshWeather(final int globalID) {
        executor.execute(() -> {
            // Check if user was fetched recently
            //boolean weatherExists = (weatherDao.get(globalID, getMaxRefreshTime(new Date())) != null);
            boolean dataExists = (weatherDao.hasData(globalID) != null);
            Log.d("MyTag", "Weather data exists in Room: " + dataExists);
            // If user have to be updated
            if (!dataExists) {
                remoteDataSource.getWeather(globalID).enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        executor.execute(() -> {
                            Weather weather = response.body();

                            List<WeatherData> dataList = response.body().getData();
                            for (WeatherData weatherData : dataList) {
                                weatherDao.save(weatherData);
                                insertWeather(weatherData);
                                weatherData.setGlobalIdLocal(globalID);
                            }
                            weather.setLastRefresh(new Date());
                        });
                    }

                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void insertWeather(WeatherData weatherData) {
        new insertAsyncTaskWeather(weatherDao).execute(weatherData);
    }

    private static class insertAsyncTaskWeather extends AsyncTask<WeatherData, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        insertAsyncTaskWeather(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WeatherData... params) {
            mAsyncTaskDao.save(params[0]);
            return null;
        }
    }

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
}
