package com.tehvilla.apps.tehvilla;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntroKeduaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_kedua);

        //insialisasi variabel
        Button intro2 = (Button) findViewById(R.id.btn_intro2);

        //set font menjadi calibri light

        //saat buttoon selanjutnya di click
        intro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroKeduaActivity.this,IntroKetigaActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

    }
}
