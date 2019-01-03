package pt.ua.icm.weatherapp;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "weather_table")
public class Weather {

    @SerializedName("owner")
    @Expose
    private String owner;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("data")
    @Expose
    private List<WeatherData> data = null;

    @PrimaryKey
    @NonNull
    @SerializedName("globalIdLocal")
    @Expose
    private int globalIdLocal;

    @SerializedName("dataUpdate")
    @Expose
    private String dataUpdate;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<WeatherData> getData() {
        return data;
    }

    public void setData(List<WeatherData> data) {
        this.data = data;
    }

    public int getGlobalIdLocal() {
        return globalIdLocal;
    }

    public void setGlobalIdLocal(int globalIdLocal) {
        this.globalIdLocal = globalIdLocal;
    }

    public String getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(String dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "owner='" + owner + '\'' +
                ", country='" + country + '\'' +
                ", data=" + data +
                ", globalIdLocal=" + globalIdLocal +
                ", dataUpdate='" + dataUpdate + '\'' +
                '}';
    }
}