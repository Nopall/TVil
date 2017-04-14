package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.FacebookAuthorizationException;
import com.facebook.Profile;
import com.onesignal.OneSignal;
import com.tehvilla.apps.tehvilla.helpers.Helpers;
import com.tehvilla.apps.tehvilla.models.ApiLogin;
import com.tehvilla.apps.tehvilla.models.ApiRegister;
import com.tehvilla.apps.tehvilla.models.BantuanInsert;
import com.tehvilla.apps.tehvilla.models.Login;
import com.tehvilla.apps.tehvilla.models.Member;
import com.tehvilla.apps.tehvilla.models.Register;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.loginButton) Button btnFb;
    @BindView(R.id.sign_in_button) Button signInButton;
    @BindView(R.id.tvRegister) TextView tvRegister;
    @BindView(R.id.peringatan) TextView tvPeringatan;
    @BindView(R.id.loginNoTelepon) EditText loginNoTelepon;
    @BindView(R.id.loginPassword) EditText loginPassword;

    String verif,email,firstname,lastname,lahir,kelamin;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";

    private List<String> permissionNeeds = Arrays.asList("user_photos",
            "public_profile", "email", "user_birthday", "user_friends",
            "user_location");

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    private PendingAction pendingAction = PendingAction.NONE;

    private ProgressDialog mProgressDialog;
    private Bundle bc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inisialisasi facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();

        bc = getIntent().getExtras();

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.tehvilla.apps.tehvilla",
//                    PackageManager.GET_SIGNATURES);
//
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
////                Toast.makeText(this, Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }

        setupGoogleSignin();
        getplayerId();

        signInButton.setOnClickListener(this);
        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissionNeeds);
            }
        });
