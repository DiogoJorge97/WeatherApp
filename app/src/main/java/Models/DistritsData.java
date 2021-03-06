package Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "distrit_table")
public class DistritsData {

    @SerializedName("idRegiao")
    @Expose
    private int idRegiao;

    @SerializedName("idAreaAviso")
    @Expose
    private String idAreaAviso;

    @SerializedName("idConcelho")
    @Expose
    private int idConcelho;

    @PrimaryKey
    @SerializedName("globalIdLocal")
    @Expose
    private int globalIdLocal;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("idDistrito")
    @Expose
    private int idDistrito;

    @SerializedName("local")
    @Expose
    private String local;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    public int getIdRegiao() {
        return idRegiao;
    }

    public void setIdRegiao(int idRegiao) {
        this.idRegiao = idRegiao;
    }

    public String getIdAreaAviso() {
        return idAreaAviso;
    }

    public void setIdAreaAviso(String idAreaAviso) {
        this.idAreaAviso = idAreaAviso;
    }

    public int getIdConcelho() {
        return idConcelho;
    }

    public void setIdConcelho(int idConcelho) {
        this.idConcelho = idConcelho;
    }

    public int getGlobalIdLocal() {
        return globalIdLocal;
    }

    public void setGlobalIdLocal(int globalIdLocal) {
        this.globalIdLocal = globalIdLocal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(int idDistrito) {
        this.idDistrito = idDistrito;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}