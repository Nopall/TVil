package com.tehvilla.apps.tehvilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tehvilla.apps.tehvilla.models.ApiTentangKami;
import com.tehvilla.apps.tehvilla.models.TentangKami;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TentangKamiActivity extends AppCompatActivity {
    @BindView(R.id.slider)SliderLayout SliderBanner;
    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.btn_prev) Button btnPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_kami);
        ButterKnife.bind(this);
        setupJsonTentang();
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SliderBanner.moveNextPosition();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SliderBanner.movePrevPosition();
            }
        });
    }

    private void setupJsonTentang() {

        ApiClient.getClient().create(ApiInterface.class).getTentangKami("7a2594cd-d6ff-440f-a950-f605342f55e4").enqueue(new Callback<ApiTentangKami>() {
            @Override
            public void onResponse(Call<ApiTentangKami> call, Response<ApiTentangKami> response) {

                if (response.body().getResult()){
                    List<TentangKami> listSlider = response.body().getResponse();
                    for(int i = 0; i<listSlider.size();i++){

                        DefaultSliderView textSliderView = new DefaultSliderView(TentangKamiActivity.this);
                        // initialize a SliderLayout
//                        Toast.makeText(TentangKamiActivity.this,""+response.body().getResult()+listSlider.size()+"",Toast.LENGTH_LONG).show();
                        textSliderView
                                .image(listSlider.get(i).getGambar());

                        SliderBanner.addSlider(textSliderView);

                    }
                    SliderBanner.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                    SliderBanner.stopAutoCycle();
                }
            }

            @Override
            public void onFailure(Call<ApiTentangKami> call, Throwable t) {

            }
        });

    }

}
