package com.tehvilla.apps.tehvilla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.models.Keranjang;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailPengirimanActivity extends AppCompatActivity {
    @BindView(R.id.btnKembali) ImageButton btnKembali;
    @BindView(R.id.btnLanjutCod) Button btnLanjutCod;
    @BindView(R.id.btnLanjutTf) Button btnLanjutTf;
    @BindView(R.id.btnUbahCod) ImageButton btnUbahCod;
    @BindView(R.id.btnUbahTfBank) ImageButton btnUbahTfBank;
    @BindView(R.id.halamanCod) LinearLayout halamanCod;
    @BindView(R.id.halamanTfBank) LinearLayout halamanTfBank;
    @BindView(R.id.etTglPengiriman) EditText etTglPengiriman;
    @BindView(R.id.etWaktuPengiriman) Spinner etWaktuPengiriman;
    @BindView(R.id.scrollviewcod) ScrollView scrollviewcod;
    @BindView(R.id.scrollviewtf) ScrollView scrollviewtf;
    String pengiriman;
    Realm realm;
    private int mYear,mMonth,mDay;
    String[] waktuCod = {
            "08.00-10.00",
            "10.00-12.00",
            "12.00-14.00",
            "14.00-16.00",
            "16.00-18.00"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengiriman);

        ButterKnife.bind(this);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        //Setting Default Font menjadi Calibri Regular
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/calibri_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        etTglPengiriman.setInputType(InputType.TYPE_NULL);

        etTglPengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();

                DatePickerDialog mDatePicker=new DatePickerDialog(DetailPengirimanActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
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
                        etTglPengiriman.setText(selectedyear+"-"+formattedMonth+"-"+formattedDayOfMonth);
                    }
                },cal.YEAR, cal.MONTH, cal.DAY_OF_MONTH);
                cal1.add(Calendar.DAY_OF_MONTH, 1);
                mDatePicker.getDatePicker().setMinDate(cal1.getTimeInMillis());
                cal1.add(Calendar.DAY_OF_MONTH, 7);
                mDatePicker.getDatePicker().setMaxDate(cal1.getTimeInMillis());
                mDatePicker.show();
            }
        });

        etWaktuPengiriman.setAdapter(new ArrayAdapter<String>(DetailPengirimanActivity.this,
                R.layout.list_item_spinner, waktuCod));

        TfBank();
    }

    @OnClick(R.id.btnKembali)
    public void SaatKembali() {
        // TODO submit data to server...
//        Intent i = new Intent(DetailPengirimanActivity.this, KeranjangActivity.class);
//        Bundle b = new Bundle();
//        b.putString("judul", "00");
//        i.putExtras(b);
//        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnLanjutCod)
    public void LanjutCod() {
        // TODO submit data to server...


            if(etTglPengiriman.getText().toString().equals("")){
                etTglPengiriman.setBackgroundResource(R.drawable.bgkosong);
                etTglPengiriman.setError("Tgl Pengiriman Harap diisi");
            }
            else {
                etTglPengiriman.setBackgroundResource(R.drawable.bgedittext2);
            }

            if(etTglPengiriman.getText().toString().equals("") == false && etWaktuPengiriman.getSelectedItem().toString().equals("") == false){
                String tanggal = etTglPengiriman.getText().toString();
                String waktu = etWaktuPengiriman.getSelectedItem().toString();
                Prefs.putString("tanggal_cod",tanggal);
                Prefs.putString("waktu_cod",waktu);
                Prefs.putString("pengiriman",pengiriman);

                Intent i = new Intent(DetailPengirimanActivity.this,UlasanActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }

    }

    @OnClick(R.id.btnLanjutTf)
    public void LanjutTf() {
        // TODO submit data to server...

        if(pengiriman.equals("tf")) {
            Prefs.putString("pengiriman", "tf");
            Intent i = new Intent(DetailPengirimanActivity.this, UlasanActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
//        }else{
//            if(etTglPengiriman.getText().toString().equals("")){
//                etTglPengiriman.setBackgroundResource(R.drawable.bgkosong);
//                etTglPengiriman.setError("Tgl Pengiriman Harap diisi");
//            }
//            else {
//                etTglPengiriman.setBackgroundResource(R.drawable.bgedittext2);
//            }
//
//            if(etTglPengiriman.getText().toString().equals("") == false && etWaktuPengiriman.getSelectedItem().toString().equals("") == false){
//                String tanggal = etTglPengiriman.getText().toString();
//                String waktu = etWaktuPengiriman.getSelectedItem().toString();
//                Prefs.putString("tanggal_cod",tanggal);
//                Prefs.putString("waktu_cod",waktu);
//                Prefs.putString("pengiriman",pengiriman);
//
//                Intent i = new Intent(DetailPengirimanActivity.this,UlasanActivity.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//            }
//        }
    }

    @OnClick(R.id.btnUbahCod)
    public void Cod() {
        scrollviewcod.setVisibility(View.VISIBLE);
        halamanCod.setVisibility(View.VISIBLE);
        scrollviewtf.setVisibility(View.INVISIBLE);
        halamanTfBank.setVisibility(View.INVISIBLE);
        btnLanjutTf.setVisibility(View.INVISIBLE);
        btnLanjutCod.setVisibility(View.VISIBLE);
        btnUbahCod.setBackgroundResource(R.drawable.btn_cod);
        btnUbahTfBank.setBackgroundResource(R.drawable.btn_tfbank2);
        pengiriman="cod";
    }

    @OnClick(R.id.btnUbahTfBank)
    public void TfBank() {
        scrollviewcod.setVisibility(View.INVISIBLE);
        halamanCod.setVisibility(View.INVISIBLE);
        scrollviewtf.setVisibility(View.VISIBLE);
        halamanTfBank.setVisibility(View.VISIBLE);
        btnLanjutCod.setVisibility(View.INVISIBLE);
        btnLanjutTf.setVisibility(View.VISIBLE);
        btnUbahCod.setBackgroundResource(R.drawable.btn_cod2);
        btnUbahTfBank.setBackgroundResource(R.drawable.btn_tfbank);
        pengiriman="tf";
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i = new Intent(DetailPengirimanActivity.this, KeranjangActivity.class);
//        Bundle b = new Bundle();
//        b.putString("judul", "00");
//        i.putExtras(b);
//        startActivity(i);
        finish();
    }
}