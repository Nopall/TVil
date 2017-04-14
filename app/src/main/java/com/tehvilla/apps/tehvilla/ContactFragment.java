package com.tehvilla.apps.tehvilla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fastaccess.permission.base.PermissionFragmentHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.onesignal.OneSignal;
import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.adapters.ProdukAdapter;
import com.tehvilla.apps.tehvilla.helpers.ImageUtils;
import com.tehvilla.apps.tehvilla.models.ApiDetailPengguna;
import com.tehvilla.apps.tehvilla.models.ApiMember;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.ApiPassword;
import com.tehvilla.apps.tehvilla.models.ApiProvinsi;
import com.tehvilla.apps.tehvilla.models.BantuanInsert;
import com.tehvilla.apps.tehvilla.models.DetailPengguna;
import com.tehvilla.apps.tehvilla.models.EditPengguna;
import com.tehvilla.apps.tehvilla.models.Pengguna;
import com.tehvilla.apps.tehvilla.models.Provinsi;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.APPWIDGET_SERVICE;


public class ContactFragment extends Fragment implements AdapterView.OnItemSelectedListener, OnPermissionCallback {
    private static final int PICK_IMAGE_REQUEST = 1;

    //Inisialisasi Variabel ButterKnife

    //Text View
    @BindView(R.id.tvNama) TextView tvNama;
    @BindView(R.id.tvAlamat) TextView tvAlamat;
    @BindView(R.id.tvPos) TextView tvPos;
    @BindView(R.id.tvTelepon) TextView tvTelepon;
    @BindView(R.id.txtNama) TextView txtNama;

    //button atas profil dan sandi
    @BindView(R.id.btn_ubahProfil) ImageButton btnProfil;
    @BindView(R.id.btn_ubahSandi) ImageButton btnSandi;

    //Image Profil
    @BindView(R.id.imgFoto) ImageView imgFoto;
    @BindView(R.id.lihatFoto) ImageView lihatFoto;

    //edit profil
    @BindView(R.id.etNama) EditText etNama;
    @BindView(R.id.etLahir) EditText etLahir;
    @BindView(R.id.etAlamat) EditText etAlamat;
    @BindView(R.id.etPos) EditText etPos;
    @BindView(R.id.etTelepon) EditText etTelepon;

    //lihat Profil
    @BindView(R.id.lihatNamaLengkap) TextView lihatNamaLengkap;
    @BindView(R.id.lihatAlamat) TextView lihatAlamat;
    @BindView(R.id.lihatProvinsi) TextView lihatProvinsi;
    @BindView(R.id.lihatKota) TextView lihatKota;
    @BindView(R.id.lihatPos) TextView lihatPos;
    @BindView(R.id.lihatTelepon) TextView lihatTelepon;
    @BindView(R.id.LihatNama) TextView LihatNama;
    @BindView(R.id.TampilLahir) TextView TampilLahir;
    //    @BindView(R.id.GenLaki) ImageButton btnGenlaki;
    @BindView(R.id.GenPerempuan) ImageButton lihatgenPerempuan;

    //button simpan profil dan sandi
    @BindView(R.id.btnSimpan) Button btnSimpan;
    @BindView(R.id.btnSimpanSandi) Button btnSimpanSandi;

    //halaman visible invisible
    @BindView(R.id.halamanProfil) RelativeLayout halamanProfil;
    @BindView(R.id.LihatProfil) RelativeLayout halamanlihatProfile;
    @BindView(R.id.halamanSandi) RelativeLayout halamanSandi;

    //spinner Provinsi dan Kota
    @BindView(R.id.spnProvinsi) Spinner spnProvinsi;
    @BindView(R.id.spnKota) Spinner spnKota;

    //Button Gender Laki Laki dan Perempuan
    @BindView(R.id.btngenderLaki) ImageButton btngenderLaki;
    @BindView(R.id.btngenderPerempuan) ImageButton btngenderPerempuan;
    @BindView(R.id.btngenderLakiOff) ImageButton btngenderLakioff;
    @BindView(R.id.btngenderPerempuanOff) ImageButton btngenderPerempuanoff;

