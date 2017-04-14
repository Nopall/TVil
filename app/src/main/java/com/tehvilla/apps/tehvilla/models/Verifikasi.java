package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SahabatDeveloper on 1/24/2017.
 */

public class Verifikasi {
    @SerializedName("kode_pengguna")
    @Expose
    private String kodePengguna;
    @SerializedName("last_token")
    @Expose
    private String last_token;
    @SerializedName("verifikasi")
    @Expose
    private String verifikasi;

    public String getKodePengguna() {
        return kodePengguna;
    }

    public void setKodePengguna(String kodePengguna) {
        this.kodePengguna = kodePengguna;
    }

    public String getLast_token() {
        return last_token;
    }

    public void setLast_token(String last_token) {
        this.last_token = last_token;
    }

    public String getVerifikasi() {
        return verifikasi;
    }

    public void setVerifikasi(String verifikasi) {
        this.verifikasi = verifikasi;
    }
}
