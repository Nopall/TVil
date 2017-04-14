package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tehvilla.apps.tehvilla.helpers.Helpers;
import com.tehvilla.apps.tehvilla.models.ApiMember;
import com.tehvilla.apps.tehvilla.models.ApiRegister;
import com.tehvilla.apps.tehvilla.models.ApiVerifikasi;
import com.tehvilla.apps.tehvilla.models.Member;
import com.tehvilla.apps.tehvilla.models.Register;
import com.tehvilla.apps.tehvilla.models.Verifikasi;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifikasiActivity extends AppCompatActivity {
    @BindView(R.id.btnVerifikasi) Button btnVerfikasi;
    @BindView(R.id.etVerifikasi) EditText etVerifikasi;
    private ProgressDialog mProgressDialog;
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.verifikasi)
    public void requestverifikasi(){
        RequestKodeVerifikasi();
    }

    private void RequestKodeVerifikasi() {
        mProgressDialog = new ProgressDialog(VerifikasiActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        String kodeVerifikasi = etVerifikasi.getText().toString();
        Intent intent=this.getIntent();
        String kodePengguna = intent.getStringExtra("kodepengguna");
        ApiClient.getClient().create(ApiInterface.class).setRequestVerifikasi("7a2594cd-d6ff-440f-a950-f605342f55e4",kodePengguna).enqueue(new Callback<ApiVerifikasi>() {
            @Override
            public void onResponse(Call<ApiVerifikasi> call, Response<ApiVerifikasi> response) {
                mProgressDialog.dismiss();
                try{
                    if (response.body().getResult()){
                        Toast.makeText(VerifikasiActivity.this, "Kode verifikasi telah dikirim di hp anda, silahkan masukkan kembali", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(VerifikasiActivity.this,rgs.get(0).getKodePengguna()+rgs.get(0).getLast_token()+rgs.get(0).getVerifikasi(),Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){
                    Toast.makeText(VerifikasiActivity.this, "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onFailure(Call<ApiVerifikasi> call, Throwable t) {
                Toast.makeText(VerifikasiActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });

    }

    @OnClick(R.id.btnVerifikasi)
    public void verifikasi(){
        InsertDataVerifikasi();
    }


    private void InsertDataVerifikasi() {
        mProgressDialog = new ProgressDialog(VerifikasiActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        String kodeVerifikasi = etVerifikasi.getText().toString();
        Intent intent=this.getIntent();
        String kodePengguna = intent.getStringExtra("kodepengguna");
        Log.v("ASU", "Kode" +kodePengguna);
        ApiClient.getClient().create(ApiInterface.class).setVerifikasi("7a2594cd-d6ff-440f-a950-f605342f55e4",kodePengguna,kodeVerifikasi).enqueue(new Callback<ApiVerifikasi>() {
            @Override
            public void onResponse(Call<ApiVerifikasi> call, Response<ApiVerifikasi> response) {
                mProgressDialog.dismiss();
                try{
                    if (response.body().getResult()){
                        List<Verifikasi> rgs = response.body().getResponse();
//                    Toast.makeText(VerifikasiActivity.this,rgs.get(0).getKodePengguna()+rgs.get(0).getLast_token()+rgs.get(0).getVerifikasi(),Toast.LENGTH_LONG).show();
                        if(rgs.get(0).getVerifikasi().equals("1")){
                            ApiClient.getClient().create(ApiInterface.class)
                                    .getPengguna(api_key, rgs.get(0).getKodePengguna(), rgs.get(0).getLast_token(), Prefs.getString("player_id", "")).enqueue(new Callback<ApiMember>() {
                                @Override
                                public void onResponse(Call<ApiMember> call, Response<ApiMember> response) {
                                    List<Member> mbr = response.body().getResponse();
                                    if (response.body().getResult()){
                                        Helpers.setActiveMember(mbr);
                                        Toast.makeText(VerifikasiActivity.this,"Verifikasi Anda Berhasil",Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(VerifikasiActivity.this,HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiMember> call, Throwable t) {

                                }
                            });
                        }
                    }else{
                        Toast.makeText(VerifikasiActivity.this,"Kode Verifikasi Anda Salah, Harap Masukkan Kode yang benar",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(VerifikasiActivity.this, "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiVerifikasi> call, Throwable t) {
                Toast.makeText(VerifikasiActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