    //btn Pindah ke halaman edit profil
    @BindView(R.id.btnEditProfile) Button btneditprofile;

    //Halaman Sandi
    @BindView(R.id.old_password) EditText old_password;
    @BindView(R.id.new_password1) EditText new_password1;
    @BindView(R.id.new_password2) EditText new_password2;

    @BindView(R.id.tvPeringatan) TextView tvPeringatan;
    //variabel di java
    String[] aProvinsi,aKode,aKota,aKodeKota;
    String provinceId = Prefs.getString("Memberprovinsi", "");;
    String cityId = Prefs.getString("Memberkota", "");
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    String gender;
    Uri filePath;
    final String sPos = Prefs.getString("Memberprovinsi","");
    int status=0,statuspos=0;
    int position_prov,position_kota;
    int a=0;
    private int mYear,mMonth,mDay;
    private static final String TYPE_2 = "base64";
    private ProgressDialog mProgressDialog;
    View view;

    final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    final String PERMISSION2 = Manifest.permission.READ_EXTERNAL_STORAGE;

    PermissionFragmentHelper permissionHelper;

    final String DIALOG_TITLE = "Akses Penyimpanan";
    final String DIALOG_MESSAGE = "Kami ingin meminta akses untuk Penyimpanan";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        //deklarasi Butter knife
        ButterKnife.bind(this,view);

        HomeActivity.txttoolbar.setText("SETTING");
        permissionHelper = PermissionFragmentHelper.getInstance(this);
        permissionHelper.setForceAccepting(false).request(PERMISSION);
        permissionHelper.setForceAccepting(false).request(PERMISSION2);

        Toast.makeText(getContext(), ""+provinceId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), ""+cityId, Toast.LENGTH_SHORT).show();

        getplayerId();

        if (provinceId.equals("") && cityId.equals("")){
            provinceId = "25";
            cityId = "2537";
        }else {

            provinceId = Prefs.getString("Memberprovinsi", "");
            cityId = Prefs.getString("Memberkota", "");
        }

        position_prov = ((Integer.parseInt(provinceId))-11);
        position_kota = ((Integer.parseInt(cityId))%100)-1;

        btngenderPerempuanoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="P";
                btngenderLaki.setVisibility(View.INVISIBLE);
                btngenderLakioff.setVisibility(View.VISIBLE);
                btngenderPerempuan.setVisibility(View.VISIBLE);
                btngenderPerempuanoff.setVisibility(View.INVISIBLE);
            }
        });

        btngenderLakioff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="L";
                btngenderLaki.setVisibility(View.VISIBLE);
                btngenderLakioff.setVisibility(View.INVISIBLE);
                btngenderPerempuan.setVisibility(View.INVISIBLE);
                btngenderPerempuanoff.setVisibility(View.VISIBLE);
            }
        });

        return view;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
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
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        Bundle b = new Bundle();
                        b.putString("ceklogin", "setting");
                        i.putExtras(b);
                        startActivity(i);
                        ((HomeActivity)getActivity()).finish();

                    }else{
                        Prefs.putString("Membernama",response.body().getResponse().get(0).getNamaLengkap());
                        Prefs.putString("Memberjenkel", response.body().getResponse().get(0).getJenisKelamin());
                        Prefs.putString("Membertanggallahir", response.body().getResponse().get(0).getTanggalLahir());
                        Prefs.putString("Memberalamat", response.body().getResponse().get(0).getAlamat());
                        Prefs.putString("Memberprovinsi", response.body().getResponse().get(0).getProvinsi());
                        Prefs.putString("Membertelepon", response.body().getResponse().get(0).getTelepon());
                        Prefs.putString("Memberkota", response.body().getResponse().get(0).getKota());
                        Prefs.putString("Memberkodepos", response.body().getResponse().get(0).getKodePos());
                        Prefs.putString("Memberphoto", response.body().getResponse().get(0).getUrlGambar());
                        Prefs.putString("Memberemail", response.body().getResponse().get(0).getEmail());

                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Mohon maaf ada kendala, harap dicoba kembali", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiMember> call, Throwable t) {

            }
        });

        //Memberi bintang pada text view
        String bintang = getColoredSpanned("*", "#FF0000");
        tvNama.setText(Html.fromHtml("Nama Lengkap "+bintang));
        tvAlamat.setText(Html.fromHtml("Alamat Lengkap "+bintang));
        tvPos.setText(Html.fromHtml("Kode Pos "+bintang));
        tvTelepon.setText(Html.fromHtml("Nomor Telepon "+bintang));

        // inisialiasi Array Adapter dengan memasukkan string array di atas

        aKode = new String[35];

        // mengeset Array Adapter tersebut ke Spinner



        //Saat btn Di klik akan pindah ke halaman profil
        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                halamanProfil.setVisibility(View.VISIBLE);
                halamanlihatProfile.setVisibility(View.INVISIBLE);
                halamanSandi.setVisibility(View.INVISIBLE);
                btnProfil.setImageResource(R.drawable.button_ubahprofil);
                btnSandi.setImageResource(R.drawable.button_ubahsandi2);
                btneditprofile.setVisibility(View.INVISIBLE);
                status=1;
                setupJsonProvinsi();
                setupJsonKota();
            }
        });

        etLahir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        int s;
                        s = selectedmonth+1;
                        etLahir.setText(selectedyear+"-"+s+"-"+selectedday);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });

