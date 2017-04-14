package com.tehvilla.apps.tehvilla.models;

import io.realm.RealmObject;

/**
 * Created by SahabatDeveloper on 1/21/2017.
 */
public class Keranjang extends RealmObject{
    private int id;
    private String kode_produk;
    private String judul;
    private String harga;
    private int jumlah;
    private String gambar;

    public Keranjang(String kode_produk, String judul, String harga, String gambar, int jumlah) {
        this.kode_produk = kode_produk;
        this.judul = judul;
        this.harga = harga;
        this.gambar = gambar;
        this.jumlah = jumlah;
    }

    public Keranjang() {
    }

    public String getKode_produk() {
        return kode_produk;
    }

    public void setKode_produk(String kode_produk) {
        this.kode_produk = kode_produk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}

