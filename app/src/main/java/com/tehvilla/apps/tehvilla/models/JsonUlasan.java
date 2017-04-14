package com.tehvilla.apps.tehvilla.models;

/**
 * Created by AkhmadNaufal on 2/21/17.
 */
public class JsonUlasan {
    private String kode_produk;
    private int jumlah;

    public JsonUlasan(String kode_produk, int jumlah) {
        this.kode_produk = kode_produk;
        this.jumlah = jumlah;
    }

    public String getKode_produk() {
        return kode_produk;
    }

    public void setKode_produk(String kode_produk) {
        this.kode_produk = kode_produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
