package com.tehvilla.apps.tehvilla;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.helpers.TehVillaApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelesaiActivity extends AppCompatActivity {
    @BindView(R.id.btnKonfirmPembayaran) Button btnKonfirm;
    @BindView(R.id.btnUtama) Button btnUtama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai);

        ButterKnife.bind(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/calibri_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if(Prefs.getString("pengiriman","").equals("cod"))
            btnKonfirm.setVisibility(View.INVISIBLE);
        else if(Prefs.getString("pengiriman","").equals("tf"))
            btnKonfirm.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SelesaiActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnKonfirmPembayaran)
    public void KonfirmBayar(){
        Intent intent = new Intent(SelesaiActivity.this, KonfirmasiActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnUtama)
    public void Utama(){
        Intent i = new Intent(SelesaiActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
