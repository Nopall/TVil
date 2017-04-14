package com.tehvilla.apps.tehvilla;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.tehvilla.apps.tehvilla.models.ApiProvinsi;
import com.tehvilla.apps.tehvilla.models.Provinsi;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailPembayaranActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.btnKembali) ImageButton btnKembali;
    @BindView(R.id.btnLanjutAlamat) Button btnLanjutAlamat;
    //Edit Text
    @BindView(R.id.etNama) EditText etNama;
    @BindView(R.id.etAlamat) EditText etAlamat;
    @BindView(R.id.etPos) EditText etPos;
    @BindView(R.id.etTelepon) EditText etTelepon;
    //Spinner
    @BindView(R.id.spnProvinsi) Spinner spnProvinsi;
    @BindView(R.id.spnKota) Spinner spnKota;

    @BindView(R.id.alamatLain) LinearLayout alamatLain;


    String provinceId = Prefs.getString("Memberprovinsi","");
    String cityId = Prefs.getString("Memberkota","");
    RadioGroup radioGroup;
    RadioButton rButton;
    int kondisi;
    int position_prov;
    int position_kota;
    final String sPos = Prefs.getString("Memberprovinsi","");
    int a = 0;
    int status = 0;
    String[] aProvinsi,aKota,aKode,aKodeKota;
    int posisikota = 36;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat);

        ButterKnife.bind(this);

        //Setting Default Font menjadi Calibri Regular
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/calibri_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Toast.makeText(this, Prefs.getString("Memberprovinsi", ""), Toast.LENGTH_SHORT).show();

        etTelepon.setText("+62");
        Selection.setSelection(etTelepon.getText(), etTelepon.getText().length());
        etTelepon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().contains("+62")){
                    etTelepon.setText("+62");
                    Selection.setSelection(etTelepon.getText(), etTelepon.getText().length());
                }

            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rButton=(RadioButton)findViewById(checkedId);
