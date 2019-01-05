package pt.ua.icm.weatherapp;

import Models.Distrit;
import Models.Weather;
import Models.WeatherIdentifier;
import Models.WindSpeed;
import Models.WindSpeedData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RemoteDataSource {
    String BASE_URL = "http://api.ipma.pt/open-data/";



    @GET("forecast/meteorology/cities/daily/{localId}.json")
    Call<Weather> getWeather(@Path("localId") int localId);

    @GET("distrits-islands.json")
    Call<Distrit> getCities();

    @GET("weather-type-classe.json")
    Call<WeatherIdentifier> getWeatherIdentifier();

    @GET("wind-speed-daily-classe.json")
    Call<WindSpeed> getWindSpeed();


}