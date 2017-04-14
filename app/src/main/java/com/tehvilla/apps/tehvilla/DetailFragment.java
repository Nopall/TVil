package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.models.ApiMember;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.Keranjang;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
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

public class DetailFragment extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{
    View rootView;
    int jumlahBarang=1;
    @BindView(R.id.sliderDetail) SliderLayout sliderDetail;
    @BindView(R.id.btn_Tambah) ImageButton btnTambah;
    @BindView(R.id.btn_Kurang) ImageButton btnKurang;
    @BindView(R.id.dJudul) TextView dJudul;
    @BindView(R.id.dHarga) TextView dHarga;
    @BindView(R.id.dHargaAkhir) TextView dHargaAkhir;
    @BindView(R.id.dDeskripsi) TextView dDeskripsi;
    @BindView(R.id.tambahKeKeranjang) Button keKeranjang;
    @BindView(R.id.btnBeli) Button btnBeli;
    @BindView(R.id.View_garis) View garis;
    @BindView(R.id.tvDiskon) TextView tvDiskon;
    @BindView(R.id.kembali) ImageButton btnKembali;
    @BindView(R.id.progressActivity23)
    ProgressRelativeLayout progressRelativeLayout23;
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    String judul,harga,gambar,kKode;
    int jumlahput=1;
    Realm realm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);
        ButterKnife.bind(this);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        setupJsonDetail();

        if (jumlahBarang == 1){
            btnKurang.setVisibility(View.INVISIBLE);
        }

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupJsonDetail() {
        progressRelativeLayout23.showLoading();
        Bundle kode = getIntent().getExtras();
        String pKode = kode.getString("kode_produk");
        ApiClient.getClient().create(ApiInterface.class).getDetailProduk(api_key, pKode).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
                try {
                    if (response.body().getResult()){
                        progressRelativeLayout23.showContent();
//                    Toast.makeText(getContext(),kodeSorting,Toast.LENGTH_LONG).show();
                        List<Produk> pds = response.body().getResponse();
                        for(int i = 0; i<pds.size();i++){
                            DecimalFormat df= new DecimalFormat("#,##0");
                            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
//                        url_link.add(i,listSlider.get(i).getUrl_gambar());
                            DefaultSliderView slide1 = new DefaultSliderView(getApplicationContext());
                            DefaultSliderView slide2 = new DefaultSliderView(getApplicationContext());
                            DefaultSliderView slide3 = new DefaultSliderView(getApplicationContext());
                            // initialize a SliderLayout
//                        Toast.makeText(getContext(),""+response.body().getResult()+pds.size()+"",Toast.LENGTH_LONG).show();
                            slide1
                                    .image(pds.get(i).getGambar1());
                            slide2
                                    .image(pds.get(i).getGambar2());
                            slide3
                                    .image(pds.get(i).getGambar3());
                            if (pds.get(i).getGambar2()==null){
                                sliderDetail.addSlider(slide1);
                            }
                            else if (pds.get(i).getGambar3()==null){
                                sliderDetail.addSlider(slide1);
                                sliderDetail.addSlider(slide2);
                            }else {
                                sliderDetail.addSlider(slide1);
                                sliderDetail.addSlider(slide2);
                                sliderDetail.addSlider(slide3);
                            }

                            dJudul.setText(pds.get(i).getNama());
                            dHarga.setText(df.format(Double.parseDouble(pds.get(i).getHargaAwal())));
//                        Toast.makeText(getContext(),pds.get(i).getHargaAwal(),Toast.LENGTH_LONG).show();
                            dDeskripsi.setText(Html.fromHtml(pds.get(i).getDeskripsi()));

                            judul = pds.get(i).getNama();
                            gambar = pds.get(i).getGambar1();
                            kKode = pds.get(i).getKode();

                            if (pds.get(i).getDiskon().equals("0")){
                                dHarga.setText("RP. "+df.format(Double.parseDouble(pds.get(i).getHargaAwal())));
                                tvDiskon.setVisibility(View.INVISIBLE);
                                garis.setVisibility(View.INVISIBLE);
                                harga = pds.get(i).getHargaAwal();
                            }else {
                                dHarga.setText("RP. "+df.format(Double.parseDouble(pds.get(i).getHargaAwal())));
                                dHargaAkhir.setText("RP. "+df.format(Double.parseDouble(pds.get(i).getHargaAkhir())));
                                garis.setVisibility(View.VISIBLE);
                                harga = pds.get(i).getHargaAkhir();
                                tvDiskon.setText("DISCOUNT "+pds.get(i).getDiskon()+"%");
                            }

//                        Toast.makeText(getContext(),"BISA 2 BOSS",Toast.LENGTH_LONG).show();
                        }
                        sliderDetail.setCustomIndicator((PagerIndicator)findViewById(R.id.custom_indicator));
                        sliderDetail.startAutoCycle();
                    }
                }catch (Exception e){
                    Toast.makeText(DetailFragment.this, "Mohon maaf ada kendala, mohon di refresh ulang", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.tambahKeKeranjang)
    public void keKeranjang(){
        Toast.makeText(getApplicationContext(),"Barang yang anda beli sudah dimasukkan ke keranjang",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),KeranjangActivity.class);

        Bundle b = new Bundle();
        b.putString("judul",judul);
        b.putString("harga",harga);
        b.putInt("jumlah",jumlahput);
        b.putString("gambar",gambar);
        b.putString("kKode",kKode);

        i.putExtras(b);
        startActivity(i);
    }

    @OnClick(R.id.btnBeli)
    public void BtnBeli(){
        if (realm.isEmpty()){
            addRecord();
        }else {
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
            ApiClient.getClient().create(ApiInterface.class)
                    .getPengguna(api_key, Prefs.getString("Memberkode", ""), Prefs.getString("Memberlasttoken", ""), Prefs.getString("player_id", "")).enqueue(new Callback<ApiMember>() {
                @Override
                public void onResponse(Call<ApiMember> call, Response<ApiMember> response) {
                    mProgressDialog.dismiss();
                    if (!response.body().getResult()) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        Bundle b = new Bundle();
                        b.putString("ceklogin", "detailpembayaran");
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    }else {
                        deleteRecord();
                        addRecord();
                        Intent i = new Intent(getApplicationContext(), DetailPembayaranActivity.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onFailure(Call<ApiMember> call, Throwable t) {

                }
            });
        }
    }

    public void addRecord(){
        realm.beginTransaction();

        Keranjang keranjang = realm.createObject(Keranjang.class);
        keranjang.setJudul(judul);
        keranjang.setHarga(harga);
        keranjang.setGambar(gambar);
        keranjang.setKode_produk(kKode);
        keranjang.setJumlah(jumlahput);
        Prefs.putInt("totalkeranjang", Integer.parseInt(harga)*jumlahBarang);
        Prefs.putInt("totalqty", jumlahput);

        realm.commitTransaction();
    }

    public void deleteRecord(){
        RealmResults<Keranjang> results = realm.where(Keranjang.class).findAll();
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    @OnClick(R.id.btn_Tambah)
    public void Tambah(){
        jumlahBarang++;
        display(jumlahBarang);
        if (jumlahBarang >= 2){
            btnKurang.setVisibility(View.VISIBLE);
        }
    }
    @OnClick(R.id.btn_Kurang)
    public void Kurang(){
        jumlahBarang--;
        if(jumlahBarang>0){
            display(jumlahBarang);
        }
        else{
            jumlahBarang=0;
            display(jumlahBarang);
        }

        if (jumlahBarang == 1){
            btnKurang.setVisibility(View.INVISIBLE);
        }
    }
    //
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //insialisasi slider
//        DefaultSliderView slider1 = new DefaultSliderView(getActivity());
//        slider1.image(R.drawable.gambarslide1);
//        DefaultSliderView slider2 = new DefaultSliderView(getActivity());
//        slider2.image(R.drawable.gambarslide1);
//        DefaultSliderView slider3 = new DefaultSliderView(getActivity());
//        slider3.image(R.drawable.gambarslide1);
//        sliderDetail.setCustomIndicator((PagerIndicator) rootView.findViewById(R.id.custom_indicator));
//        sliderDetail.addSlider(slider1);
//        sliderDetail.addSlider(slider2);
//        sliderDetail.addSlider(slider3);
//        sliderDetail.startAutoCycle();
//
//    }
    @Override
    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(getContext(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }
    public void display(int number){
        TextView viewer=(TextView) findViewById(R.id.tvJumlah);
        viewer.setText(""+ number);
        jumlahput = number;
    }
}