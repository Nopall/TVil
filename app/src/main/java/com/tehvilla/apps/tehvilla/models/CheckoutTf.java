package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SahabatDeveloper on 2/7/2017.
 */

public class CheckoutTf {
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
    @SerializedName("jenis_bank")
    @Expose
    private String jenisBank;
    @SerializedName("no_rekening")
    @Expose
    private String noRekening;
    @SerializedName("nama_rekening")
    @Expose
    private String namaRekening;
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

    public String getJenisBank() {
        return jenisBank;
    }

    public void setJenisBank(String jenisBank) {
        this.jenisBank = jenisBank;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getNamaRekening() {
        return namaRekening;
    }

    public void setNamaRekening(String namaRekening) {
        this.namaRekening = namaRekening;
    }

    public List<PemesananDetail> getPemesananDetail() {
        return pemesananDetail;
    }

    public void setPemesananDetail(List<PemesananDetail> pemesananDetail) {
        this.pemesananDetail = pemesananDetail;
    }
}
