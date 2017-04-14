package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AkhmadNaufal on 2/15/17.
 */
public class PesananDetail {
    @SerializedName("kode_produk")
    @Expose
    private String kodeProduk;
    @SerializedName("nama_produk")
    @Expose
    private String namaProduk;
    @SerializedName("jumlah_beli")
    @Expose
    private String jumlahBeli;
    @SerializedName("harga_satuan")
    @Expose
    private String hargaSatuan;
    @SerializedName("url_gambar")
    @Expose
    private String urlGambar;

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getJumlahBeli() {
        return jumlahBeli;
    }

    public void setJumlahBeli(String jumlahBeli) {
        this.jumlahBeli = jumlahBeli;
    }

    public String getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(String hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }
}
