package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class KeranjangFragment extends Fragment {
    @BindView(R.id.btnCheckOut) Button btnCheckOut;
    public static TextView tKeranjang;
    @BindView(R.id.Text12)TextView txt12;
    @BindView(R.id.text11)TextView txt11;
    @BindView(R.id.listKeranjang)RecyclerView listKeranjang;
    String tempJudul,tempHarga,tempGambar,tempKode,kode;
    public  static int tempJumlah;
    public  static int total=0;
    public  static int qty=0;
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    public  static List<Keranjang> listArray;
    public  static Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_keranjang, container, false);
        //inisialisasi butter kinfe
        ButterKnife.bind(this, rootView);

        tKeranjang = (TextView)rootView.findViewById(R.id.totalKeranjang);
        //inisialisasi Realm Database
        realm = Realm.getDefaultInstance();

        loadData();

        return rootView;
    }

    private void loadData() {
        listKeranjang.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listKeranjang.setLayoutManager(llm);

        listArray = new ArrayList<>();
        Bundle b = this.getArguments();
        if(b.getString("judul").toString().equals("00")){
            if (realm.isEmpty()){
                txt11.setVisibility(View.VISIBLE);
                txt12.setVisibility(View.VISIBLE);
            }else{
                txt11.setVisibility(View.INVISIBLE);
                txt12.setVisibility(View.INVISIBLE);
            }
            viewRecord();
        }
        else{
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
        KeranjangFragmentAdapter ka = new KeranjangFragmentAdapter(listArray, getActivity());
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

    public static void viewRecord(){
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
        total = 0;
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
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @OnClick(R.id.btnCheckOut)
    public void CheckOut(){
        if (realm.isEmpty()){
            Toast.makeText(getContext(), "Keranjang belanja anda masih kosong, silahkan belanja terlebih dahulu", Toast.LENGTH_SHORT).show();

        }else {
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
            ApiClient.getClient().create(ApiInterface.class)
                    .getPengguna(api_key, Prefs.getString("Memberkode", ""), Prefs.getString("Memberlasttoken", ""), Prefs.getString("player_id", "")).enqueue(new Callback<ApiMember>() {
                @Override
                public void onResponse(Call<ApiMember> call, Response<ApiMember> response) {
                    mProgressDialog.dismiss();
                    if (!response.body().getResult()) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        Bundle b = new Bundle();
                        b.putString("ceklogin", "detailpembayaran");
                        i.putExtras(b);
                        startActivity(i);
                    }else {
                        Intent i = new Intent(getActivity(), DetailPembayaranActivity.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onFailure(Call<ApiMember> call, Throwable t) {

                }
            });
        }
    }

}