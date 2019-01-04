package pt.ua.icm.weatherapp;

import Models.Distrit;
import Models.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RemoteDataSource {
    String BASE_URL = "http://api.ipma.pt/open-data/";



    @GET("forecast/meteorology/cities/daily/{localId}.json")
    Call<Weather> getWeather(@Path("localId") int localId);

    @GET("distrits-islands.json")
    Call<Distrit> getCities();



}