//        position_prov = ((Integer.parseInt(Prefs.getString("Memberprovinsi",""))))-11;
//        position_kota = ((Integer.parseInt(Prefs.getString("Memberkota","")))%100)-1;



//        Toast.makeText(getContext(),"aprov "+position_prov,Toast.LENGTH_LONG).show();
//        Toast.makeText(getContext(),"akota "+position_kota,Toast.LENGTH_LONG).show();
        Profil();
        TampilProfil();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imgFoto.setImageURI(filePath);
        }
    }

    @OnClick(R.id.btn_ubahProfil)
    public void Profil() {
        tvPeringatan.setVisibility(View.INVISIBLE);
        halamanlihatProfile.setVisibility(View.VISIBLE);
        halamanSandi.setVisibility(View.INVISIBLE);
        halamanProfil.setVisibility(View.INVISIBLE);
        btnProfil.setImageResource(R.drawable.button_ubahprofil);
        btnSandi.setImageResource(R.drawable.button_ubahsandi2);
        status=0;
    }



    @OnClick(R.id.btnSimpan)
    public void Simpan() {
        // TODO submit data to server...
        if(etNama.getText().toString().equals("")){
            etNama.setBackgroundResource(R.drawable.bgkosong);
            etNama.setError("Nama Lengkap Harap DIisi");
        }
        else {
            etNama.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(etTelepon.getText().toString().equals("")){
            etTelepon.setBackgroundResource(R.drawable.bgkosong);
            etTelepon.setError("Nomor Telepon Harap diisi");
        }
        else {
            etTelepon.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(etAlamat.getText().toString().equals("")){
            etAlamat.setBackgroundResource(R.drawable.bgkosong);
            etAlamat.setError("Alamat Lengkap Harap diisi");
        }
        else {
            etAlamat.setBackgroundResource(R.drawable.bgedittext2);
        }

        if(etPos.getText().toString().equals("")){
            etPos.setBackgroundResource(R.drawable.bgkosong);
            etPos.setError("Kata Sandi Harap diisi");
        }
        else {
            etPos.setBackgroundResource(R.drawable.bgedittext2);
        }
        if(etNama.getText().toString().equals("") == false && etAlamat.getText().toString().equals("") == false && etTelepon.getText().toString().equals("") == false && etPos.getText().toString().equals("") == false){
            File file = null;
            if(filePath != null) {
                file = com.tehvilla.apps.tehvilla.helpers.FileUtils.getFile(getActivity(),filePath);
            }
            Prefs.putString("Memberprovinsi",Prefs.getString("tempProvinsi",""));
            Prefs.putString("Memberkota",Prefs.getString("tempKota",""));
            EditProfil(file);
        }
    }

    @OnClick(R.id.btnSimpanSandi)
    public void SimpanSandi() {
        EditPassword();
    }

    @OnClick(R.id.btn_ubahSandi)
    public void Sandi() {
        halamanProfil.setVisibility(View.INVISIBLE);
        halamanlihatProfile.setVisibility(View.INVISIBLE);
        btnProfil.setImageResource(R.drawable.button_ubahprofil2);
        btnSandi.setImageResource(R.drawable.button_ubahsandi);
        if(Prefs.getString("kodeLoginType","").equals("2") || Prefs.getString("kodeLoginType","").equals("3")){
            tvPeringatan.setVisibility(View.VISIBLE);
            halamanSandi.setVisibility(View.INVISIBLE);
        }
        else{
            tvPeringatan.setVisibility(View.INVISIBLE);
            halamanSandi.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.imgFoto)
    public void FotoProfil(){
        showFileChooser();
    }

    public void TampilProfil(){
        setupJsonProvinsi();
        setupJsonKota();
        ApiClient.getClient().create(ApiInterface.class).getDetailPengguna(Prefs.getString("Memberkode", ""),Prefs.getString("Memberlasttoken", ""),api_key).enqueue(new Callback<ApiDetailPengguna>() {
            @Override
            public void onResponse(Call<ApiDetailPengguna> call, Response<ApiDetailPengguna> response) {
                try {
                    if (response.body().getResult()){
                        List<DetailPengguna> dp = response.body().getResponse();
                        Prefs.putString("kodeLoginType",dp.get(0).getKodeRegisterType());
                        lihatNamaLengkap.setText(dp.get(0).getNama());
                        lihatAlamat.setText(dp.get(0).getAlamat());
                        lihatPos.setText(dp.get(0).getKodePos());
                        lihatTelepon.setText(dp.get(0).getTelepon());
                        LihatNama.setText(dp.get(0).getNama());
                        TampilLahir.setText(dp.get(0).getTanggalLahir());
                        Glide.with(getActivity()).load(dp.get(0)
                                .getUrlGambar())
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(lihatFoto);
                        gender = dp.get(0).getJenisKelamin();
                        if (dp.get(0).getJenisKelamin().equals("L")){
                            Glide.with(getActivity()).load(R.drawable.ic_male).into(lihatgenPerempuan);
                            btngenderLaki.setVisibility(View.VISIBLE);
                            btngenderLakioff.setVisibility(View.INVISIBLE);
                            btngenderPerempuan.setVisibility(View.INVISIBLE);
                            btngenderPerempuanoff.setVisibility(View.VISIBLE);
                        }else {
                            Glide.with(getActivity()).load(R.drawable.ic_female).into(lihatgenPerempuan);
                            btngenderLaki.setVisibility(View.INVISIBLE);
                            btngenderLakioff.setVisibility(View.VISIBLE);
                            btngenderPerempuan.setVisibility(View.VISIBLE);
                            btngenderPerempuanoff.setVisibility(View.INVISIBLE);
                        }

                        txtNama.setText(dp.get(0).getNama());
                        etNama.setText(dp.get(0).getNama());
                        etAlamat.setText(dp.get(0).getAlamat());
                        etLahir.setText(dp.get(0).getTanggalLahir());
                        etPos.setText(dp.get(0).getKodePos());
                        etTelepon.setText(dp.get(0).getTelepon());
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
                        Glide.with(getActivity()).load(dp.get(0).getUrlGambar()).centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(imgFoto);
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Maaf ada kendala, Harap coba beberapa saat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDetailPengguna> call, Throwable t) {
                Toast.makeText(getContext(),"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void EditProfil(File file){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        String cNama = etNama.getText().toString();
        String cAlamat = etAlamat.getText().toString();
        String cLahir = etLahir.getText().toString();
        String cPos = etPos.getText().toString();
        String cTelepon = etTelepon.getText().toString();
        String cEmail = Prefs.getString("Memberemail", "");

        RequestBody kode_pengguna = RequestBody.create(MediaType.parse("text/plain"), Prefs.getString("Memberkode", ""));
        RequestBody last_token = RequestBody.create(MediaType.parse("text/plain"), Prefs.getString("Memberlasttoken", ""));
        RequestBody api_key = RequestBody.create(MediaType.parse("text/plain"), "7a2594cd-d6ff-440f-a950-f605342f55e4");
        RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), cNama);
        RequestBody jenis_kelamin = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody tanggal_lahir = RequestBody.create(MediaType.parse("text/plain"), cLahir);
        RequestBody alamat = RequestBody.create(MediaType.parse("text/plain"), cAlamat);
        RequestBody provinsi = RequestBody.create(MediaType.parse("text/plain"), Prefs.getString("Memberprovinsi",""));
        RequestBody kota = RequestBody.create(MediaType.parse("text/plain"), Prefs.getString("Memberkota",""));
        RequestBody kode_pos = RequestBody.create(MediaType.parse("text/plain"), cPos);
        RequestBody telepon = RequestBody.create(MediaType.parse("text/plain"), cTelepon);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), cEmail);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("kode_pengguna", kode_pengguna);
        map.put("last_token", last_token);
        map.put("api_key", api_key);
        map.put("nama", nama);
        map.put("jenis_kelamin", jenis_kelamin);
        map.put("tanggal_lahir", tanggal_lahir);
        map.put("alamat", alamat);
        map.put("provinsi", provinsi);
        map.put("kota", kota);
        map.put("kode_pos", kode_pos);
        map.put("telepon", telepon);
        map.put("email", email);

        if(file != null){
            if (file.toString().endsWith(".jpg")) {
                RequestBody imgReport = RequestBody.create(MediaType.parse("image/jpeg"), file);
                map.put("image\"; filename=\"" + file.getName() + "", imgReport);
            }else{
                Toast.makeText(getContext(), "Gambar tidak bisa diganti, gambar yang diupload harus format .JPG", Toast.LENGTH_SHORT).show();
            }
        }

        ApiClient.getClient().create(ApiInterface.class).setInsertEditProfil(map).enqueue(new Callback<EditPengguna>() {
            @Override
            public void onResponse(Call<EditPengguna> call, Response<EditPengguna> response) {
                Log.v("Tos", String.valueOf(response.body().getResult()));
                mProgressDialog.dismiss();
                halamanlihatProfile.setVisibility(View.VISIBLE);
                halamanProfil.setVisibility(View.INVISIBLE);
                btneditprofile.setVisibility(View.VISIBLE);
                tvPeringatan.setVisibility(View.INVISIBLE);

                try {
                    if (response.body().getResult()) {
                        List<Pengguna> pg = response.body().getResponse();
                        Prefs.putString("Membernama",pg.get(0).getNamaLengkap());
                        Prefs.putString("Memberjenkel", pg.get(0).getJenisKelamin());
                        Prefs.putString("Membertanggallahir", pg.get(0).getTanggalLahir());
                        Prefs.putString("Memberalamat", pg.get(0).getAlamat());
                        Prefs.putString("Memberprovinsi", pg.get(0).getProvinsi());
                        Prefs.putString("Membertelepon", pg.get(0).getTelepon());
                        Prefs.putString("Memberkota", pg.get(0).getKota());
                        Prefs.putString("Memberkodepos", pg.get(0).getKodePos());
                        Prefs.putString("Memberphoto", pg.get(0).getUrlGambar());
                        Prefs.putString("Memberemail", pg.get(0).getEmail());
//                    Toast.makeText(getContext(),kodeSorting,Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Profile berhasil diperbrui", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), KeranjangActivity.class);
                        Bundle b = new Bundle();
                        b.putString("judul", "11");
                        i.putExtras(b);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getContext(),"Profile gagal diperbrui",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditPengguna> call, Throwable t) {
                Toast.makeText(getContext(),"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void EditPassword(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        final String oldPassword = old_password.getText().toString();
        String newPassword1 = new_password1.getText().toString();
        String newPassword2 = new_password2.getText().toString();
        ApiClient.getClient().create(ApiInterface.class).setInsertEditPassword(Prefs.getString("Memberkode", ""),Prefs.getString("Memberlasttoken", ""),api_key,oldPassword,newPassword1,newPassword2).enqueue(new Callback<ApiPassword>() {
            @Override
            public void onResponse(Call<ApiPassword> call, Response<ApiPassword> response) {
                Log.v("Tos", String.valueOf(response.body().getResult()));
                if (response.body().getResult()) {
//                    if (response.body().getResponse().toString().equals("[]") && response.body().getResult() == true)
                    Toast.makeText(getContext(), "Password anda berhasil diperbarui", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(), "Password anda gagal diperbarui", Toast.LENGTH_LONG).show();
                }

                old_password.setText("");
                new_password1.setText("");
                new_password2.setText("");
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiPassword> call, Throwable t) {

            }
        });
    }

    private void setupJsonProvinsi() {
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

                        Toast.makeText(getContext(), "Cek status"+ aProvinsi[position_prov], Toast.LENGTH_SHORT).show();

                        if(status==0)
                            lihatProvinsi.setText(""+aProvinsi[position_prov]);

                        //Toast.makeText(getContext(),"a"+position_kota,Toast.LENGTH_LONG).show();
                        if (getActivity() != null) {
                            ArrayAdapter<String> adapter;
                            adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_spinner,aProvinsi);
                            //setting adapter to spinner
                            spnProvinsi.setAdapter(adapter);
                            if(status==1)
                                spnProvinsi.setSelection(position_prov);
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Maaf ada kendala, Harap coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(getContext(),"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
            }
        });
        spnProvinsi.setOnItemSelectedListener(this);
    }

    private void setupJsonKota() {
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
                        if(status==0){
                            lihatKota.setText(""+aKota[position_kota]);
                        }

                    }
                    if (getActivity() != null) {
                        spnKota.setAdapter(new ArrayAdapter<String>(getActivity(),
                                R.layout.list_item_spinner, aKota));
                        if(status==1 && sPos.equals(provinceId)==false){
                            Toast.makeText(getContext(), "Status 1", Toast.LENGTH_SHORT).show();
                            spnKota.setSelection(0);
                            a+=position_prov;
                        }else{
                            Toast.makeText(getContext(), "Status 0", Toast.LENGTH_SHORT).show();
                            spnKota.setSelection(position_kota);
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Maaf ada kendala, harap coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiProvinsi> call, Throwable t) {
                Toast.makeText(getContext(),"Harap Periksa Koneksi Anda",Toast.LENGTH_LONG).show();
            }

        });

        spnKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                cityId = aKodeKota[i];
                Prefs.putString("tempKota",cityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(status==1){
            provinceId = aKode[position];
            Prefs.putString("tempProvinsi",aKode[position]);
            setupJsonKota();
        }else{
            setupJsonKota();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
//        Toast.makeText(getContext(), "onPermissionGranted()" + Arrays.toString(permissionName), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
//        Toast.makeText(getContext(), "onPermissionDeclined() " + Arrays.toString(permissionName), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
//        Toast.makeText(getContext(), "onPermissionPreGranted() " + permissionsName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
//        Toast.makeText(getContext(), "onPermissionNeedExplanation() " + permissionName, Toast.LENGTH_SHORT).show();

        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION);
    }

    private void showAlertDialog(String dialog_title, String dialog_message, final String permission) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Permission")
                .setMessage("Teh Villa ingin meminta akses kepada anda")
                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        permissionHelper.requestAfterExplanation(permission);

                    }
                })
                .create();

        dialog.show();
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
//        Toast.makeText(getContext(), "onPermissionReallyDeclined() " + permissionName + "\nCan only be granted from settingsScreen", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoPermissionNeeded() {
//        Toast.makeText(getContext(), "onNoPermissionNeeded() fallback for pre Marshmallow ", Toast.LENGTH_SHORT).show();
    }
}