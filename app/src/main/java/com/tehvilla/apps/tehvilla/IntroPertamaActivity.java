package com.tehvilla.apps.tehvilla;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tehvilla.apps.tehvilla.helpers.Helpers;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroPertamaActivity extends AppCompatActivity {
    @BindView(R.id.btn_intro1) Button btnselanjutnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_pertama);
        ButterKnife.bind(this);
        setupView();
    }

    private void setupView() {
        if (Helpers.getIntro()){
            startActivity(new Intent(IntroPertamaActivity.this, HomeActivity.class));
            finish();
        }
        btnselanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helpers.setIntro(true);
                startActivity(new Intent(IntroPertamaActivity.this, IntroKeduaActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        });
    }
}