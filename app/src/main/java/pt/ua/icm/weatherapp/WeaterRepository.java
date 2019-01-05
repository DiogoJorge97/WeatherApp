package pt.ua.icm.weatherapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import DAO.DistritDao;
import DAO.WeatherDao;
import DAO.WeatherIdentifierDao;
import DAO.WindSpeedDao;
import Models.Distrit;
import Models.DistritsData;
import Models.Weather;
import Models.WeatherData;
import Models.WeatherIdentifier;
import Models.WeatherIdentifierData;
import Models.WindSpeed;
import Models.WindSpeedData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeaterRepository {
    private static int FRESH_TIMEOUT_IN_MINUTES = 10;
    public static final String BASE_URL = "http://api.ipma.pt/open-data/";


    private final RemoteDataSource remoteDataSource;
    private final Executor executor;

    private WeatherDao weatherDao;
    private DistritDao distritDao;
    private WindSpeedDao windSpeedDao;
    private WeatherIdentifierDao weatherIdentifierDao;
    private Map<String, Integer> distritMap;
    private List<String> distritList;
    private Map<Integer, String> windMap;


    public WeaterRepository(Application application) {
        WeatherRoomDatabase db = WeatherRoomDatabase.getDatabase(application);
        weatherDao = db.weatherDao();
        distritDao = db.distritDao();
        windSpeedDao = db.windSpeedDao();
        weatherIdentifierDao = db.weatherIdentifierDao();
        windMap = new ArrayMap<>();


        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        remoteDataSource = retrofit.create(RemoteDataSource.class);
        executor = Executors.newSingleThreadExecutor();

    }

    public void refreshAll(){
        refreshData();
    }

    public LiveData<List<DistritsData>> getDistrits() {
        LiveData<List<DistritsData>> distritDataList = distritDao.getAllDistrits(); // return a LiveData directly from the database
        return distritDataList;
    }

    public LiveData<List<WeatherData>> getWeatherData(int globalID) {
        refreshWeather(globalID); // try to refresh data if possible from Api
        LiveData<List<WeatherData>> weatherData = weatherDao.getAllWeatherData(globalID); // return a LiveData directly from the database
        return weatherData;
    }

    public LiveData<List<WindSpeedData>> getWindSpeedData() {
        LiveData<List<WindSpeedData>> windSpeedData = windSpeedDao.getAllWind(); // return a LiveData directly from the database
        return windSpeedData;
    }

    public LiveData<List<WeatherIdentifierData>> getWeatherIdentifier() {
        LiveData<List<WeatherIdentifierData>> weatherIdentifierData = weatherIdentifierDao.getAllWeatherIdentifierData(); // return a LiveData directly from the database
        return weatherIdentifierData;
    }


    private void refreshData() {
        executor.execute(() -> {
                remoteDataSource.getCities().enqueue(new Callback<Distrit>() {
                    @Override
                    public void onResponse(Call<Distrit> call, Response<Distrit> response) {
                        List<DistritsData> dataList = response.body().getData();
                        for (DistritsData distritsData : dataList) {
                            new insertAsyncTaskDistrit(distritDao).execute(distritsData);
                        }
                    }

                    @Override
                    public void onFailure(Call<Distrit> call, Throwable t) {
                    }
                });
                remoteDataSource.getWindSpeed().enqueue(new Callback<WindSpeed>() {
                    @Override
                    public void onResponse(Call<WindSpeed> call, Response<WindSpeed> response) {
                        List<WindSpeedData> dataList = response.body().getData();
                        for (WindSpeedData windSpeedData : dataList) {
                            new insertAsyncTaskWind(windSpeedDao).execute(windSpeedData);
                        }
                    }

                    @Override
                    public void onFailure(Call<WindSpeed> call, Throwable t) {
                    }
                });
                remoteDataSource.getWeatherIdentifier().enqueue(new Callback<WeatherIdentifier>() {
                    @Override
                    public void onResponse(Call<WeatherIdentifier> call, Response<WeatherIdentifier> response) {
                        List<WeatherIdentifierData> dataList = response.body().getData();
                        for (WeatherIdentifierData weatherIdentifierData : dataList) {
                            new insertAsyncTaskWeatherIdentifier(weatherIdentifierDao).execute(weatherIdentifierData);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherIdentifier> call, Throwable t) {
                    }
                });



        });
    }

    private void refreshWeather(final int globalID) {
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean dataExists = (weatherDao.hasData(globalID) != null);
            Log.d("MyTag", "Weather data exists in Room: " + dataExists);
            // If user have to be updated
            if (!dataExists) {
                remoteDataSource.getWeather(globalID).enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        Weather weather = response.body();
                        if (response.body() != null) {
                            List<WeatherData> dataList = response.body().getData();
                            for (WeatherData weatherData : dataList) {
                                weatherData.setWindSpeed(windMap.get(weatherData.getClassWindSpeed()));
                                insertWeather(weatherData);
                                weatherData.setGlobalIdLocal(globalID);
                            }
                            weather.setLastRefresh(new Date());
                        }
                    }
                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {
                        Log.d("MyTag", "Failed");
                    }
                });
            }
        });
    }


    private static class insertAsyncTaskWind extends AsyncTask<WindSpeedData, Void, Void> {

        private WindSpeedDao mAsyncTaskDao;

        insertAsyncTaskWind(WindSpeedDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WindSpeedData... params) {
            mAsyncTaskDao.save(params[0]);
            return null;
        }
    }

    private static class insertAsyncTaskWeatherIdentifier extends AsyncTask<WeatherIdentifierData, Void, Void> {

        private WeatherIdentifierDao mAsyncTaskDao;

        insertAsyncTaskWeatherIdentifier(WeatherIdentifierDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WeatherIdentifierData... params) {
            mAsyncTaskDao.save(params[0]);
            return null;
        }

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
