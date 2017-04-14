package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tehvilla.apps.tehvilla.adapters.UlasanAdapter;
import com.tehvilla.apps.tehvilla.helpers.Helpers;
import com.tehvilla.apps.tehvilla.models.ApiCheckoutCod;
import com.tehvilla.apps.tehvilla.models.ApiCheckoutTf;
import com.tehvilla.apps.tehvilla.models.ApiProvinsi;
import com.tehvilla.apps.tehvilla.models.CheckoutCod;
import com.tehvilla.apps.tehvilla.models.CheckoutTf;
import com.tehvilla.apps.tehvilla.models.JsonUlasan;
import com.tehvilla.apps.tehvilla.models.Keranjang;
import com.tehvilla.apps.tehvilla.models.Provinsi;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UlasanActivity extends AppCompatActivity {
    @BindView(R.id.btnSelesai) Button btnSelesai;
    @BindView(R.id.jdl_detailpesanan) TextView jdl_detailPesanan;
    @BindView(R.id.uNama) TextView uNama;
    @BindView(R.id.uAlamat) TextView uAlamat;
    @BindView(R.id.uProvinsi) TextView uProvinsi;
    @BindView(R.id.uKota) TextView uKota;
    @BindView(R.id.uPos) TextView uPos;
    @BindView(R.id.uTelepon) TextView uTelepon;
    @BindView(R.id.jenisPembayaran) TextView jenisPembayaran;
    @BindView(R.id.tvDetail1) TextView tvDetail1;
    @BindView(R.id.tvDetail2) TextView tvDetail2;
    @BindView(R.id.etDetail1) TextView isiDetail1;
    @BindView(R.id.etDetail2) TextView isiDetail2;
    @BindView(R.id.namaRekening) TextView namaRekening;
    @BindView(R.id.Total_harga) TextView totalharga;
    @BindView(R.id.halDetail3) LinearLayout halTf;
    @BindView(R.id.btnKembali) ImageButton btnKembali;
    List<Keranjang> listArray;
    List<JsonUlasan> listJson;
    Realm realm;
    JSONObject listProduk;
    JSONArray arrayProduk;
    String json;
    String[] aKode,aKodeKota,aProvinsi,aKota;
    int position_prov,position_kota;
    String tipe_bayar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ulasan);
        ButterKnife.bind(this);
        //Setting Default Font menjadi Calibri Regular
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/calibri_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        listArray = new ArrayList<>();
        //inisialisasi Realm Database
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        //inisialisasi listKeranjangbvv
        RecyclerView listUlasan = (RecyclerView)findViewById(R.id.listUlasan);
        listUlasan.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listUlasan.setLayoutManager(llm);
        viewRecord();
        UlasanAdapter ka = new UlasanAdapter(listArray, this);
        listUlasan.setAdapter(ka);

        Log.v("OT", Prefs.getString("pNama",""));

