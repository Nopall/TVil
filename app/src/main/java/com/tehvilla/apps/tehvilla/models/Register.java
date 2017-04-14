package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SahabatDeveloper on 1/24/2017.
 */

public class Register {
    @SerializedName("kode_pengguna")
    @Expose
    private String kodePengguna;
    @SerializedName("register_type")
    @Expose
    private String registerType;
    @SerializedName("verifikasi")
    @Expose
    private String verifikasi;

    public String getKodePengguna() {
        return kodePengguna;
    }

    public void setKodePengguna(String kodePengguna) {
        this.kodePengguna = kodePengguna;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getVerifikasi() {
        return verifikasi;
    }

    public void setVerifikasi(String verifikasi) {
        this.verifikasi = verifikasi;
    }
}
