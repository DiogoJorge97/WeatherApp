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
    private LiveData<List<WeatherData>> mAllWeather;

    public WeaterRepository(Application application) {
        WeatherRoomDatabase db = WeatherRoomDatabase.getDatabase(application);
        weatherDao = db.weatherDao();

        //mAllWeather = weatherDao.getAllWeatherData(1010500);

        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        remoteDataSource = retrofit.create(RemoteDataSource.class);

        executor = Executors.newSingleThreadExecutor();

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
            Log.d("MyTag","Data exists in Room: " + dataExists);
            // If user have to be updated
            if (!dataExists) {
            remoteDataSource.getWeather(globalID).enqueue(new Callback<Weather>() {
                @Override
                public void onResponse(Call<Weather> call, Response<Weather> response) {
                    executor.execute(() -> {
                        Weather weather = response.body();

                        List<WeatherData> dataList = response.body().getData();
                        String logMessage = "";
                        boolean flag = false;
                        for (WeatherData weatherData : dataList) {
                            logMessage += "Id: " + globalID + " | Date:" + weatherData.getForecastDate() + "\n";
                            weatherDao.save(weatherData);
                            if (flag == false){
                                insert(weatherData);
                                weatherData.setGlobalIdLocal(globalID);
                                flag = true;
                            }
                        }
                        Log.d("MyTag", logMessage);
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


    public void insert (WeatherData weatherData) {
        new insertAsyncTask(weatherDao).execute(weatherData);
    }

    private static class insertAsyncTask extends AsyncTask<WeatherData, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        insertAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WeatherData... params) {
            mAsyncTaskDao.insert(params[0]);
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
