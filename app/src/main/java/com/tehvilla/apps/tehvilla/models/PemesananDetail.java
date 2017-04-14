package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SahabatDeveloper on 1/25/2017.
 */

public class PemesananDetail {
    @SerializedName("kode_produk")
    @Expose
    private String kodeProduk;
    @SerializedName("jumlah")
    @Expose
    private String jumlah;
    @SerializedName("harga_satuan")
    @Expose
    private String hargaSatuan;
    @SerializedName("diskon")
    @Expose
    private String diskon;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("url_gambar")
    @Expose
    private String urlGambar;

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(String hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }
}