//                Toast.makeText(DetailPembayaranActivity.this,rButton.getText().toString(),Toast.LENGTH_SHORT).show();
                if(rButton.getText().toString().equals("Alamat yang sudah terdaftar di akun")){
                    etNama.setText(Prefs.getString("Membernama",""));
                    etAlamat.setText(Prefs.getString("Memberalamat",""));
                    etTelepon.setText(Prefs.getString("Membertelepon",""));
                    Selection.setSelection(etTelepon.getText(), etTelepon.getText().length());
                    etTelepon.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (!editable.toString().contains("+62")){
                                etTelepon.setText("+62");
                                Selection.setSelection(etTelepon.getText(), etTelepon.getText().length());
                            }

                        }
                    });
                    etPos.setText(Prefs.getString("Memberkodepos",""));
                    provinceId=Prefs.getString("Memberprovinsi","");
                    cityId=Prefs.getString("Memberkota","");

                    position_prov = ((Integer.parseInt(provinceId)))-11;
                    position_kota = ((Integer.parseInt(cityId))%100)-1;

                    Toast.makeText(DetailPembayaranActivity.this, ""+cityId, Toast.LENGTH_SHORT).show();


                    Toast.makeText(DetailPembayaranActivity.this, ""+position_kota, Toast.LENGTH_SHORT).show();

                    status=1;
                    setupJsonProvinsiUser();
                    setupJsonKotaUser();
                    kondisi = 0;
                }else if(rButton.getText().toString().equals("Alamat Lain")){
                    etNama.setText("");
                    etAlamat.setText("");
                    etTelepon.setText("+62");
                    Selection.setSelection(etTelepon.getText(), etTelepon.getText().length());
                    etTelepon.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (!editable.toString().contains("+62")){
                                etTelepon.setText("+62");
                                Selection.setSelection(etTelepon.getText(), etTelepon.getText().length());
                            }

                        }
                    });
                    etPos.setText("");
                    setupJsonProvinsi();
                    setupJsonKota();
                    kondisi = 1;
                }
            }
        });

        setupJsonProvinsi();

        aKode = new String[34];
    }



    @OnClick(R.id.btnKembali)
    public void SaatKembali() {
        // TODO submit data to server...
//        Intent i = new Intent(DetailPembayaranActivity.this, KeranjangActivity.class);
//        Bundle b = new Bundle();
//        b.putString("judul", "00");
//        i.putExtras(b);
//        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnLanjutAlamat)
    public void LanjutAlamat(){
        String nama = etNama.getText().toString();
        String alamat = etAlamat.getText().toString();
        String pos = etPos.getText().toString();
        String telepon = etTelepon.getText().toString();
        if (nama.equals("")){
            etNama.setError("Nama Lengkap Harap diisi");

        }else if (alamat.equals("")){
            etAlamat.setError("Alamat Lengkap Harap diisi");

        }else if (pos.equals("")){
            etPos.setError("Kode Pos Harap diisi");

        }else if (telepon.equals("")){
            etTelepon.setError("Telepon Harap diisi");

        }

        if(nama.equals("") == false && telepon.equals("") == false && alamat.equals("") == false && pos.equals("") == false && telepon.equals("") == false) {

            if (kondisi == 0) {
                Prefs.putString("pNama", nama);
                Prefs.putString("pAlamat", alamat);
                Prefs.putString("pPos", pos);
                Prefs.putString("pTelepon", telepon);
                Prefs.putString("pProvinceId", provinceId);
                Prefs.putString("pCityId", cityId);
            } else if (kondisi == 1) {
                Prefs.putString("pNama", nama);
                Prefs.putString("pAlamat", alamat);
                Prefs.putString("pPos", pos);
                Prefs.putString("pTelepon", telepon);
                Prefs.putString("pProvinceId", provinceId);
                Prefs.putString("pCityId", cityId);
            }
            Intent i = new Intent(DetailPembayaranActivity.this, DetailPengirimanActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setupJsonProvinsi() {
        ApiClient.getClient().create(ApiInterface.class).getProvinsi("7a2594cd-d6ff-440f-a950-f605342f55e4").enqueue(new Callback<ApiProvinsi>() {
            @Override
            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
                Log.v("Tos", String.valueOf(response.body().getResult()));
                if (response.body().getResult()){
                    List<Provinsi> listProv = response.body().getResponse();
                    aProvinsi = new String[listProv.size()];
                    for (int i = 0; i < listProv.size(); i++) {
                        aKode[i] = listProv.get(i).getId();
                        aProvinsi[i] = listProv.get(i).getName();

                        Prefs.putString("detailprovinsi",aProvinsi[i]);
                    }
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(DetailPembayaranActivity.this, R.layout.list_item_spinner,aProvinsi);
                    //setting adapter to spinner
                    spnProvinsi.setAdapter(adapter);

                    spnProvinsi.setSelection(14);
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(DetailPembayaranActivity.this,"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
            }
        });
        spnProvinsi.setOnItemSelectedListener(this);
//        Toast.makeText(DetailPembayaranActivity.this,"Mulai",Toast.LENGTH_LONG).show();
    }

//    private void setupJsonProvinsiUser() {
//        final ArrayList<String[]> _ids = new ArrayList<>();
//        ApiClient.getClient().create(ApiInterface.class).getProvinsi("7a2594cd-d6ff-440f-a950-f605342f55e4").enqueue(new Callback<ApiProvinsi>() {
//            @Override
//            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
//                Log.v("Tos", String.valueOf(response.body().getResult()));
//                if (response.body().getResult()){
//                    List<Provinsi> listProv = response.body().getResponse();
//                    ArrayAdapter<String> adapter;
//                    aProvinsi = new String[listProv.size()];
//                    for (int i = 0; i < listProv.size(); i++) {
//                        aKode[i] = listProv.get(i).getId();
//                        aProvinsi[i] = listProv.get(i).getName();
//                        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_spinner,aProvinsi);
//                        //setting adapter to spinner
//                        spnProvinsi.setAdapter(adapter);
//                            if(aKode[i].equals(Prefs.getString("Memberprovinsi",""))){
////                                spnProvinsi.setSelection(aProvinsi[i]);
//                                Prefs.putString("detailprovinsi",aProvinsi[i]);
//                                int spinnerPosition = adapter.getPosition(String.valueOf(aProvinsi));
//                                Log.d("spinner_set_selection", aProvinsi[i] + spinnerPosition);
//                                spnProvinsi.setSelection(spinnerPosition);
//                            }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
//                Toast.makeText(DetailPembayaranActivity.this,"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
//            }
//        });
//        spnProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                provinceId = String.valueOf(_ids.get(i));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    private void setupJsonKota() {
        ApiClient.getClient().create(ApiInterface.class).getKota("7a2594cd-d6ff-440f-a950-f605342f55e4",provinceId).enqueue(new Callback<ApiProvinsi>() {
            @Override
            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
                Log.v("Tos", String.valueOf(response.body().getResult()));
                if (response.body().getResult()){
                    List<Provinsi> listKota = response.body().getResponse();
                    aKota = new String[listKota.size()];
                    aKodeKota = new String[listKota.size()];
                    for(int i = 0; i<listKota.size();i++){
                        aKota[i]=listKota.get(i).getName();
                        aKodeKota[i] = listKota.get(i).getId();

                        Prefs.putString("detailkota",aKota[i]);
                    }
                }
                spnKota.setAdapter(new ArrayAdapter<String>(DetailPembayaranActivity.this,
                        R.layout.list_item_spinner, aKota));

                if (!provinceId.equals("25")){
                    spnKota.setSelection(0);
                }else {
                    spnKota.setSelection(36);
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(DetailPembayaranActivity.this,"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
            }

        });

        spnKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                cityId = aKodeKota[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setupJsonProvinsiUser() {
        ApiClient.getClient().create(ApiInterface.class).getProvinsi("7a2594cd-d6ff-440f-a950-f605342f55e4").enqueue(new Callback<ApiProvinsi>() {
            @Override
            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
                try {
                    if (response.body().getResult()){
                        List<Provinsi> listProv = response.body().getResponse();
                        aProvinsi = new String[listProv.size()];
                        for (int i = 0; i < listProv.size(); i++) {
                            aKode[i] = listProv.get(i).getId();
                            aProvinsi[i] = listProv.get(i).getName();
                        }


                        //Toast.makeText(getContext(),"a"+position_kota,Toast.LENGTH_LONG).show();
                        ArrayAdapter<String> adapter;
                        adapter = new ArrayAdapter<String>(DetailPembayaranActivity.this, R.layout.list_item_spinner,aProvinsi);
                        //setting adapter to spinner
                        spnProvinsi.setAdapter(adapter);
                        if(status==1)
                            spnProvinsi.setSelection(position_prov);

                    }
                }catch (Exception e){
                    Toast.makeText(DetailPembayaranActivity.this, "Mohon Maaf ada kendala, coba beberapa saat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(DetailPembayaranActivity.this,"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
            }
        });
        spnProvinsi.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i = new Intent(DetailPembayaranActivity.this, KeranjangActivity.class);
//        Bundle b = new Bundle();
//        b.putString("judul", "00");
//        i.putExtras(b);
//        startActivity(i);
        finish();
    }

    private void setupJsonKotaUser() {
        ApiClient.getClient().create(ApiInterface.class).getKota("7a2594cd-d6ff-440f-a950-f605342f55e4",provinceId).enqueue(new Callback<ApiProvinsi>() {
            @Override
            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
                try {
                    if (response.body().getResult()){
                        List<Provinsi> listKota = response.body().getResponse();
                        aKota = new String[listKota.size()];
                        aKodeKota = new String[listKota.size()];
                        for(int i = 0; i<listKota.size();i++){
                            aKota[i]=listKota.get(i).getName();
                            aKodeKota[i] = listKota.get(i).getId();
                        }
                    }

                    spnKota.setAdapter(new ArrayAdapter<String>(DetailPembayaranActivity.this,
                            R.layout.list_item_spinner, aKota));
                    if(status==1 && sPos.equals(provinceId)==false){
                        Toast.makeText(DetailPembayaranActivity.this, "1"+position_kota, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(DetailPembayaranActivity.this, "1"+provinceId, Toast.LENGTH_SHORT).show();
                        spnKota.setSelection(position_kota);
                        a+=position_prov;
                    }else{
                        Toast.makeText(DetailPembayaranActivity.this, "0"+position_kota, Toast.LENGTH_SHORT).show();
                        spnKota.setSelection(position_kota);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(DetailPembayaranActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(DetailPembayaranActivity.this,"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
            }

        });

        spnKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                cityId = aKodeKota[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(status==1){
            provinceId = aKode[position];
            setupJsonKotaUser();
            status = 0;
        }else{
            provinceId = aKode[position];
            setupJsonKota();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}