package com.tehvilla.apps.tehvilla;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tehvilla.apps.tehvilla.adapters.ProdukAdapter;
import com.tehvilla.apps.tehvilla.adapters.ProdukShorcutAdapter;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.ApiSlider;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.tehvilla.apps.tehvilla.models.Slider;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment{
    View rootView;
    @BindView(R.id.slider)
    SliderLayout SliderBanner;
    @BindView(R.id.btnMenujuProduk) ImageButton btnProduk;
    @BindView(R.id.progressActivity)
    ProgressRelativeLayout progressRelativeLayout;
    @BindView(R.id.listShorcutproduk)
    RecyclerView listShorcutproduk;
    private ProdukShorcutAdapter produkAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);

//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.setTitle("Home");
        HomeActivity.txttoolbar.setText("HOME");
//        setupBanner();
        setupJsonBanner();
        setupProduk();
        return rootView;
    }
    private void setupProduk() {
        ApiClient.getClient().create(ApiInterface.class).getPostLimitProduk(getString(R.string.api_key), 3).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
                try{
                    if (response.body().getResult()) {
                        List<Produk> pds = response.body().getResponse();
                        produkAdapter = new ProdukShorcutAdapter(getActivity(), pds);
                        listShorcutproduk.setHasFixedSize(true);
                        listShorcutproduk.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                        listShorcutproduk.setAdapter(produkAdapter);
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Harap Periksa Internet Anda", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {
            }
        });
    }
    private void setupJsonBanner() {
        progressRelativeLayout.showLoading();
        ApiClient.getClient().create(ApiInterface.class).getSliderHome(getString(R.string.api_key)).enqueue(new Callback<ApiSlider>() {
            @Override
            public void onResponse(Call<ApiSlider> call, Response<ApiSlider> response) {
                try {
                    if (response.body().getResult()){
                        progressRelativeLayout.showContent();
                        List<Slider> listSlider = response.body().getResponse();
                        for(int i = 0; i<listSlider.size();i++){
                            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                            textSliderView.image(listSlider.get(i).getUrlGambar());
                            SliderBanner.addSlider(textSliderView);
                        }
                        SliderBanner.setCustomIndicator((PagerIndicator) rootView.findViewById(R.id.custom_indicator));
                        SliderBanner.stopAutoCycle();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Mohon maaf ada kendala, harap coba beberapa saat", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiSlider> call, Throwable t) {
            }
        });
    }
//    private void setupBanner() {
//        DefaultSliderView slider1 = new DefaultSliderView(getActivity());
//        slider1.image(R.drawable.gambarslide1);
//        DefaultSliderView slider2 = new DefaultSliderView(getActivity());
//        slider2.image(R.drawable.gambarslide1);
//        DefaultSliderView slider3 = new DefaultSliderView(getActivity());
//        slider3.image(R.drawable.gambarslide1);
//        SliderBanner.setCustomIndicator((PagerIndicator) rootView.findViewById(R.id.custom_indicator));
////        SliderBanner.addSlider(slider1);
////        SliderBanner.addSlider(slider2);
//        SliderBanner.addSlider(slider3);
//        SliderBanner.startAutoCycle();
//    }
    @OnClick(R.id.btnMenujuProduk)
    public void MenujuProduk(){
        HomeActivity.imgSearch.setVisibility(View.VISIBLE);
        ProdukFragment fragment2 = new ProdukFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fFrame, fragment2);
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_keranjang:
                // do s.th.
                Intent i = new Intent(getActivity(), KeranjangActivity.class);
                Bundle b = new Bundle();
                b.putString("judul","00");
                i.putExtras(b);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    ((HomeActivity)getActivity()).finish();

                    return true;

                }

                return false;
            }
        });
    }
}