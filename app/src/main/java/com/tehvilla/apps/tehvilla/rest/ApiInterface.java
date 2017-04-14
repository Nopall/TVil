package com.tehvilla.apps.tehvilla.rest;

import android.net.Uri;

import com.tehvilla.apps.tehvilla.models.ApiBantuan;
import com.tehvilla.apps.tehvilla.models.ApiCheckoutCod;
import com.tehvilla.apps.tehvilla.models.ApiCheckoutTf;
import com.tehvilla.apps.tehvilla.models.ApiDetailPengguna;
import com.tehvilla.apps.tehvilla.models.ApiLogin;
import com.tehvilla.apps.tehvilla.models.ApiLogout;
import com.tehvilla.apps.tehvilla.models.ApiMember;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.ApiPassword;
import com.tehvilla.apps.tehvilla.models.ApiPesananSaya;
import com.tehvilla.apps.tehvilla.models.ApiProdukSorting;
import com.tehvilla.apps.tehvilla.models.ApiProvinsi;
import com.tehvilla.apps.tehvilla.models.ApiRegister;
import com.tehvilla.apps.tehvilla.models.ApiSlider;
import com.tehvilla.apps.tehvilla.models.ApiTentangKami;
import com.tehvilla.apps.tehvilla.models.ApiTotal;
import com.tehvilla.apps.tehvilla.models.ApiVerifikasi;
import com.tehvilla.apps.tehvilla.models.Bantuan;
import com.tehvilla.apps.tehvilla.models.BantuanInsert;
import com.tehvilla.apps.tehvilla.models.EditPengguna;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.tehvilla.apps.tehvilla.models.Response;
import com.tehvilla.apps.tehvilla.models.TentangKami;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("Home/get_pengguna")
    Call<ApiMember> getPengguna(@Field("api_key") String api_key, @Field("kode_pengguna") String kode_pengguna,
                                @Field("last_token") String token, @Field("player_id") String player_id);

    @FormUrlEncoded
    @POST("Home/home_get_slider")
    Call<ApiSlider> getSliderHome(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("Produk/get")
    Call<ApiMessages> getPostAllProduk(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("Produk/get_total_produk")
    Call<ApiTotal> getCountProduk(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("Home/get_produk")
    Call<ApiMessages> getPostLimitProduk(@Field("api_key") String api_key, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("Produk/get")
    Call<ApiMessages> getPostLimitAwalProduk(@Field("api_key") String api_key, @Field("limit_mulai") int limit);

    @FormUrlEncoded
    @POST("Bantuan/get")
    Call<ApiBantuan> getBantuan(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("TentangKami/get")
    Call<ApiTentangKami> getTentangKami(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("Pengguna/get_provinces")
    Call<ApiProvinsi> getProvinsi(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("Pengguna/get_regencies")
    Call<ApiProvinsi> getKota(@Field("api_key") String api_key, @Field("province_id") String province_id);

    @FormUrlEncoded
    @POST("Bantuan/insert")
    Call<BantuanInsert> getResponseBantuanInsert(@Field("api_key") String api_key, @Field("nama") String nama, @Field("telepon") String telepon, @Field("email") String email, @Field("subyek") String subyek, @Field("pesan") String pesan);
//
    @Multipart
    @POST("Pengguna/edit")
    Call<EditPengguna> setInsertEditProfil(@PartMap Map<String, RequestBody> params);

//    @FormUrlEncoded
//    @POST("Pengguna/edit")
//    Call<BantuanInsert> setInsertEditProfil(@Field("kode_pengguna") String kode_pengguna, @Field("last_token") String last_token, @Field("api_key") String api_key, @Field("nama") String nama, @Field("jenis_kelamin") String jenis_kelamin, @Field("tanggal_lahir") String tanggal_lahir, @Field("alamat") String alamat, @Field("provinsi") int provinsi, @Field("kota") int kota, @Field("kode_pos") String kode_pos, @Field("telepon") String telepon, @Field("email") String email, @Field("image") MultipartBody.Part url_gambar);

    @FormUrlEncoded
    @POST("Pengguna/edit_password")
    Call<ApiPassword> setInsertEditPassword(@Field("kode_pengguna") String kode_pengguna, @Field("last_token") String last_token, @Field("api_key") String api_key, @Field("old_password") String old_password, @Field("new_password1") String new_password1, @Field("new_password2") String new_password2);

    @FormUrlEncoded
    @POST("Pengguna/register")
    Call<ApiRegister> setInsertRegister(@Field("api_key") String api_key, @Field("register_type") int register_type, @Field("nama_lengkap") String nama_lengkap, @Field("jenis_kelamin") String jenis_kelamin, @Field("tanggal_lahir") String tanggal_lahir, @Field("telepon") String telepon, @Field("alamat") String alamat, @Field("provinsi") String provinsi, @Field("kota") String kota, @Field("password1") String password1, @Field("password2") String password2, @Field("player_id") String player_id);

    @FormUrlEncoded
    @POST("Pengguna/verifikasi")
    Call<ApiVerifikasi> setVerifikasi(@Field("api_key") String api_key, @Field("kode_pengguna") String kode_pengguna, @Field("kode_verifikasi") String kode_verifikasi);

    @FormUrlEncoded
    @POST("Pengguna/request_new_verification")
    Call<ApiVerifikasi> setRequestVerifikasi(@Field("api_key") String api_key, @Field("kode_pengguna") String kode_pengguna);


    @FormUrlEncoded
    @POST("Produk/get")
    Call<ApiMessages> getProdukSorting(@Field("api_key") String api_key, @Field("limit_mulai") int limit, @Field("orderby_key") String orderby_key);

    @FormUrlEncoded
    @POST("Produk/get")
        Call<ApiMessages> getProdukSortingSearch(@Field("api_key") String api_key, @Field("limit_mulai") int limit, @Field("search_key") String search_key);

    @FormUrlEncoded
    @POST("Produk/get_detail")
    Call<ApiMessages> getDetailProduk(@Field("api_key") String api_key, @Field("kode_produk") String kode_produk);

    @FormUrlEncoded
    @POST("Pengguna/get_detail")
    Call<ApiDetailPengguna> getDetailPengguna(@Field("kode_pengguna") String kode_pengguna, @Field("last_token") String last_token, @Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("Pemesanan/check_pemesanan_expired")
    Call<BantuanInsert> getReminder(@Field("kode_pengguna") String kode_pengguna, @Field("last_token") String last_token, @Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("Pemesanan/insert")
    Call<ApiCheckoutCod> setInsertCheckoutCod(@Field("api_key") String api_key,
                                              @Field("kode_pengguna") String kode_pengguna,
                                              @Field("last_token") String last_token,
                                              @Field("kategori_pembayaran") String kategori_pembayaran,
                                              @Field("nama_lengkap") String nama_lengkap,
                                              @Field("alamat_lengkap") String alamat_lengkap,
                                              @Field("provinsi") int provinsi, @Field("kota") int kota,
                                              @Field("kode_pos") String kode_pos,
                                              @Field("nomor_telepon") String nomor_telepon,
                                              @Field("list_produk") JSONArray list_produk,
                                              @Field("total_harga") int total_harga,
                                              @Field("tanggal_pengiriman") String tanggal_pengiriman,
                                              @Field("waktu_pengiriman") String waktu_pengiriman);

    @FormUrlEncoded
    @POST("Pemesanan/insert")
    Call<ApiCheckoutTf> setInsertCheckoutTf(@Field("api_key") String api_key,
                                            @Field("kode_pengguna") String kode_pengguna, @Field("last_token") String last_token,
                                            @Field("kategori_pembayaran") String kategori_pembayaran,
                                            @Field("nama_lengkap") String nama_lengkap, @Field("alamat_lengkap") String alamat_lengkap,
                                            @Field("provinsi") int provinsi, @Field("kota") int kota,
                                            @Field("kode_pos") String kode_pos, @Field("nomor_telepon") String nomor_telepon,
                                            @Field("list_produk") JSONArray list_produk, @Field("total_harga") int total_harga,
                                            @Field("waktu_pengiriman") String waktu_pengiriman);

    @Multipart
    @POST("Pemesanan/confirm_payment")
    Call<BantuanInsert> setInsertKonfirmasi(@PartMap Map<String,RequestBody> params);

    @FormUrlEncoded
    @POST("Pengguna/login")
    Call<ApiLogin> setLoginTelepon(@Field("api_key") String api_key, @Field("login_type") int login_type, @Field("telepon") String telepon, @Field("password") String password, @Field("player_id") String player_id);

    @FormUrlEncoded
    @POST("Pengguna/login")
    Call<ApiLogin> setLoginGoogle(@Field("api_key") String api_key, @Field("login_type") int login_type, @Field("email") String email, @Field("firstname") String firstname, @Field("lastname") String lastname, @Field("jenis_kelamin") String jenis_kelamin, @Field("tanggal_lahir") Date tanggal_lahir, @Field("player_id") String player_id);

    @FormUrlEncoded
    @POST("Pengguna/test_update_player_id")
    Call<BantuanInsert> UpdatesIds(@Field("api_key") String api_key, @Field("kode_pengguna") String kode_pengguna, @Field("player_id") String player_id);

    @FormUrlEncoded
    @POST("Pemesanan/get_history_pemesanan")
    Call<ApiPesananSaya> discoverPesanan(@Field("api_key") String apikey,
                                         @Field("kode_pengguna") String kd,
                                         @Field("last_token") String token
    );

    @FormUrlEncoded
    @POST("Pengguna/logout")
    Call<ApiLogout> logout(@Field("api_key") String apikey,
                           @Field("kode_pengguna") String kd,
                           @Field("last_token") String token);
}
