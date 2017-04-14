package com.tehvilla.apps.tehvilla.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkhmadNaufal on 2/10/17.
 */
public class History {
    @SerializedName("kode_pemesanan")
    @Expose
    private String kodePemesanan;
    @SerializedName("tanggal_pemesanan")
    @Expose
    private String tanggalPemesanan;
    @SerializedName("kode_kategori_pembayaran")
    @Expose
    private String kodeKategoriPembayaran;
    @SerializedName("request_tanggal_pengiriman")
    @Expose
    private String requestTanggalPengiriman;
    @SerializedName("request_jam_pengiriman")
    @Expose
    private String requestJamPengiriman;
    @SerializedName("total_harga")
    @Expose
    private String totalHarga;
    @SerializedName("pemesanan_detail")
    @Expose
    private List<PemesananDetail> pemesananDetail = null;
    @SerializedName("status_pemesanan")
    @Expose
    private List<StatusPemesanan> statusPemesanan = null;

    public String getKodePemesanan() {
        return kodePemesanan;
    }

    public void setKodePemesanan(String kodePemesanan) {
        this.kodePemesanan = kodePemesanan;
    }

    public String getTanggalPemesanan() {
        return tanggalPemesanan;
    }

    public void setTanggalPemesanan(String tanggalPemesanan) {
        this.tanggalPemesanan = tanggalPemesanan;
    }

    public String getKodeKategoriPembayaran() {
        return kodeKategoriPembayaran;
    }

    public void setKodeKategoriPembayaran(String kodeKategoriPembayaran) {
        this.kodeKategoriPembayaran = kodeKategoriPembayaran;
    }

    public String getRequestTanggalPengiriman() {
        return requestTanggalPengiriman;
    }

    public void setRequestTanggalPengiriman(String requestTanggalPengiriman) {
        this.requestTanggalPengiriman = requestTanggalPengiriman;
    }

    public String getRequestJamPengiriman() {
        return requestJamPengiriman;
    }

    public void setRequestJamPengiriman(String requestJamPengiriman) {
        this.requestJamPengiriman = requestJamPengiriman;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }

    public List<PemesananDetail> getPemesananDetail() {
        return pemesananDetail;
    }

    public void setPemesananDetail(List<PemesananDetail> pemesananDetail) {
        this.pemesananDetail = pemesananDetail;
    }

    public List<StatusPemesanan> getStatusPemesanan() {
        return statusPemesanan;
    }

    public void setStatusPemesanan(List<StatusPemesanan> statusPemesanan) {
        this.statusPemesanan = statusPemesanan;
    }
}

