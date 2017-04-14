package com.tehvilla.apps.tehvilla;

import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.paginate.Paginate;
import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.adapters.ProdukAdapter;
import com.tehvilla.apps.tehvilla.helpers.Utils;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProdukActivity extends AppCompatActivity {
    @BindView(R.id.toolbarsearch) Toolbar toolbarsearch;
    @BindView(R.id.ListProdukSearch) RecyclerView listproduksearch;
    @BindView(R.id.txtKosongProduk) TextView txtKosong;
    @BindView(R.id.txtTeks) TextView txtTeks;
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    private ProdukAdapter produkAdapter;
    private Paginate mPaginate;
    private LinearLayoutManager layoutManager;
    private String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    private String isiSearch="";
    private List<Produk> pds;
    private boolean mIsFinished = false;
    int mCurrentPagesearch = 2;
    int[] jmlhasortingsearch = {0};
    public static EditText edttextsearch2;
    int mCurrentPage = 2;
    int[] jmlha = {10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_produk);
        ButterKnife.bind(this);

//        setupJsonSortingSearch();

        txtTeks.setVisibility(View.VISIBLE);
        listproduksearch.setVisibility(View.INVISIBLE);

        edttextsearch2 = (EditText) toolbarsearch.findViewById(R.id.EdittxtSearch2);
        edttextsearch2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equalsIgnoreCase("")){
                    if (produkAdapter != null) {
                        produkAdapter.clearr();
                        produkAdapter.notifyDataSetChanged();
                    }
                        mCurrentPage = 0;
                        jmlha = new int[]{10};
                        txtKosong.setText("");
                        mIsFinished = false;
                        listproduksearch.setVisibility(View.INVISIBLE);
                        txtTeks.setVisibility(View.VISIBLE);

                }else{
                    if (produkAdapter != null) {
                        produkAdapter.clearr();
                        produkAdapter.notifyDataSetChanged();
                    }
                        txtKosong.setText("");
                        mCurrentPagesearch = 0;
                        jmlhasortingsearch = new int[]{0};
                        isiSearch = charSequence.toString();
                        mIsFinished = false;
                        listproduksearch.setVisibility(View.VISIBLE);
                        txtTeks.setVisibility(View.INVISIBLE);
                        setupJsonSortingSearch();

                }
                return;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        setSupportActionBar(toolbarsearch);
        getSupportActionBar().setHomeAsUpIndicator(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).color(Color.WHITE).actionBar());
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarsearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setupJsonSortingSearch() {
        Log.v("setupjsonsearch", " Mulai");

        if (produkAdapter==null){
            txtKosong.setText("Produk masih kosong");
        }else {
            produkAdapter.clearr();
            produkAdapter.notifyDataSetChanged();
        }
//        pds.clear();
//        produkAdapter.notifyDataSetChanged();
        if (mPaginate!=null){
            mPaginate.unbind();
            mPaginate.setHasMoreDataToLoad(false);
        }
        ApiClient.getClient().create(ApiInterface.class).getProdukSortingSearch(api_key, 0, isiSearch).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
//                Log.v("Tos", String.valueOf(response.body().getResult()));
                layoutManager = new LinearLayoutManager(getApplicationContext());
                try {
                    if (response.body().getResult()){
                        if (response.body().getResponse().toString().equals("[]") && response.body().getResult()==true) {
                            progressBar.setVisibility(View.INVISIBLE);
                            listproduksearch.setVisibility(View.INVISIBLE);
                            txtKosong.setText("Data yang anda cari kosong");

                        }else {
                            listproduksearch.setVisibility(View.VISIBLE);
                            txtKosong.setText("");
                            int k = 1;
                            pds = response.body().getResponse();
                            produkAdapter = new ProdukAdapter(getApplicationContext(), pds);
                            listproduksearch.setHasFixedSize(true);
                            listproduksearch.setLayoutManager(layoutManager);
                            listproduksearch.setAdapter(produkAdapter);
                            produkAdapter.notifyDataSetChanged();
                            mIsFinished = true;
                            mCurrentPagesearch = k;
//                        mSwiperefresh.setEnabled(false);
//                        mSwiperefresh.setRefreshing(false);
                            k++;
                            paginateNextPageSortingSearch();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Mohon maaf ada kendala, harap dicoba kembali", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {

            }
        });
    }

    private void paginateNextPageSortingSearch() {
        Log.v("paginatenextpagesearch", " Mulai");
        final int tothalamansearch = Prefs.getInt("halaman", 0) + 1;
        progressBar.setVisibility(View.VISIBLE);
        Paginate.Callbacks paginateCallbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
//                Log.d("Paginate", "onLoadMore");
                fetchMovieNextPageSortingSearch();
                mIsFinished = false;
                isLoading();
//                Log.d("Paginate", "isloading()");
            }

            @Override
            public boolean isLoading() {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("sizenya", "=" + pds.size());
//                Log.d("isLoadingcurrentpage", "=" + mCurrentPage);
//                Log.d("isLoadingpage", "=" + tothalaman);
                if (mCurrentPagesearch == tothalamansearch)
                {
                    mPaginate.setHasMoreDataToLoad(false);
                    return false;
                }
                return !mIsFinished;
            }

            @Override
            public boolean hasLoadedAllItems() {
                progressBar.setVisibility(View.GONE);
//                Log.d("hasLolItemscurrentpage", "=" + mCurrentPage);
//                Log.d("hasLoadedAllItems", "=" + halaman+1);
                if (mCurrentPagesearch == tothalamansearch) {
                    mPaginate.setHasMoreDataToLoad(false);
                    return true;
                }

                return false;
            }
        };

        mPaginate = Paginate.with(listproduksearch, paginateCallbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
    }

    private void fetchMovieNextPageSortingSearch() {
        Log.v("fetchmoviesearch", " Mulai");
        progressBar.setVisibility(View.VISIBLE);
        Log.v("Limit mulai", "" + jmlhasortingsearch[0]);
//        Toast.makeText(getContext(), "Total limit" + jmlha[0], Toast.LENGTH_SHORT).show();
        ApiClient.getClient().create(ApiInterface.class).getProdukSortingSearch(api_key, jmlhasortingsearch[0], isiSearch).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
//                produkAdapter.clearr();
                if (response.body().getResult()){
                    if (response.body().getResponse().toString().equals("[]") && response.body().getResult()==true){
                        mPaginate.setHasMoreDataToLoad(false);
                        progressBar.setVisibility(View.GONE);
                        return;
                    }else {
                        int gridsize = pds.size();

                        for (Produk produk : response.body().getResponse()) {
                            pds.add(gridsize, produk);
                        }
                        produkAdapter.notifyDataSetChanged();
                        Log.d("search ditambah jadi ", "" + mCurrentPagesearch);
                        Log.d("search sorting jadi ", "" + jmlhasortingsearch[0]);
                        mCurrentPagesearch++;
                        jmlhasortingsearch[0] += 10;
                        mIsFinished = true;
                    }
//                    Toast.makeText(getContext(), "Total limit lagi" + jmlha[0], Toast.LENGTH_SHORT).show();

                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {

            }
        });

    }



    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
//        final MenuItem item_keranjang = menu.findItem(R.id.action_keranjang);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        isiSearch = searchView.getQuery().toString();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                setupJsonSortingSearch();
                if (newText.equalsIgnoreCase("")){

                }else{
                    produkAdapter.clearr();
                    produkAdapter.notifyDataSetChanged();
                    txtKosong.setText("");
                    mCurrentPagesearch = 0;
                    jmlhasortingsearch = new int[]{0};
                    isiSearch=newText;
                    mIsFinished = false;
                    listproduksearch.setVisibility(View.VISIBLE);
                    setupJsonSortingSearch();
                }

                return false;
            }
        });
    }
}
