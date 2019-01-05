package Models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "weather_identifier_table")
public class WeatherIdentifierData {

    @SerializedName("descIdWeatherTypeEN")
    @Expose
    private String descIdWeatherTypeEN;
    @SerializedName("descIdWeatherTypePT")
    @Expose
    private String descIdWeatherTypePT;
    @NonNull
    @PrimaryKey
    @SerializedName("idWeatherType")
    @Expose
    private int idWeatherType;

    public String getDescIdWeatherTypeEN() {
        return descIdWeatherTypeEN;
    }

    public void setDescIdWeatherTypeEN(String descIdWeatherTypeEN) {
        this.descIdWeatherTypeEN = descIdWeatherTypeEN;
    }

    public String getDescIdWeatherTypePT() {
        return descIdWeatherTypePT;
    }

    public void setDescIdWeatherTypePT(String descIdWeatherTypePT) {
        this.descIdWeatherTypePT = descIdWeatherTypePT;
    }

    public int getIdWeatherType() {
        return idWeatherType;
    }

    public void setIdWeatherType(int idWeatherType) {
        this.idWeatherType = idWeatherType;
    }

}