package Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "wind_table")
public class WindSpeedData {

    @SerializedName("descClassWindSpeedDailyEN")
    @Expose
    private String descClassWindSpeedDailyEN;
    @SerializedName("descClassWindSpeedDailyPT")
    @Expose
    private String descClassWindSpeedDailyPT;
    @PrimaryKey
    @NonNull
    @SerializedName("classWindSpeed")
    @Expose
    private int classWindSpeed;

    public String getDescClassWindSpeedDailyEN() {
        return descClassWindSpeedDailyEN;
    }

    public void setDescClassWindSpeedDailyEN(String descClassWindSpeedDailyEN) {
        this.descClassWindSpeedDailyEN = descClassWindSpeedDailyEN;
    }

    public String getDescClassWindSpeedDailyPT() {
        return descClassWindSpeedDailyPT;
    }

    public void setDescClassWindSpeedDailyPT(String descClassWindSpeedDailyPT) {
        this.descClassWindSpeedDailyPT = descClassWindSpeedDailyPT;
    }

    public int getClassWindSpeed() {
        return classWindSpeed;
    }

    public void setClassWindSpeed(int classWindSpeed) {
        this.classWindSpeed = classWindSpeed;
    }

    @Override
    public String toString() {
        return "WindSpeedData{" +
                "descClassWindSpeedDailyEN='" + descClassWindSpeedDailyEN + '\'' +
                ", descClassWindSpeedDailyPT='" + descClassWindSpeedDailyPT + '\'' +
                ", classWindSpeed=" + classWindSpeed +
                '}';
    }
}