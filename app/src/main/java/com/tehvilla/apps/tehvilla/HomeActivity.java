package com.tehvilla.apps.tehvilla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.helpers.Helpers;
import com.tehvilla.apps.tehvilla.models.ApiLogout;
import com.tehvilla.apps.tehvilla.models.ApiMember;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.BantuanInsert;
import com.tehvilla.apps.tehvilla.models.Member;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener , OnPermissionCallback {
    private static final int PROFILE_SETTING = 100000;
    AccountHeader headerResult;
    Drawer result;
    private GoogleApiClient mGoogleApiClient;
    public static Toolbar toolbar;
    public static TextView txttoolbar;
    public static ImageButton imgCart;
    public static ImageButton imgSearch;
    public static EditText edttextsearch;
    public static ImageButton imgarrow;
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";

    final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    final String PERMISSION2 = Manifest.permission.READ_EXTERNAL_STORAGE;
    final String PERMISSION3 = Manifest.permission.CAMERA;

    PermissionHelper permissionHelper;

    final String DIALOG_TITLE = "Akses Penyimpanan";
    final String DIALOG_MESSAGE = "Kami ingin meminta akses untuk Penyimpanan";

    InputMethodManager imm;

    boolean doubleBackToExitPressedOnce = false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //build library Navigation Drawer
        FacebookSdk.sdkInitialize(getApplicationContext());
        new DrawerBuilder().withActivity(this).build();

        permissionHelper = PermissionHelper.getInstance(this);
        permissionHelper.setForceAccepting(false).request(PERMISSION);
        permissionHelper.setForceAccepting(false).request(PERMISSION2);
        permissionHelper.setForceAccepting(false).request(PERMISSION3);


        setupGoogleClient();
        CekReminder();
//        icCari = (Button) findViewById(R.id.ic_cari);
        //inisialisasi toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        txttoolbar = (TextView)toolbar.findViewById(R.id.txtToolbar);
        imgCart = (ImageButton) toolbar.findViewById(R.id.imgCart);
        imgSearch = (ImageButton) toolbar.findViewById(R.id.imgSearch);
        edttextsearch = (EditText) toolbar.findViewById(R.id.EdittxtSearch);
        imgarrow = (ImageButton)toolbar.findViewById(R.id.imgArrow);

//        edttextsearch.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_back, 0, 0, 0);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

//        edttextsearch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                imm.showSoftInput(HomeActivity.edttextsearch, InputMethodManager.SHOW_FORCED);
//                return false;
//            }
//        });

//        imgarrow.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_keyboard_arrow_left).color(Color.GRAY).actionBar());
//        setSupportActionBar(toolbar);

//        toolbar.setTitle("Home");

        //Log.v("nopal", Prefs.getString("Membernama", ""));

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), KeranjangActivity.class);
                Bundle b = new Bundle();
                b.putString("judul","00");
                i.putExtras(b);
                startActivity(i);
            }
        });

        //Setting Default Font menjadi Calibri Regular
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/calibri_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //inisialisasi variabel drawerItem
//        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(11).withName("Home").withIcon(R.mipmap.icon_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(1).withName("Keranjang Belanja").withIcon(R.mipmap.icon_keranjang);
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(imageView.getContext()).load(uri).centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }
        });

        final IProfile profile = new ProfileDrawerItem().withName(Prefs.getString("Membernama", ""))
                .withIcon(Prefs.getString("Memberphoto",""));
        Log.v("Allah", Prefs.getString("Memberphoto", ""));
        Log.v("Allah", Prefs.getString("Membernama", ""));


        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
//                .withHeaderBackground(R.drawable.img_bg_cover)
                .addProfiles(
                        profile
                )

                .build();

        headerResult.updateProfile(profile);

        //Membuat Sub menu drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Produk").withIcon(R.mipmap.icon_produk).withIdentifier(2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Pesanan Saya").withIcon(R.mipmap.icon_pesanan_saya).withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Konfirmasi Transfer").withIcon(R.mipmap.icon_konfirmasi_transfer).withIdentifier(4),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Setting").withIcon(R.mipmap.icon_setting).withIdentifier(5),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Bantuan").withIcon(R.mipmap.icon_bantuan).withIdentifier(6),
                        new DividerDrawerItem(),
