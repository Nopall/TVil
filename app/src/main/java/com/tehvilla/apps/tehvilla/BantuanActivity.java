package com.tehvilla.apps.tehvilla;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tehvilla.apps.tehvilla.adapters.ProdukAdapter;
import com.tehvilla.apps.tehvilla.models.ApiBantuan;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.Bantuan;
import com.tehvilla.apps.tehvilla.models.BantuanInsert;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.R.attr.onClick;

public class BantuanActivity extends Fragment {
    @BindView(R.id.btnKirim)
    Button btnKirim;
    @BindView(R.id.btnTeleponKami)
    Button btnTeleponKami;
    @BindView(R.id.btnSmsKami)
    Button btnSmsKami;
    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.etTelepon)
    EditText etTelepon;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etSubject)
    EditText etSubject;
    @BindView(R.id.etPesan)
    EditText etPesan;
    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvTelepon)
    TextView tvTelepon;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvSubject)
    TextView tvSubject;
    @BindView(R.id.tvPesan)
    TextView tvPesan;

    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    String telpon="";
    String telponSms="";
    String kNama,kTelepon,kEmail,kSubyek,kPesan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_bantuan, container, false);
        ButterKnife.bind(this, rootView);

        String bintang = getColoredSpanned("*", "#FF0000");
        tvNama.setText(Html.fromHtml("Nama Lengkap " + bintang));
        tvTelepon.setText(Html.fromHtml("Nomor Telepon " + bintang));
        tvEmail.setText(Html.fromHtml("Email " + bintang));
        tvSubject.setText(Html.fromHtml("Subject " + bintang));
        tvPesan.setText(Html.fromHtml("Pesan " + bintang));

        setupJsonBantuan();

        return rootView;

    }

    @OnClick(R.id.btnKirim)
    public void ButtonKirim() {
        if (etNama.getText().toString().equals("")) {
            etNama.setBackgroundResource(R.drawable.bgkosong1);
            etNama.setError("Nama Lengkap harap Diisi");
        } else {
            etNama.setBackgroundResource(R.drawable.bgedittext);
        }

        if (etTelepon.getText().toString().equals("")) {
            etTelepon.setBackgroundResource(R.drawable.bgkosong1);
            etTelepon.setError("Nomor Telepon harap Diisi");
        } else {
            etTelepon.setBackgroundResource(R.drawable.bgedittext);
        }

        if (etEmail.getText().toString().equals("")) {
            etEmail.setBackgroundResource(R.drawable.bgkosong1);
            etEmail.setError("Email harap Diisi");
        } else {
            etEmail.setBackgroundResource(R.drawable.bgedittext);
        }

        if (etSubject.getText().toString().equals("")) {
            etSubject.setBackgroundResource(R.drawable.bgkosong1);
            etSubject.setError("Subject harap Diisi");
        } else {
            etSubject.setBackgroundResource(R.drawable.bgedittext);
        }

        if (etPesan.getText().toString().equals("")) {
            etPesan.setBackgroundResource(R.drawable.bgkosong1);
            etPesan.setError("Pesan harap Diisi");
        } else {
            etPesan.setBackgroundResource(R.drawable.bgedittext);
        }

        if (etNama.getText().toString().equals("") == false && etTelepon.getText().toString().equals("") == false && etEmail.getText().toString().equals("") == false && etSubject.getText().toString().equals("") == false && etPesan.getText().toString().equals("") == false) {
            kNama = etNama.getText().toString();
            kTelepon = etTelepon.getText().toString();
            kEmail = etEmail.getText().toString();
            kSubyek = etSubject.getText().toString();
            kPesan = etPesan.getText().toString();

            setupJsonInsert();

            Toast.makeText(getContext(), "Pesan Anda Sedang Kami Proses", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), HomeActivity.class);
            startActivity(i);
        }
    }

    @OnClick(R.id.btnSmsKami)
    public void SmsKami(){
        setupJsonBantuan();
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String (telponSms));
        smsIntent.putExtra("sms_body"  , "Test ");

        try {
            startActivity(smsIntent);
            Log.i("Finished sending SMS...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    private void setupJsonBantuan() {
        ApiClient.getClient().create(ApiInterface.class).getBantuan(api_key).enqueue(new Callback<ApiBantuan>() {
            @Override
            public void onResponse(Call<ApiBantuan> call, Response<ApiBantuan> response) {
                try {
                    if (response.body().getResult()){
//                    Toast.makeText(getContext(),kodeSorting,Toast.LENGTH_LONG).show();
                        List<Bantuan> listBantuan = response.body().getResponse();
                        telpon = listBantuan.get(0).getNoTelepon();
                        telponSms = listBantuan.get(0).getNoSms();

                        btnTeleponKami.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startCallActivity(telpon);
                            }
                        });
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiBantuan> call, Throwable t) {
                Toast.makeText(getContext(),"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupJsonInsert() {
        ApiClient.getClient().create(ApiInterface.class).getResponseBantuanInsert(api_key,kNama,kTelepon,kEmail,kSubyek,kPesan).enqueue(new Callback<BantuanInsert>() {
            @Override
            public void onResponse(Call<BantuanInsert> call, Response<BantuanInsert> response) {
                
                try{
                    if (response.body().getResult()){
//                    Toast.makeText(getContext(),kodeSorting,Toast.LENGTH_LONG).show();
                        List<Object> listBantuan = response.body().getResponse();
                        if (listBantuan.toString().equals("[]"))
                            Toast.makeText(getContext(),"Data Anda Sudah Masuk",Toast.LENGTH_LONG);
                        else
                            Toast.makeText(getContext(), "Pesan Anda Gagal",Toast.LENGTH_LONG);
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BantuanInsert> call, Throwable t) {
                Toast.makeText(getContext(),"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void startCallActivity(String phoneNumber)
    {
        try
        {
            StringBuilder builder = new StringBuilder();
            builder.append("tel:");
            builder.append(phoneNumber);

            Intent intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse(builder.toString()));
            startActivity(intent);
        }
        catch(android.content.ActivityNotFoundException e)
        {
            // can't start activity
        }
    }
}
