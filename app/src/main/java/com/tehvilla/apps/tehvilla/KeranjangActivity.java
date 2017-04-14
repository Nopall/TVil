package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.adapters.KeranjangAdapter;
import com.tehvilla.apps.tehvilla.adapters.KeranjangFragmentAdapter;
import com.tehvilla.apps.tehvilla.models.ApiMember;
import com.tehvilla.apps.tehvilla.models.Keranjang;
import com.tehvilla.apps.tehvilla.models.Member;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.kinst.jakub.view.StatefulLayout;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeranjangActivity extends AppCompatActivity {
    @BindView(R.id.btnCheckOut) Button btnCheckOut;
    @BindView(R.id.kembali) ImageButton btnKembali;
    public static TextView tKeranjang;
    @BindView(R.id.Text12)TextView txt12;
    @BindView(R.id.text11)TextView txt11;
    String tempJudul,tempHarga,tempGambar,tempKode,kode;
    int tempJumlah;
    int total=0;
    int qty=0;
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    List<Keranjang> listArray;
    Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_keranjangbelanja);

        ButterKnife.bind(this);
        Bundle c = getIntent().getExtras();

        loadData(c.getString("judul"));
    }

    public void loadData(String judul) {
        //inisialisasi butter kinfe
        total=0;
        //inisialisasi Realm Database
        realm = Realm.getDefaultInstance();

        tKeranjang = (TextView) findViewById(R.id.totalKeranjang);

        RecyclerView listKeranjang = (RecyclerView)findViewById(R.id.listKeranjang);
        listKeranjang.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listKeranjang.setLayoutManager(llm);

        listArray = new ArrayList<>();
        if(judul.equals("00")){
            if (realm.isEmpty()){
                txt11.setVisibility(View.VISIBLE);
                txt12.setVisibility(View.VISIBLE);
            }else{
                txt11.setVisibility(View.INVISIBLE);
                txt12.setVisibility(View.INVISIBLE);
            }
            viewRecord();
        }else if(judul.equals("11")){
            Prefs.putString("kodeintenthome","ok");
            Intent i = new Intent(KeranjangActivity.this,HomeActivity.class);
            startActivity(i);
        }else if(judul.equals("22")){
            Prefs.putString("kodeintenthome","22");
            Intent i = new Intent(KeranjangActivity.this,HomeActivity.class);
            startActivity(i);
        }else if(judul.equals("33")){
            Prefs.putString("kodeintenthome","33");
            Intent i = new Intent(KeranjangActivity.this,HomeActivity.class);
            startActivity(i);
        }
        else{
            Bundle b = getIntent().getExtras();
            tempJudul = b.getString("judul").toString();
            tempHarga = b.getString("harga").toString();
            tempGambar = b.getString("gambar").toString();
            tempKode = b.getString("kKode").toString();
            tempJumlah = b.getInt("jumlah");
            kode = tempKode;
            updateRecord();
            addRecord();
            viewRecord();
        }
        KeranjangAdapter ka = new KeranjangAdapter(listArray, this);
        listKeranjang.setAdapter(ka);
    }

    public void addRecord(){
        realm.beginTransaction();

        Keranjang keranjang = realm.createObject(Keranjang.class);
        keranjang.setJudul(tempJudul);
        keranjang.setHarga(tempHarga);
        keranjang.setGambar(tempGambar);
        keranjang.setKode_produk(tempKode);
        keranjang.setJumlah(tempJumlah);

        realm.commitTransaction();
    }

    public void viewRecord(){
        RealmResults<Keranjang> results = realm.where(Keranjang.class).findAll();
        for(Keranjang keranjang : results){
            listArray.add(new Keranjang(keranjang.getKode_produk(), keranjang.getJudul(), keranjang.getHarga(), keranjang.getGambar(), keranjang.getJumlah()));
            total = total + (Integer.parseInt(keranjang.getHarga())*keranjang.getJumlah());
            qty = qty + keranjang.getJumlah();
        }
        DecimalFormat df= new DecimalFormat("#,##0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        tKeranjang.setText("Rp. "+df.format(total));
        Prefs.putInt("totalkeranjang",total);
        Prefs.putInt("totalqty", qty);
    }

    public void updateRecord(){
        RealmResults<Keranjang> results = realm.where(Keranjang.class).equalTo("kode_produk", kode).findAll();
        realm.beginTransaction();
        int j = 0;
        for(Keranjang keranjang : results){
            j = keranjang.getJumlah();
        }
        results.deleteAllFromRealm();
        tempJumlah += j;
        realm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick(R.id.btnCheckOut)
    public void CheckOut(){
        if (realm.isEmpty()){
            Toast.makeText(KeranjangActivity.this, "Keranjang belanja anda masih kosong, silahkan belanja terlebih dahulu", Toast.LENGTH_SHORT).show();

        }else {
            final ProgressDialog mProgressDialog = new ProgressDialog(KeranjangActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
            ApiClient.getClient().create(ApiInterface.class)
                    .getPengguna(api_key, Prefs.getString("Memberkode", ""), Prefs.getString("Memberlasttoken", ""), Prefs.getString("player_id", "")).enqueue(new Callback<ApiMember>() {
                @Override
                public void onResponse(Call<ApiMember> call, Response<ApiMember> response) {
                    mProgressDialog.dismiss();
                    try {
                        if (!response.body().getResult()) {
                            Intent i = new Intent(KeranjangActivity.this, LoginActivity.class);
                            Bundle b = new Bundle();
                            b.putString("ceklogin", "detailpembayaran");
                            i.putExtras(b);
                            startActivity(i);
                            finish();
                        }else {
                            Intent i = new Intent(KeranjangActivity.this, DetailPembayaranActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }catch (Exception e){
                        Toast.makeText(KeranjangActivity.this, "Mohon maaf ada kendala, mohon refresh ulang", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiMember> call, Throwable t) {

                }
            });
        }
    }
    @OnClick(R.id.kembali)
    public void Kembali(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}