//                        new SecondaryDrawerItem().withName("Tutorial").withIcon(R.mipmap.icon_tutorial).withIdentifier(7),
//                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Tentang Kami").withIcon(R.mipmap.icon_tentang_kami).withIdentifier(8),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Log out").withIcon(R.mipmap.icon_log_out).withIdentifier(9)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Fragment fragment = null;
                            if (drawerItem.getIdentifier() == 11) {
                                fragment = new HomeFragment();
                                txttoolbar.setText("HOME");
                                imgCart.setVisibility(View.VISIBLE);
                                edttextsearch.setVisibility(View.GONE);
                                imgSearch.setVisibility(View.GONE);
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 1) {
                                Bundle b = new Bundle();
                                b.putString("judul", "00");
                                fragment = new KeranjangFragment();
                                fragment.setArguments(b);
                                txttoolbar.setText("KERANJANG BELANJA");
                                imgCart.setVisibility(View.INVISIBLE);
                                edttextsearch.setVisibility(View.GONE);
                                imgSearch.setVisibility(View.GONE);
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 2) {
                                fragment = new ProdukFragment();
                                txttoolbar.setText("PRODUK");
                                imgCart.setVisibility(View.VISIBLE);
                                imgSearch.setVisibility(View.VISIBLE);
                                edttextsearch.setVisibility(View.GONE);
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 3) {
                                fragment = new PesananSayaActivity();
                                txttoolbar.setText("PESANAN SAYA");
                                imgCart.setVisibility(View.VISIBLE);
                                edttextsearch.setVisibility(View.GONE);
                                imgSearch.setVisibility(View.GONE);
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 4) {
                                fragment = new KonfirmasiFragment();
                                txttoolbar.setText("KONFIRMASI PEMBAYARAN");
                                imgCart.setVisibility(View.GONE);
                                edttextsearch.setVisibility(View.GONE);
                                imgSearch.setVisibility(View.GONE);
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 5) {
                                fragment = new ContactFragment();
                                txttoolbar.setText("SETTING");
                                imgCart.setVisibility(View.VISIBLE);
                                edttextsearch.setVisibility(View.GONE);
                                imgSearch.setVisibility(View.GONE);
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 6) {
                                fragment = new BantuanActivity();
                                txttoolbar.setText("BANTUAN");
                                imgCart.setVisibility(View.VISIBLE);
                                edttextsearch.setVisibility(View.GONE);
                                imgSearch.setVisibility(View.GONE);
                                result.closeDrawer();
//                            } else if (drawerItem.getIdentifier() == 7) {
//                                Intent i = new Intent(HomeActivity.this, RegisterActivity.class);
//                                startActivity(i);
                            } else if (drawerItem.getIdentifier() == 8) {
                                Intent i = new Intent(HomeActivity.this, TentangKamiActivity.class);
                                startActivity(i);
                            } else if (drawerItem.getIdentifier() == 9) {
                                ApiClient.getClient().create(ApiInterface.class).logout(api_key, Prefs.getString("Memberkode", ""), Prefs.getString("Memberlasttoken", "")).enqueue(new Callback<ApiLogout>() {
                                    @Override
                                    public void onResponse(Call<ApiLogout> call, Response<ApiLogout> response) {
                                        try{
                                            if (response.body().getResult()) {
                                                Helpers.resetActiveMember();
                                                if (mGoogleApiClient.isConnected()) {
                                                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                                                        @Override
                                                        public void onResult(@NonNull Status status) {
                                                            mGoogleApiClient.connect();
                                                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                                                            Bundle b = new Bundle();
                                                            b.putString("ceklogin", "logout");
                                                            i.putExtras(b);
                                                            startActivity(i);
                                                            finish();
//                                                        LoginManager.getInstance().logOut();
                                                            AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                                            if(accessToken == null)
                                                                return;

                                                            GraphRequest request = GraphRequest.newGraphPathRequest(
                                                                    accessToken,
                                                                    "/me/permissions/user_status",
                                                                    new GraphRequest.Callback() {
                                                                        @Override
                                                                        public void onCompleted(GraphResponse response) {
                                                                            // response
                                                                        }
                                                                    }
                                                            );
                                                            request.setHttpMethod(HttpMethod.DELETE);
                                                            request.executeAsync();
                                                            mGoogleApiClient.disconnect();
                                                        }
                                                    });
                                                }
                                            }else{
                                                Helpers.resetActiveMember();
                                                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                                                Bundle b = new Bundle();
                                                b.putString("ceklogin", "logout");
                                                i.putExtras(b);
                                                startActivity(i);
                                            }
                                        }catch (Exception e){
                                            Toast.makeText(HomeActivity.this, "Mohon maaf ada kendala, mohon di coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiLogout> call, Throwable t) {

                                    }
                                });
                            }
                            //replacing the fragment
                            if (fragment != null) {
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.fFrame, fragment);
                                ft.commit();
                            }
                        }
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();


        //set yang pertama di clcik
        result.setSelection(11);
        //open / close the drawer
        result.closeDrawer();
        //Mendapatkan Drawer Layout
        toolbar.setNavigationIcon(R.mipmap.icon_menu_bar);
        result.getDrawerLayout();

        Bundle back = getIntent().getExtras();
        if(back!=null) {
            if (back.getString("backhome").equals("backkehome")) {
                result.setSelection(2);
            }
        }

        if(Prefs.getString("kodeintenthome","").equals("ok")){
            Fragment fg = new ContactFragment();
            switchContent(R.id.fFrame,fg);
            Prefs.putString("kodeintenthome","");
            txttoolbar.setText("SETTING");
        }else if(Prefs.getString("kodeintenthome","").equals("22")){
            Fragment fg = new KonfirmasiFragment();
            switchContent(R.id.fFrame,fg);
            Prefs.putString("kodeintenthome","");
            txttoolbar.setText("KONFIRMASI PEMBAYARAN");
        }else if(Prefs.getString("kodeintenthome","").equals("33") || Prefs.getString("kodeintenthome","").equals("pesanansaya")) {
            Fragment fg = new PesananSayaActivity();
            switchContent(R.id.fFrame,fg);
            Prefs.putString("kodeintenthome","");
            txttoolbar.setText("PESANAN SAYA");
        }else if(Prefs.getString("kodeintenthome","").equals("setting")){
            Fragment fg = new ContactFragment();
            switchContent(R.id.fFrame,fg);
            Prefs.putString("kodeintenthome","");
            txttoolbar.setText("SETTING");
        }

    }

    private void CekReminder() {
        ApiClient.getClient().create(ApiInterface.class).getReminder(Prefs.getString("Memberkode", ""),Prefs.getString("Memberlasttoken", ""),api_key).enqueue(new Callback<BantuanInsert>() {
            @Override
            public void onResponse(Call<BantuanInsert> call, Response<BantuanInsert> response) {
//                Log.v("Tos", String.valueOf(response.body().getResult()));
                try {
                    if (response.body().getResult()){
//                    Toast.makeText(getContext(),kodeSorting,Toast.LENGTH_LONG).show();
                        if (response.body().getResponse().toString().equals("[]") && response.body().getResult()==true)
                            Toast.makeText(HomeActivity.this,"Data Anda Sudah Masuk",Toast.LENGTH_LONG);
                        else
                            Toast.makeText(HomeActivity.this,"Pesan Anda Gagal",Toast.LENGTH_LONG);
                    }else {
                        CekReminder();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BantuanInsert> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setupGoogleClient(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
    }

    @Override
    public void onBackPressed() {
        if(result.isDrawerOpen()){
            result.closeDrawer();
        }else {
            result.setSelection(11);
            result.closeDrawer();
        }

        if (!imm.isAcceptingText()){
            edttextsearch.setVisibility(View.GONE);
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        new MenuInflater(this).inflate(R.menu.menu_main, menu);
//        return (super.onCreateOptionsMenu(menu));
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_keranjang){
//            Intent i = new Intent(HomeActivity.this, KeranjangActivity.class);
//            Bundle b = new Bundle();
//            b.putString("judul","00");
//            i.putExtras(b);
//            startActivity(i);
//        }
//        return super.onOptionsItemSelected(item);
//    }
    //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (data != null && data.getExtras() != null)
//            for (String key : data.getExtras().keySet())
//                Toast.makeText(this, key + " : " + data.getExtras().get(key).toString(), Toast.LENGTH_SHORT).show();
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
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