//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if(Prefs.getString("pengiriman","").equals("cod"))
//                    InsertDataCheckoutCOD();
//                else if(Prefs.getString("pengiriman","").equals("tf"))
//                    InsertDataCheckoutTF();
//            }
//        });

        position_prov = ((Integer.parseInt(Prefs.getString("pProvinceId",""))))-11;
        position_kota = ((Integer.parseInt(Prefs.getString("pCityId","")))%100)-1;

        if(Prefs.getString("pengiriman","").equals("cod"))
            InsertDataCheckoutCOD();
        else if(Prefs.getString("pengiriman","").equals("tf"))
            InsertDataCheckoutTF();
    }
    public void viewRecord(){
        RealmResults<Keranjang> results = realm.where(Keranjang.class).findAll();
        int i = 0;
        listProduk = new JSONObject();
        listJson = new ArrayList<JsonUlasan>();
        JSONObject l = new JSONObject();
        for(Keranjang keranjang : results){
            listArray.add(new Keranjang(keranjang.getKode_produk(), keranjang.getJudul(), keranjang.getHarga(), keranjang.getGambar(), keranjang.getJumlah()));
            listJson.add(new JsonUlasan(keranjang.getKode_produk(),keranjang.getJumlah()));
//          try {
//                listProduk.put("kode_produk",keranjang.getKode());
//                listProduk.put("jumlah",keranjang.getJumlah());
//                Toast.makeText(UlasanActivity.this,"id"+listArray.get(i).getKode(),Toast.LENGTH_LONG).show();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
        json = new Gson().toJson(listJson);
        try {
            arrayProduk = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DOG", "Array " + arrayProduk);
//        Toast.makeText(UlasanActivity.this,"array"+arrayProduk,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        deleteRecord();
        Intent i = new Intent(UlasanActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnKembali)
    public void SaatKembali() {
        // TODO submit data to server...
        deleteRecord();
        Intent i = new Intent(UlasanActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnSelesai)
    public void SaatSelesai() {
        // TODO submit data to server...
        Intent i = new Intent(UlasanActivity.this,SelesaiActivity.class);
        startActivity(i);
        deleteRecord();
        finish();
    }

    public void deleteRecord(){
        RealmResults<Keranjang> results = realm.where(Keranjang.class).findAll();
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void InsertDataCheckoutCOD() {
        final ProgressDialog mProgressDialog = new ProgressDialog(UlasanActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        String pNama = Prefs.getString("pNama","");
        String pAlamat = Prefs.getString("pAlamat","");
        String pPos = Prefs.getString("pPos","");
        String pTelepon = Prefs.getString("pTelepon","");
        String pProvinceId = Prefs.getString("pProvinceId","");
        String pCityId = Prefs.getString("pCityId","");
        setupJsonProvinsi();
        setupJsonKota();
        ApiClient.getClient().create(ApiInterface.class).setInsertCheckoutCod("7a2594cd-d6ff-440f-a950-f605342f55e4",
                Prefs.getString("Memberkode", ""), Prefs.getString("Memberlasttoken", ""),"COD",pNama,pAlamat,
                Integer.parseInt(pProvinceId),Integer.parseInt(pCityId),pPos,pTelepon,arrayProduk,
                Prefs.getInt("totalkeranjang",0),Prefs.getString("tanggal_cod",""), Prefs.getString("waktu_cod", "")).enqueue(new Callback<ApiCheckoutCod>() {
            @Override
            public void onResponse(Call<ApiCheckoutCod> call, Response<ApiCheckoutCod> response) {
                try {
                    if (response.body().getResult()){
                        List<CheckoutCod> rgs = response.body().getResponse();
                        Prefs.putString("IDOrder", rgs.get(0).getKodePemesanan());
                        jdl_detailPesanan.setText("Detail Pemesanan(ID "+rgs.get(0).getKodePemesanan()+")");
                        uNama.setText(rgs.get(0).getNamaLengkap());
                        uAlamat.setText(rgs.get(0).getAlamatLengkap());
                        uTelepon.setText(rgs.get(0).getNomorTelepon());
                        uPos.setText(rgs.get(0).getKodePos());
                        uProvinsi.setText(aProvinsi[position_prov]);
                        uKota.setText(aKota[position_kota]);
                        jenisPembayaran.setText("Cash On Delivery");
                        isiDetail1.setText(Prefs.getString("tanggal_cod",""));
                        isiDetail2.setText(Prefs.getString("waktu_cod",""));
                        DecimalFormat df= new DecimalFormat("#,##0");
                        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
                        totalharga.setText("RP. "+df.format(Double.parseDouble(rgs.get(0).getTotalHarga())));
                        halTf.setVisibility(View.INVISIBLE);
                        mProgressDialog.dismiss();

                    }else{
                        //Toast.makeText(UlasanActivity.this,"GAGAL RESULT = "+response.body().getResult().toString(),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(UlasanActivity.this, "Maaf ada kendala, Pesanan anda sudah masuk, silahkan cek Menu Pesanan Saya", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UlasanActivity.this, HomeActivity.class);
                    Prefs.putString("kodeintenthome","pesanansaya");
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<ApiCheckoutCod> call, Throwable t) {
                Toast.makeText(UlasanActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void InsertDataCheckoutTF() {
        final ProgressDialog mProgressDialog = new ProgressDialog(UlasanActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        String pNama = Prefs.getString("pNama","");
        String pAlamat = Prefs.getString("pAlamat","");
        String pPos = Prefs.getString("pPos","");
        String pTelepon = Prefs.getString("pTelepon","");
        final String pProvinceId = Prefs.getString("pProvinceId","");
        final String pCityId = Prefs.getString("pCityId","");
        setupJsonProvinsi();
        setupJsonKota();
        ApiClient.getClient().create(ApiInterface.class).setInsertCheckoutTf("7a2594cd-d6ff-440f-a950-f605342f55e4", Prefs.getString("Memberkode", ""),
                Prefs.getString("Memberlasttoken", ""),"TF", Prefs.getString("pNama",""),Prefs.getString("pAlamat",""),
                Integer.parseInt(pProvinceId),Integer.parseInt(pCityId),pPos, pTelepon,arrayProduk,Prefs.getInt("totalkeranjang",0),"-").enqueue(new Callback<ApiCheckoutTf>() {
            @Override
            public void onResponse(Call<ApiCheckoutTf> call, Response<ApiCheckoutTf> response) {
                try {
                    if (response.body().getResult()){
                        List<CheckoutTf> rgs = response.body().getResponse();
                        Prefs.putString("IDOrder", rgs.get(0).getKodePemesanan());
                        jdl_detailPesanan.setText("Detail Pemesanan(ID "+rgs.get(0).getKodePemesanan()+")");
                        uNama.setText(rgs.get(0).getNamaLengkap());
                        uAlamat.setText(rgs.get(0).getAlamatLengkap());
                        uTelepon.setText(rgs.get(0).getNomorTelepon());
                        uPos.setText(rgs.get(0).getKodePos());
                        uProvinsi.setText(aProvinsi[position_prov]);
                        uKota.setText(aKota[position_kota]);
                        jenisPembayaran.setText("Transfer");
                        tvDetail1.setText("Jenis Bank");
                        tvDetail2.setText("No. Rekening");
                        isiDetail1.setText(rgs.get(0).getJenisBank());
                        isiDetail2.setText(rgs.get(0).getNoRekening());
                        halTf.setVisibility(View.VISIBLE);
                        namaRekening.setText(rgs.get(0).getNamaRekening());
                        DecimalFormat df= new DecimalFormat("#,##0");
                        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
                        totalharga.setText("RP. "+df.format(Double.parseDouble(rgs.get(0).getTotalHarga())));
                        mProgressDialog.dismiss();
                    }else{
//                    Toast.makeText(UlasanActivity.this,"GAGAL RESULT = "+response.body().getResult().toString(),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(UlasanActivity.this, "Maaf ada kendala, Pesanan anda sudah masuk, silahkan cek Menu Pesanan Saya", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UlasanActivity.this, HomeActivity.class);
                    Prefs.putString("kodeintenthome","pesanansaya");
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<ApiCheckoutTf> call, Throwable t) {
                Toast.makeText(UlasanActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupJsonProvinsi() {
        ApiClient.getClient().create(ApiInterface.class).getProvinsi("7a2594cd-d6ff-440f-a950-f605342f55e4").enqueue(new Callback<ApiProvinsi>() {
            @Override
            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
//                Log.v("Tos", String.valueOf(response.body().getResult()));
                try{
                    if (response.body().getResult()) {
                        List<Provinsi> listProv = response.body().getResponse();
                        aProvinsi = new String[listProv.size()];
                        aKode = new String[34];
                        for (int i = 0; i < listProv.size(); i++) {
                            aKode[i] = listProv.get(i).getId();
                            aProvinsi[i] = listProv.get(i).getName();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(UlasanActivity.this, "Maaf ada kendala, Pesanan anda sudah masuk, silahkan cek Menu Pesanan Saya", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UlasanActivity.this, HomeActivity.class);
                    Prefs.putString("kodeintenthome","pesanansaya");
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(UlasanActivity.this, "Harap Periksa Koneksi Anda", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupJsonKota() {
        ApiClient.getClient().create(ApiInterface.class).getKota("7a2594cd-d6ff-440f-a950-f605342f55e4", Prefs.getString("pProvinceId","")).enqueue(new Callback<ApiProvinsi>() {
            @Override
            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
                try {
                    List<Provinsi> listKota = response.body().getResponse();
                    aKota = new String[listKota.size()];
                    aKodeKota = new String[listKota.size()];
                    for (int i = 0; i < listKota.size(); i++) {
                        aKota[i] = listKota.get(i).getName();
                        aKodeKota[i] = listKota.get(i).getId();
                    }
                }catch (Exception e){
                    Toast.makeText(UlasanActivity.this, "Maaf ada kendala, Pesanan anda sudah masuk, silahkan cek Menu Pesanan Saya", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UlasanActivity.this, HomeActivity.class);
                    Prefs.putString("kodeintenthome","pesanansaya");
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(UlasanActivity.this, "Harap Periksa Koneksi Anda", Toast.LENGTH_LONG).show();
            }

        });
    }
}