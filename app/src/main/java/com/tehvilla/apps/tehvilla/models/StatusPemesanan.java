package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AkhmadNaufal on 2/10/17.
 */
public class StatusPemesanan {
    @SerializedName("kode_tipe_pembayaran")
    @Expose
    private String kodeTipePembayaran;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    public String getKodeTipePembayaran() {
        return kodeTipePembayaran;
    }

    public void setKodeTipePembayaran(String kodeTipePembayaran) {
        this.kodeTipePembayaran = kodeTipePembayaran;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

}
