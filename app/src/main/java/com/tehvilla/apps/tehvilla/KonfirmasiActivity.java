package com.tehvilla.apps.tehvilla;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fastaccess.permission.base.PermissionFragmentHelper;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.models.BantuanInsert;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.bitmap;
import static android.R.attr.onClick;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class KonfirmasiActivity extends AppCompatActivity implements OnPermissionCallback {
    int PICK_IMAGE_REQUEST = 1;
    ImageButton iBtnUpload;
    @BindView(R.id.orderId) EditText orderId;
    @BindView(R.id.bankAsal) EditText bankAsal;
    @BindView(R.id.namaRekening) EditText namaRekening;
    @BindView(R.id.nominal) EditText nominal;
    @BindView(R.id.tanggal) EditText tanggal;
    @BindView(R.id.btnKonfirmasi) Button btnKonfirmasi;
    @BindView(R.id.tvOrderId) TextView tvOrderId;
    @BindView(R.id.tvNamarekening) TextView tvNamaRekening;
    @BindView(R.id.tvNominal) TextView tvNominal;
    @BindView(R.id.tvTanggal) TextView tvTanggal;
    @BindView(R.id.tvBukti) TextView tvBukti;
    @BindView(R.id.kembali) ImageButton btnKembali;
    private int mYear,mMonth,mDay;
    Uri filePath;

    final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    final String PERMISSION2 = Manifest.permission.READ_EXTERNAL_STORAGE;
    final String PERMISSION3 = Manifest.permission.CAMERA;

    PermissionHelper permissionHelper;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    final String DIALOG_TITLE = "Akses Penyimpanan";
    final String DIALOG_MESSAGE = "Kami ingin meminta akses untuk Penyimpanan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);
        // Inflate the layout for this fragment
        ButterKnife.bind(this);
        permissionHelper = PermissionHelper.getInstance(this);
        permissionHelper.setForceAccepting(false).request(PERMISSION);
        permissionHelper.setForceAccepting(false).request(PERMISSION2);
        permissionHelper.setForceAccepting(false).request(PERMISSION3);


        iBtnUpload = (ImageButton) findViewById(R.id.iBtnUpload);
        iBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        String bintang = getColoredSpanned("*", "#FF0000");
        tvOrderId.setText(Html.fromHtml("Order Id Order "+bintang));
        tvNamaRekening.setText(Html.fromHtml("Rekening Atas Nama "+bintang));
        tvNominal.setText(Html.fromHtml("Nominal Transfer "+bintang));
        tvTanggal.setText(Html.fromHtml("Tanggal Transfer "+bintang));
        tvBukti.setText(Html.fromHtml("Upload Bukti Pembayaran "+bintang));

        orderId.setText(Prefs.getString("IDOrder", ""));

        tanggal.setInputType(InputType.TYPE_NULL);
        tanggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    Calendar mcurrentDate=Calendar.getInstance();
                    mYear=mcurrentDate.get(Calendar.YEAR);
                    mMonth=mcurrentDate.get(Calendar.MONTH);
                    mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker=new DatePickerDialog(KonfirmasiActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
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
                            tanggal.setText(selectedyear+"-"+formattedMonth+"-"+formattedDayOfMonth);
                        }
                    },mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select date");
                    mDatePicker.show();
                }else{
                    Calendar mcurrentDate=Calendar.getInstance();
                    mYear=mcurrentDate.get(Calendar.YEAR);
                    mMonth=mcurrentDate.get(Calendar.MONTH);
                    mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker=new DatePickerDialog(KonfirmasiActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
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
                            tanggal.setText(selectedyear+"-"+formattedMonth+"-"+formattedDayOfMonth);
                        }
                    },mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select date");
                    mDatePicker.hide();
                }
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KonfirmasiActivity.this, HomeActivity.class));
                finish();
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(KonfirmasiActivity.this, HomeActivity.class));
        finish();
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(MainActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    showFileChooser();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        filePath = data.getData();
        iBtnUpload.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), thumbnail, "Title", null);
        filePath= Uri.parse(path);
        iBtnUpload.setImageBitmap(thumbnail);
    }

    @OnClick(R.id.btnKonfirmasi)
    public void KlikSelesaiKonfirmasi(){

        if(orderId.getText().toString().equals("")){
            orderId.setBackgroundResource(R.drawable.bgkosong1);
            orderId.setError("Order ID harap Diisi");
        }
        else {
            orderId.setBackgroundResource(R.drawable.bgedittext);
        }

        if(namaRekening.getText().toString().equals("")){
            namaRekening.setBackgroundResource(R.drawable.bgkosong1);
            namaRekening.setError("Atas Nama Rekening Harap diisi");
        }
        else {
            namaRekening.setBackgroundResource(R.drawable.bgedittext);
        }

        if(nominal.getText().toString().equals("")){
            nominal.setBackgroundResource(R.drawable.bgkosong1);
            nominal.setError("Nominal Tranfer Harap diisi");
        }
        else {
            nominal.setBackgroundResource(R.drawable.bgedittext);
        }

        if(tanggal.getText().toString().equals("")){
            tanggal.setBackgroundResource(R.drawable.bgkosong1);
            tanggal.setError("Tanggal Pembayaran Harap diisi");
        }
        else {
            tanggal.setBackgroundResource(R.drawable.bgedittext);
        }

        if (filePath == null){
            Toast.makeText(KonfirmasiActivity.this, "Mohon lampirkan bukti transfer", Toast.LENGTH_SHORT).show();
        }

        if(orderId.getText().toString().equals("") == false && namaRekening.getText().toString().equals("")== false && nominal.getText().toString().equals("") == false && tanggal.getText().toString().equals("") == false){
            if(filePath != null) {
                File file = com.tehvilla.apps.tehvilla.helpers.FileUtils.getFile(KonfirmasiActivity.this, filePath);
                InsertKonfirmasi(file);
            }
        }
    }

    public void InsertKonfirmasi(File file){
        RequestBody api_key = RequestBody.create(MediaType.parse("text/plain"), "7a2594cd-d6ff-440f-a950-f605342f55e4");
        RequestBody kode_pemesanan = RequestBody.create(MediaType.parse("text/plain"), orderId.getText().toString());
        RequestBody kode_pengguna = RequestBody.create(MediaType.parse("text/plain"), Prefs.getString("Memberkode", ""));
        RequestBody last_token = RequestBody.create(MediaType.parse("text/plain"), Prefs.getString("Memberlasttoken", ""));
        RequestBody bank_asal = RequestBody.create(MediaType.parse("text/plain"), bankAsal.getText().toString());
        RequestBody rekening_atas_nama = RequestBody.create(MediaType.parse("text/plain"), namaRekening.getText().toString());
        RequestBody nominal_transfer = RequestBody.create(MediaType.parse("text/plain"), nominal.getText().toString());
        RequestBody tanggal_transfer = RequestBody.create(MediaType.parse("text/plain"), tanggal.getText().toString());

        Map<String, RequestBody> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("kode_pemesanan", kode_pemesanan);
        map.put("kode_pengguna", kode_pengguna);
        map.put("last_token", last_token);
        map.put("bank_asal", bank_asal);
        map.put("rekening_atas_nama", rekening_atas_nama);
        map.put("nominal_transfer", nominal_transfer);
        map.put("tanggal_transfer", tanggal_transfer);

        if(file != null){
            //File imgFile = new File(selectedPath);
            RequestBody imgReport = RequestBody.create(MediaType.parse("image/jpeg"), file);
            map.put("image\"; filename=\"" + file.getName(), imgReport);
        }

        ApiClient.getClient().create(ApiInterface.class).setInsertKonfirmasi(map).enqueue(new Callback<BantuanInsert>() {
            @Override
            public void onResponse(Call<BantuanInsert> call, Response<BantuanInsert> response) {
                
                try {
                    if (response.body().getResult()){
//                    Toast.makeText(getContext(),kodeSorting,Toast.LENGTH_LONG).show();
                        if (response.body().getResponse().toString().equals("[]") && response.body().getResult()==true) {
                            Toast.makeText(KonfirmasiActivity.this, "Data Anda Sudah Masuk", Toast.LENGTH_LONG);
                            Toast.makeText(KonfirmasiActivity.this, "Pembayaran Anda Sedang Kami Proses", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(KonfirmasiActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(KonfirmasiActivity.this, "Pesan Anda Gagal", Toast.LENGTH_LONG);
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(KonfirmasiActivity.this, "Harap periksa koneksi anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BantuanInsert> call, Throwable t) {
                Toast.makeText(KonfirmasiActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
//        Toast.makeText(getContext(), "onPermissionGranted()" + Arrays.toString(permissionName), Toast.LENGTH_SHORT).show();
//        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION);
//        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION2);
//        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION3);
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
//        Toast.makeText(getContext(), "onPermissionDeclined() " + Arrays.toString(permissionName), Toast.LENGTH_SHORT).show();
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION);
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION2);
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION3);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
//        Toast.makeText(getContext(), "onPermissionPreGranted() " + permissionsName, Toast.LENGTH_SHORT).show();
//        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION);
//        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION2);
//        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION3);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
//        Toast.makeText(getContext(), "onPermissionNeedExplanation() " + permissionName, Toast.LENGTH_SHORT).show();

        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION);
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION2);
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION3);
    }

    private void showAlertDialog(String dialog_title, String dialog_message, final String permission) {
        AlertDialog dialog = new AlertDialog.Builder(KonfirmasiActivity.this)
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
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION);
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION2);
        showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE, PERMISSION3);
    }

    @Override
    public void onNoPermissionNeeded() {
//        Toast.makeText(getContext(), "onNoPermissionNeeded() fallback for pre Marshmallow ", Toast.LENGTH_SHORT).show();
    }
}