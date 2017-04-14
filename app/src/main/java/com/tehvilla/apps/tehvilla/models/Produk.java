package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by NIT NB1 on 16/01/2017.
 */

public class Produk {

    @SerializedName("kode")
    @Expose
    private String kode;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("harga_awal")
    @Expose
    private String hargaAwal;
    @SerializedName("harga_akhir")
    @Expose
    private String hargaAkhir;
    @SerializedName("kategori_produk")
    @Expose
    private String kategoriProduk;
    @SerializedName("diskon")
    @Expose
    private String diskon;
    @SerializedName("gambar1")
    @Expose
    private String gambar1;
    @SerializedName("gambar2")
    @Expose
    private String gambar2;
    @SerializedName("gambar3")
    @Expose
    private String gambar3;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHargaAwal() {
        return hargaAwal;
    }

    public void setHargaAwal(String hargaAwal) {
        this.hargaAwal = hargaAwal;
    }

    public String getHargaAkhir() {
        return hargaAkhir;
    }

    public void setHargaAkhir(String hargaAkhir) {
        this.hargaAkhir = hargaAkhir;
    }

    public String getKategoriProduk() {
        return kategoriProduk;
    }

    public void setKategoriProduk(String kategoriProduk) {
        this.kategoriProduk = kategoriProduk;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getGambar1() {
        return gambar1;
    }

    public void setGambar1(String gambar1) {
        this.gambar1 = gambar1;
    }

    public String getGambar2() {
        return gambar2;
    }

    public void setGambar2(String gambar2) {
        this.gambar2 = gambar2;
    }

    public String getGambar3() {
        return gambar3;
    }

    public void setGambar3(String gambar3) {
        this.gambar3 = gambar3;
    }

}
