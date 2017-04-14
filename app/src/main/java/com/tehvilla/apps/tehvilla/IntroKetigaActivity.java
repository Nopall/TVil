package com.tehvilla.apps.tehvilla;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntroKetigaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_ketiga);

        //insialisasi variabel
        Button register = (Button) findViewById(R.id.btn_register);
        Button lewati = (Button) findViewById(R.id.btn_lewati);

        //set font nya menjadi calibri light

        //Saat Button register di click
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroKetigaActivity.this, LoginActivity.class);
                Bundle b = new Bundle();
                b.putString("ceklogin", "logout");
                i.putExtras(b);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        //saat button lewati di click
        lewati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(IntroKetigaActivity.this, HomeActivity.class);
                startActivity(j);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }
}
