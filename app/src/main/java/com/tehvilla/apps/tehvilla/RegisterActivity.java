package com.tehvilla.apps.tehvilla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.onesignal.OneSignal;
import com.tehvilla.apps.tehvilla.adapters.ProdukAdapter;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.ApiProvinsi;
import com.tehvilla.apps.tehvilla.models.ApiRegister;
import com.tehvilla.apps.tehvilla.models.ApiSlider;
import com.tehvilla.apps.tehvilla.models.BantuanInsert;
import com.tehvilla.apps.tehvilla.models.Provinsi;
import com.tehvilla.apps.tehvilla.models.Register;
import com.tehvilla.apps.tehvilla.models.Slider;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.namaLengkap) EditText namaLengkap;
    @BindView(R.id.tanggalLahir) EditText tanggalLahir;
    @BindView(R.id.spnKelamin) Spinner spnKelamin;
    @BindView(R.id.nomorTelepon) EditText nomorTelepon;
    @BindView(R.id.alamatLengkap) EditText alamatLengkap;
    @BindView(R.id.kataSandi1) EditText kataSandi1;
    @BindView(R.id.kataSandi2) EditText kataSandi2;
    @BindView(R.id.spnProvinsi) Spinner spnProvinsi;
    @BindView(R.id.spnKota) Spinner spnKota;
    String provinceId = "25";
    String cityId,gender;
    String[] aProvinsi,aKota,aKode,aKodeKota;
    String[] aKelamin = {"Laki - Laki",
                          "Perempuan"};
    private ProgressDialog mProgressDialog;
    private String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    private int mYear,mMonth,mDay;
    int posisispinner = 36;
    int posisispinnerprov = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        getplayerId();

        aKode = new String[34];
        // inisialiasi Array Adapter dengan memasukkan string array di atas
        spnKelamin.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                R.layout.list_item_spinner, aKelamin));
        spnKelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(aKelamin[position].equals("Laki - Laki")){
//                    Toast.makeText(RegisterActivity.this,aKelamin[position],Toast.LENGTH_SHORT).show();
                    gender="L";
                }else{
                    gender="P";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setupJsonProvinsi();

        tanggalLahir.setInputType(InputType.TYPE_NULL);

        tanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(RegisterActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        int month = selectedmonth + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + selectedday;

                        if(month < 10){

                            formattedMonth = "0" + month;
                        }
                        if(selectedday < 10){

                            formattedDayOfMonth = "0" + selectedday;
                        }
                        tanggalLahir.setText(selectedyear+"-"+formattedMonth+"-"+formattedDayOfMonth);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        nomorTelepon.setText("+62");
        Selection.setSelection(nomorTelepon.getText(), nomorTelepon.getText().length());
        nomorTelepon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().contains("+62")){
                    nomorTelepon.setText("+62");
                    Selection.setSelection(nomorTelepon.getText(), nomorTelepon.getText().length());
                }

            }
        });
    }

    private void setupJsonProvinsi() {
        ApiClient.getClient().create(ApiInterface.class).getProvinsi("7a2594cd-d6ff-440f-a950-f605342f55e4").enqueue(new Callback<ApiProvinsi>() {
            @Override
            public void onResponse(Call<ApiProvinsi> call, Response<ApiProvinsi> response) {
                Log.v("Tos", String.valueOf(response.body().getResult()));
                try {
                    if (response.body().getResult()){
                        List<Provinsi> listProv = response.body().getResponse();
                        aProvinsi = new String[listProv.size()];
                        for (int i = 0; i < listProv.size(); i++) {
                            aKode[i] = listProv.get(i).getId();
                            aProvinsi[i] = listProv.get(i).getName();
                        }
                        ArrayAdapter<String> adapter;
                        adapter = new ArrayAdapter<String>(RegisterActivity.this, R.layout.list_item_spinner,aProvinsi);
                        //setting adapter to spinner
                        spnProvinsi.setAdapter(adapter);

                        spnProvinsi.setSelection(14);
                    }
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
            }


        });
        spnProvinsi.setOnItemSelectedListener(this);
    }

    private void setupJsonKota() {
        ApiClient.getClient().create(ApiInterface.class).getKota("7a2594cd-d6ff-440f-a950-f605342f55e4", provinceId).enqueue(new Callback<ApiProvinsi>() {
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
                    spnKota.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                            R.layout.list_item_spinner, aKota));

                    if (!provinceId.equals("25")){
                        posisispinner = 0;
                    }

                    spnKota.setSelection(posisispinner);

                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
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

    public void getplayerId() {
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                if (!userId.equalsIgnoreCase("")) {
                    Log.d("LoginActivity", "User ID : " + userId);
                    Prefs.putString("player_id", userId);
                }
            }
        });
    }

    @OnClick(R.id.btnRegister)
    public void KlikDaftar() {

        // TODO submit data to server...
        if(namaLengkap.getText().toString().equals("")){
            namaLengkap.setBackgroundResource(R.drawable.bgkosong);
            namaLengkap.setError("Nama Lengkap Harap DIisi");
        }
        else {
            namaLengkap.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(nomorTelepon.getText().toString().equals("")){
            nomorTelepon.setBackgroundResource(R.drawable.bgkosong);
            nomorTelepon.setError("Nomor Telepon Harap diisi");
        }
        else {
            namaLengkap.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(alamatLengkap.getText().toString().equals("")){
            alamatLengkap.setBackgroundResource(R.drawable.bgkosong);
            alamatLengkap.setError("Alamat Lengkap Harap diisi");
        }
        else {
            namaLengkap.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(kataSandi1.getText().toString().equals("")){
            kataSandi1.setBackgroundResource(R.drawable.bgkosong);
            kataSandi1.setError("Kata Sandi Harap diisi");
        }
        else {
            namaLengkap.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(kataSandi2.getText().toString().equals("")){
            kataSandi2.setBackgroundResource(R.drawable.bgkosong);
            kataSandi2.setError("Kata Sandi Harap diisi");
        }
        else {
            namaLengkap.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(kataSandi1.getText().toString().equals(kataSandi2.getText().toString())){
            namaLengkap.setBackgroundResource(R.drawable.bgedittext2);
        }
        else {
            kataSandi2.setBackgroundResource(R.drawable.bgkosong);
            kataSandi2.setError("Kata Sandi Tidak Sama, Harap Diperbaiki");
        }

        if(namaLengkap.getText().toString().equals("") == false && nomorTelepon.getText().toString().equals("") == false && alamatLengkap.getText().toString().equals("") == false && kataSandi1.getText().toString().equals("") == false && kataSandi2.getText().toString().equals("") == false && kataSandi1.getText().toString().equals(kataSandi2.getText().toString()) == true){
            InsertDataRegister();

    }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        provinceId = aKode[position];
        setupJsonKota();
//        Toast.makeText(RegisterActivity.this,"sudah bisa",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void InsertDataRegister() {
        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        String nama = namaLengkap.getText().toString();
        String lahir = tanggalLahir.getText().toString();
        String tlp = nomorTelepon.getText().toString();
        String alamat = alamatLengkap.getText().toString();
        String password1 = kataSandi1.getText().toString();
        String password2 = kataSandi2.getText().toString();

        ApiClient.getClient().create(ApiInterface.class).setInsertRegister("7a2594cd-d6ff-440f-a950-f605342f55e4",
                1,nama,gender,lahir,tlp,alamat,provinceId,cityId,password1,password2, Prefs.getString("player_id", "")).enqueue(new Callback<ApiRegister>() {
            @Override
            public void onResponse(Call<ApiRegister> call, Response<ApiRegister> response) {
                try {
                    if (response.body().getResult()) {
                        mProgressDialog.dismiss();
                        List<Register> rgs = response.body().getResponse();
                        ApiClient.getClient().create(ApiInterface.class).UpdatesIds(api_key,
                                rgs.get(0).getKodePengguna(), Prefs.getString("player_id", "")).enqueue(new Callback<BantuanInsert>() {
                            @Override
                            public void onResponse(Call<BantuanInsert> call, Response<BantuanInsert> response) {

                            }

                            @Override
                            public void onFailure(Call<BantuanInsert> call, Throwable t) {

                            }
                        });
                        if (rgs.get(0).getVerifikasi().equals("0")) {
                            Intent i = new Intent(RegisterActivity.this, VerifikasiActivity.class);
                            i.putExtra("kodepengguna", rgs.get(0).getKodePengguna());
                            startActivity(i);
                        }
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Maaf, Nomor Telpon sudah digunakan", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }
                

            @Override
            public void onFailure(Call<ApiRegister> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });

    }
}