//
//        btnFb.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends, gender, first_name, last_name"));

        Facebook();

        loginNoTelepon.setText("+62");
        Selection.setSelection(loginNoTelepon.getText(), loginNoTelepon.getText().length());
        loginNoTelepon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().contains("+62")){
                    loginNoTelepon.setText("+62");
                    Selection.setSelection(loginNoTelepon.getText(), loginNoTelepon.getText().length());
                }

            }
        });

    }

    private void setupGoogleSignin() {
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
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setScopes(gso.getScopeArray());
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.tvRegister:
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                break;
            // ...
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            Plus.PeopleApi.load(mGoogleApiClient, acct.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                    Person person = loadPeopleResult.getPersonBuffer().get(0);
                    String bth = person.getBirthday();
                    Log.e(TAG,"Person loaded");
                    Log.e(TAG,"GivenName "+person.getName().getGivenName());
                    Log.e(TAG,"FamilyName "+person.getName().getFamilyName());
                    Log.e(TAG,("DisplayName "+person.getDisplayName()));
                    Log.e(TAG,"Gender "+person.getGender());
                    Log.e(TAG,"Url "+person.getUrl());
                    Log.e(TAG,"CurrentLocation "+person.getCurrentLocation());
                    Log.e(TAG,"AboutMe "+person.getAboutMe());
                    Log.e(TAG,"Birthday "+ bth);
                    Log.e(TAG,"Image "+person.getImage());

                    String kelamin = null;
                    if (person.getGender()==0){
                        kelamin = "L";
                    }else if(person.getGender()==1){
                        kelamin = "P";
                    }

                    String birthday = null;
                    if (person.getBirthday() == null){
                        birthday = "0000-00-00";
                    }else {
                        birthday = person.getBirthday();
                    }


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date convertedDate = null;
                    try {
                        convertedDate = dateFormat.parse(birthday);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mProgressDialog = new ProgressDialog(LoginActivity.this);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();
                    ApiClient.getClient().create(ApiInterface.class).setLoginGoogle(api_key, 3, acct.getEmail(),
                            person.getName().getGivenName(), person.getName().getFamilyName(), kelamin, convertedDate, Prefs.getString("player_id", ""))
                            .enqueue(new Callback<ApiLogin>() {
                                @Override
                                public void onResponse(Call<ApiLogin> call, Response<ApiLogin> response) {
                                    final List<Member> rgs = response.body().getResponse();
                                    Helpers.setActiveMember(rgs);

                                        ApiClient.getClient().create(ApiInterface.class).UpdatesIds(api_key,
                                                rgs.get(0).getKode(), Prefs.getString("player_id", "")).enqueue(new Callback<BantuanInsert>() {
                                            @Override
                                            public void onResponse(Call<BantuanInsert> call, Response<BantuanInsert> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<BantuanInsert> call, Throwable t) {

                                            }
                                        });

                                    if (response.body().getResult()){
                                        if (rgs.get(0).getVerifikasi().equals("0")){
                                            Intent i = new Intent(LoginActivity.this, VerifikasiActivity.class);
                                            i.putExtra("kodepengguna", rgs.get(0).getKode());
                                            startActivity(i);
                                            finish();
                                        }else {
                                            if (bc != null) {
                                                if (bc.getString("ceklogin").toString().equals("detailpembayaran")) {
                                                    Intent i = new Intent(LoginActivity.this, DetailPembayaranActivity.class);
                                                    Toast.makeText(LoginActivity.this, rgs.get(0).getProvinsi(), Toast.LENGTH_SHORT).show();
                                                    startActivity(i);
                                                    finish();
                                                    mProgressDialog.dismiss();
                                                } else if (bc.getString("ceklogin").toString().equals("setting")) {
                                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                                    Prefs.putString("kodeintenthome", "setting");
                                                    startActivity(i);
                                                    finish();
                                                    mProgressDialog.dismiss();
                                                } else if (bc.getString("ceklogin").toString().equals("logout")) {
                                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                    mProgressDialog.dismiss();
                                                } else if (bc.getString("ceklogin").toString().equals("pesanansaya")) {
                                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                                    Prefs.putString("kodeintenthome", "pesanansaya");
                                                    startActivity(i);
                                                    finish();
                                                    mProgressDialog.dismiss();
                                                }
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiLogin> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
                                }
                            });

//                    getDataGoogleplus(person.getBirthday(), kelamin, acct.getEmail(),
//                            person.getName().getGivenName(), person.getName().getFamilyName());


                }
            });
//            txtName.setText(personName);
//            txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);
//
////            updateUI(true);
        }
    }

    public void getplayerId(){
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                if (!userId.equalsIgnoreCase("")) {
                    Log.d("LoginActivity", "User ID : " + userId);
                    Prefs.putString("player_id", userId);
//                    ApiClient.getClient().create(ApiInterface.class).UpdatesIds(api_key, , userId).enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                        }
//                    });
                }
            }
        });
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.).setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(LoginActivity.this,"Anda Logut",Toast.LENGTH_LONG).show();
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void Facebook(){
//        btnFb.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        //CallBack Fb

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                            String mAccessToken = loginResult.getAccessToken()
                                    .getToken();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        // TODO Auto-generated method stub
                                        Log.v("LoginActivity",
                                                response.toString());
                                        try {
                                            Log.v("LoginActivity",
                                                    object.toString());

                                            String name= object
                                                    .getString("name");
                                            String email=object
                                                    .getString("email");
                                            String gender = object.getString("gender");
                                            String tgllahir = object.optString("birthday");
                                            String firstname = object.getString("first_name");
                                            String lastname = object.getString("last_name");
                                            JSONObject picJson = object
                                                    .getJSONObject("picture");
                                            JSONObject mData = picJson
                                                    .getJSONObject("data");
                                            String photo= mData
                                                    .getString("url");
                                            Log.v("Pla", name);
                                            Log.v("Pla", email);
                                            Log.v("Pla", photo);
                                            Log.v("Pla", gender);
                                            Log.v("Pla", tgllahir);
                                            Log.v("Pla", firstname);
                                            Log.v("Pla", lastname);

                                            if (object.optJSONObject("birthday")==null){
                                                tgllahir = "00/00/0000";
                                                setFacebookAccount(gender, tgllahir, firstname, lastname, email);
                                            }else {
                                                String tgllahir2 = object.getString("birthday");
                                                setFacebookAccount(gender, tgllahir2, firstname, lastname, email);
                                            }


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block

                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters
                                .putString(
                                        "fields",
                                        "id,name,email,gender,birthday,first_name,last_name,location,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                        handlePendingAction();
                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "LoginManager FacebookCallback onCancel");
                        if (pendingAction != PendingAction.NONE) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i(TAG, "LoginManager FacebookCallback onError");
                        System.out.println("FB Exception :" + exception.getMessage());
                        if (pendingAction != PendingAction.NONE
                                && exception instanceof FacebookAuthorizationException) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Cancel")
                                .setMessage("Tidak Permisi")
                                .setPositiveButton("OK", null).show();
                    }


                });
    }

    private void setFacebookAccount(String gender, String tgllahir, String firstname, String lastname,
                                    String email) {

        String kelamin = null;
        if (gender.equals("male")){
            kelamin = "L";
        }else if(gender.equals("female")){
            kelamin = "P";
        }


//        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
//        Date parsed = null;
//        try {
//            parsed = outputFormat.parse(tgllahir);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        String outputText = outputFormat.format(parsed);

        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(tgllahir);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).setLoginGoogle(api_key, 2, email,
            lastname, firstname, kelamin, date, Prefs.getString("player_id", "")).enqueue(new Callback<ApiLogin>() {
                    @Override
                    public void onResponse(Call<ApiLogin> call, Response<ApiLogin> response) {
                        Log.v("nopal", String.valueOf(response.body().getResult()));
                        final List<Member> rgs = response.body().getResponse();
                        Helpers.setActiveMember(rgs);
                            ApiClient.getClient().create(ApiInterface.class).UpdatesIds(api_key,
                                    rgs.get(0).getKode(), Prefs.getString("player_id", "")).enqueue(new Callback<BantuanInsert>() {
                                @Override
                                public void onResponse(Call<BantuanInsert> call, Response<BantuanInsert> response) {

                                }

                                @Override
                                public void onFailure(Call<BantuanInsert> call, Throwable t) {

                                }
                            });

                        if (response.body().getResult()){

                            if (rgs.get(0).getVerifikasi().equals("0")){
                                Intent i = new Intent(LoginActivity.this, VerifikasiActivity.class);
                                i.putExtra("kodepengguna", rgs.get(0).getKode());
                                startActivity(i);
                                finish();
                            }else {
                                if (bc != null) {
                                    if (bc.getString("ceklogin").toString().equals("detailpembayaran")) {
                                        Intent i = new Intent(LoginActivity.this, DetailPembayaranActivity.class);
                                        startActivity(i);
                                        finish();
                                        mProgressDialog.dismiss();
                                    } else if (bc.getString("ceklogin").toString().equals("setting")) {
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        Prefs.putString("kodeintenthome", "setting");

                                        startActivity(i);
                                        finish();
                                        mProgressDialog.dismiss();
                                    } else if (bc.getString("ceklogin").toString().equals("logout")) {
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);

                                        startActivity(i);
                                        finish();
                                        mProgressDialog.dismiss();
                                    } else if (bc.getString("ceklogin").toString().equals("pesanansaya")) {
                                        Prefs.putString("kodeintenthome", "pesanansaya");
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);

                                        startActivity(i);
                                        finish();
                                        mProgressDialog.dismiss();
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ApiLogin> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;
        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                break;
            case POST_STATUS_UPDATE:
                break;
        }
    }


    @OnClick(R.id.btnLogin)
    public void klikLogin() {
        // TODO submit data to server...
            LoginTelepon();
    }

    @OnClick(R.id.tvRegister)
    public void klikRegister() {
        // TODO submit data to server...
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);
    }

    public void LoginTelepon(){
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        String telepon = loginNoTelepon.getText().toString();
        String pass = loginPassword.getText().toString();
        ApiClient.getClient().create(ApiInterface.class).setLoginTelepon("7a2594cd-d6ff-440f-a950-f605342f55e4",1,telepon,pass, Prefs.getString("player_id", "")).enqueue(new Callback<ApiLogin>() {
            @Override
            public void onResponse(Call<ApiLogin> call, Response<ApiLogin> response) {
                Log.v("Tos", String.valueOf(response.body().getResult()));
                final List<Member> rgs = response.body().getResponse();
                if (!response.body().getResult()){
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Maaf Nomor telpon dan password anda salah", Toast.LENGTH_SHORT).show();
                }else{
                    ApiClient.getClient().create(ApiInterface.class).UpdatesIds(api_key,
                            rgs.get(0).getKode(), Prefs.getString("player_id", "")).enqueue(new Callback<BantuanInsert>() {
                        @Override
                        public void onResponse(Call<BantuanInsert> call, Response<BantuanInsert> response) {

                        }

                        @Override
                        public void onFailure(Call<BantuanInsert> call, Throwable t) {

                        }
                    });
                }



                if (response.body().getResult()){
                    Helpers.setActiveMember(rgs);
                    Toast.makeText(LoginActivity.this,rgs.get(0).getKode(),Toast.LENGTH_LONG).show();
                    verif = rgs.get(0).getVerifikasi();
                    if(verif.equals("0")){
                        Toast.makeText(LoginActivity.this, "Anda Harap Verifikasi Terlebih Dahulu", Toast.LENGTH_SHORT);
                        Intent i = new Intent(LoginActivity.this, VerifikasiActivity.class);
                        i.putExtra("kodepengguna", rgs.get(0).getKode());
                        Helpers.setActiveMember(rgs);
                        startActivity(i);
                        finish();
                    }else if(verif.equals("1")) {
                        Toast.makeText(LoginActivity.this, "Anda berhasil Login", Toast.LENGTH_SHORT);
//                        Bundle b = getIntent().getExtras();
                        if (bc!=null) {
                            if (bc.getString("ceklogin").toString().equals("detailpembayaran")) {
                                Intent i = new Intent(LoginActivity.this, DetailPembayaranActivity.class);

                                startActivity(i);
                                finish();
                                mProgressDialog.dismiss();
                            } else if (bc.getString("ceklogin").toString().equals("setting")) {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                Prefs.putString("kodeintenthome", "setting");

                                startActivity(i);
                                finish();
                                mProgressDialog.dismiss();
                            } else if (bc.getString("ceklogin").toString().equals("logout")) {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);

                                startActivity(i);
                                finish();
                                mProgressDialog.dismiss();
                            } else if (bc.getString("ceklogin").toString().equals("pesanansaya")) {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                Prefs.putString("kodeintenthome", "pesanansaya");

                                startActivity(i);
                                finish();
                                mProgressDialog.dismiss();
                            }
                        }
                    }

                }else{
                    Toast.makeText(LoginActivity.this,"Harap Periksa Nomor Telepon dan Sandi Anda",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiLogin> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Harap Periksa Koneksi Internet Anda",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

        LoginManager.getInstance().logOut();
    }

}
