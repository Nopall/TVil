package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SahabatDeveloper on 1/25/2017.
 */

public class CheckoutCod {
    @SerializedName("kode_pemesanan")
    @Expose
    private String kodePemesanan;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;
    @SerializedName("alamat_lengkap")
    @Expose
    private String alamatLengkap;
    @SerializedName("provinsi")
    @Expose
    private String provinsi;
    @SerializedName("kota")
    @Expose
    private String kota;
    @SerializedName("kode_pos")
    @Expose
    private String kodePos;
    @SerializedName("nomor_telepon")
    @Expose
    private String nomorTelepon;
    @SerializedName("total_harga")
    @Expose
    private String totalHarga;
    @SerializedName("kategori_pembayaran")
    @Expose
    private String kategoriPembayaran;
    @SerializedName("pemesanan_detail")
    @Expose
    private List<PemesananDetail> pemesananDetail = null;

    public String getKodePemesanan() {
        return kodePemesanan;
    }

    public void setKodePemesanan(String kodePemesanan) {
        this.kodePemesanan = kodePemesanan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public void setAlamatLengkap(String alamatLengkap) {
        this.alamatLengkap = alamatLengkap;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getKategoriPembayaran() {
        return kategoriPembayaran;
    }

    public void setKategoriPembayaran(String kategoriPembayaran) {
        this.kategoriPembayaran = kategoriPembayaran;
    }

    public List<PemesananDetail> getPemesananDetail() {
        return pemesananDetail;
    }

    public void setPemesananDetail(List<PemesananDetail> pemesananDetail) {
        this.pemesananDetail = pemesananDetail;
    }
}
