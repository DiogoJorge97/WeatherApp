package Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "weather_table", primaryKeys = {"globalIdLocal", "forecastDate"})
public class WeatherData {

    @SerializedName("precipitaProb")
    @Expose
    private String precipitaProb;

    @SerializedName("tMin")
    @Expose
    private String tMin;

    @SerializedName("tMax")
    @Expose
    private String tMax;

    @SerializedName("predWindDir")
    @Expose
    private String predWindDir;

    @SerializedName("idWeatherType")
    @Expose
    private int idWeatherType;

    @SerializedName("classWindSpeed")
    @Expose
    private int classWindSpeed;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @NonNull
    @SerializedName("forecastDate")
    @Expose
    private String forecastDate;

    @NonNull
    @Expose
    private int globalIdLocal;


    @SerializedName("latitude")
    @Expose
    private String latitude;

    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public void setPrecipitaProb(String precipitaProb) {
        this.precipitaProb = precipitaProb;
    }

    public String getTMin() {
        return tMin;
    }

    public void setTMin(String tMin) {
        this.tMin = tMin;
    }

    public String getTMax() {
        return tMax;
    }

    public void setTMax(String tMax) {
        this.tMax = tMax;
    }

    public String getPredWindDir() {
        return predWindDir;
    }

    public void setPredWindDir(String predWindDir) {
        this.predWindDir = predWindDir;
    }

    public int getIdWeatherType() {
        return idWeatherType;
    }

    public void setIdWeatherType(int idWeatherType) {
        this.idWeatherType = idWeatherType;
    }

    public int getClassWindSpeed() {
        return classWindSpeed;
    }

    public void setClassWindSpeed(int classWindSpeed) {
        this.classWindSpeed = classWindSpeed;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public int getGlobalIdLocal() {
        return globalIdLocal;
    }

    public void setGlobalIdLocal(@NonNull int globalIdLocal) {
        this.globalIdLocal = globalIdLocal;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "precipitaProb='" + precipitaProb + '\'' +
                ", tMin='" + tMin + '\'' +
                ", tMax='" + tMax + '\'' +
                ", predWindDir='" + predWindDir + '\'' +
                ", idWeatherType=" + idWeatherType +
                ", classWindSpeed=" + classWindSpeed +
                ", longitude='" + longitude + '\'' +
                ", forecastDate='" + forecastDate + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}