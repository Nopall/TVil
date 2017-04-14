package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.paginate.Paginate;
import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.adapters.ProdukAdapter;
import com.tehvilla.apps.tehvilla.helpers.FetchCountTask;
import com.tehvilla.apps.tehvilla.helpers.StrikeLine;
import com.tehvilla.apps.tehvilla.helpers.Utils;
import com.tehvilla.apps.tehvilla.models.ApiMessages;
import com.tehvilla.apps.tehvilla.models.ApiTotal;
import com.tehvilla.apps.tehvilla.models.Keranjang;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.tehvilla.apps.tehvilla.models.TotalProduk;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

public class ProdukFragment extends Fragment {
    @BindView(R.id.ListProduk)RecyclerView listProduk;
    @BindView(R.id.btnurutkan)ImageButton btnurutkan;
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    @BindView(R.id.tvKosongProduk)TextView tbkosong;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwiperefresh;
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";
    String kodeSorting = "H1";
    String isiSearch="";
    private ProdukAdapter produkAdapter;
    private List<Produk> pds;
    private String mNotificationsCount = "0";
    private LinearLayoutManager layoutManager;
    private static final int MAX_ITEMS_PER_REQUEST = 20;
    private Paginate mPaginate;
    private boolean mIsFinished = false;
    int mCurrentPage = 2;
    int mCurrentPagesearch = 2;
    int halaman = 0;
    int[] jmlha = {10};
    int[] jmlhasortingbiasa = {10};
    int[] jmlhasortingsearch = {0};
    Realm realm;
    InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_produk, container, false);
        ButterKnife.bind(this, rootView);

        new FetchCountTask().execute();
        realm = Realm.getDefaultInstance();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mSwiperefresh.setEnabled(true);
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.setTitle("Produk");

        HomeActivity.txttoolbar.setText("PRODUK");

        mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupJson();
                mSwiperefresh.setRefreshing(false);
            }
        });

        HomeActivity.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchProdukActivity.class);
                startActivity(i);
            }
        });

        HomeActivity.imgarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_SHORT).show();
            }
        });

        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).getCountProduk(api_key).enqueue(new Callback<ApiTotal>() {
            @Override
            public void onResponse(Call<ApiTotal> call, Response<ApiTotal> response) {
                try {
                    if (response.body().getResult()){
                        List<TotalProduk> tp = response.body().getResponse();
                        String totproduk = tp.get(0).getTotal();
                        int total = Integer.parseInt(totproduk);
                        if (total % 10 != 0){
                            halaman = total/10+1;
                        }else {
                            halaman = total/10;
                        }

//                    Toast.makeText(getContext(), "Total halaman nya"+ halaman, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Total nya"+ total, Toast.LENGTH_SHORT).show();
                        Prefs.putInt("halaman", halaman);
//                    Prefs.putInt("halaman", total);
                        mProgressDialog.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Mohon maaf ada kendala, mohon coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiTotal> call, Throwable t) {

            }
        });

        setupJson();
        setupView();

        return rootView;
    }


    private void loadItems(){
        ApiClient.getClient().create(ApiInterface.class).getPostLimitAwalProduk(api_key, 0).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
                Log.v("Tos", String.valueOf(response.body().getResult()));
                if (response.body().getResult()){
                    pds = response.body().getResponse();
                    produkAdapter = new ProdukAdapter(getActivity(), pds);
                    listProduk.setHasFixedSize(true);
                    listProduk.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listProduk.setAdapter(produkAdapter);
                    listProduk.addOnScrollListener(createInfiniteScrollListener());
                    mSwiperefresh.setRefreshing(true);
//                    Toast.makeText(getContext(),""+pds.size()+"",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {

            }
        });
    }

    public void setupJson() {
//        pds.clear();
//        produkAdapter.notifyDataSetChanged();
        if (mPaginate!=null){
            mPaginate.unbind();
            mPaginate.setHasMoreDataToLoad(false);
        }
        ApiClient.getClient().create(ApiInterface.class).getPostLimitAwalProduk(api_key, 0).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
                layoutManager = new LinearLayoutManager(getActivity());
                try {
                    if (response.body().getResult()){
                        if (response.body().getResponse().toString().equals("[]") && response.body().getResult()==true){
                            tbkosong.setText("Produk masih kosong");
                        }else {
                            tbkosong.setText("");
                            int k = 1;
                            pds = response.body().getResponse();
                            produkAdapter = new ProdukAdapter(getActivity(), pds);
                            listProduk.setHasFixedSize(true);
                            listProduk.setLayoutManager(layoutManager);
                            listProduk.setAdapter(produkAdapter);
                            produkAdapter.notifyDataSetChanged();
                            mIsFinished = true;
                            mCurrentPage = k;
//                            mSwiperefresh.setEnabled(false);
                            mSwiperefresh.setRefreshing(false);
                            k++;
                            paginateNextPage();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Maaf Ada kesalahan, coba beberapa saat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {

            }
        });
        setHasOptionsMenu(true);
    }

    private void paginateNextPage() {
        final int tothalaman = Prefs.getInt("halaman", 0) + 1;
        progressBar.setVisibility(View.VISIBLE);
        Paginate.Callbacks paginateCallbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
//                Log.d("Paginate", "onLoadMore");
                fetchMovieNextPage();
                mIsFinished = false;
                isLoading();
//                Log.d("Paginate", "isloading()");
            }

            @Override
            public boolean isLoading() {
                progressBar.setVisibility(View.VISIBLE);
//                Log.d("sizenya", "=" + pds.size());
//                Log.d("isLoadingcurrentpage", "=" + mCurrentPage);
//                Log.d("isLoadingpage", "=" + tothalaman);
                if (mCurrentPage == tothalaman)
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
                if (mCurrentPage == tothalaman) {
                    mPaginate.setHasMoreDataToLoad(false);
                    return true;
                }

                return false;
            }
        };

        mPaginate = Paginate.with(listProduk, paginateCallbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
    }

    private void fetchMovieNextPage() {
        progressBar.setVisibility(View.VISIBLE);
//        Toast.makeText(getContext(), "Total limit" + jmlha[0], Toast.LENGTH_SHORT).show();
        ApiClient.getClient().create(ApiInterface.class).getPostLimitAwalProduk(api_key, jmlha[0]).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
//                produkAdapter.clearr();
                try {
                    if (response.body().getResult()) {
                        if (response.body().getResponse().toString().equals("[]") && response.body().getResult() == true) {
                            mPaginate.setHasMoreDataToLoad(false);
                            progressBar.setVisibility(View.GONE);
                            return;
                        } else {
                            int gridsize = pds.size();

                            for (Produk produk : response.body().getResponse()) {
                                pds.add(gridsize, produk);
                            }
                            produkAdapter.notifyDataSetChanged();
                            Log.d("setelah ditambah jadi", "" + mCurrentPage);
                            mCurrentPage++;
                            jmlha[0] += 10;
                            mIsFinished = true;
//                    Toast.makeText(getContext(), "Total limit lagi" + jmlha[0], Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Mohon maaf ada kendala, mohon refresh ulang", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {

            }
        });

    }

    private void setupJsonSorting() {
        if (produkAdapter==null){
            tbkosong.setText("Produk masih kosong");
        }else {
            produkAdapter.clearr();
            produkAdapter.notifyDataSetChanged();
        }
//        pds.clear();
//        produkAdapter.notifyDataSetChanged();
            if (mPaginate != null) {
                mPaginate.unbind();
                mPaginate.setHasMoreDataToLoad(false);
            }
            ApiClient.getClient().create(ApiInterface.class).getProdukSorting(api_key, 0, kodeSorting).enqueue(new Callback<ApiMessages>() {
                @Override
                public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
                    Log.v("Tos", String.valueOf(response.body().getResult()));
                    layoutManager = new LinearLayoutManager(getActivity());
                    if (response.body().getResult()) {
                        tbkosong.setText("");
                        int k = 1;
                        pds = response.body().getResponse();
                        produkAdapter = new ProdukAdapter(getActivity(), pds);
                        listProduk.setHasFixedSize(true);
                        listProduk.setLayoutManager(layoutManager);
                        listProduk.setAdapter(produkAdapter);
                        produkAdapter.notifyDataSetChanged();
                        mIsFinished = true;
                        mCurrentPage = k;
//                        mSwiperefresh.setEnabled(false);
//                        mSwiperefresh.setRefreshing(false);
                        k++;
                        paginateNextPageSorting();
                    }
                }

                @Override
                public void onFailure(Call<ApiMessages> call, Throwable t) {

                }
            });
            setHasOptionsMenu(true);

    }

    private void paginateNextPageSorting() {
        final int tothalaman = Prefs.getInt("halaman", 0) + 1;
        progressBar.setVisibility(View.VISIBLE);
        Paginate.Callbacks paginateCallbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
//                Log.d("Paginate", "onLoadMore");
                fetchMovieNextPageSorting();
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
                if (mCurrentPage == tothalaman)
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
                if (mCurrentPage == tothalaman) {
                    mPaginate.setHasMoreDataToLoad(false);
                    return true;
                }

                return false;
            }
        };

        mPaginate = Paginate.with(listProduk, paginateCallbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
    }

    private void fetchMovieNextPageSorting() {
        progressBar.setVisibility(View.VISIBLE);
//        Toast.makeText(getContext(), "Total limit" + jmlha[0], Toast.LENGTH_SHORT).show();
        ApiClient.getClient().create(ApiInterface.class).getProdukSorting(api_key, jmlhasortingbiasa[0], kodeSorting).enqueue(new Callback<ApiMessages>() {
            @Override
            public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
//                produkAdapter.clearr();
                if (response.body().getResult()) {
                    if (response.body().getResponse().toString().equals("[]") && response.body().getResult() == true) {
                        mPaginate.setHasMoreDataToLoad(false);
                        progressBar.setVisibility(View.GONE);
                        return;
                    } else {
                        int gridsize = pds.size();

                        for (Produk produk : response.body().getResponse()) {
                            pds.add(gridsize, produk);
                        }
                        produkAdapter.notifyDataSetChanged();
                        Log.d("setelah ditambah jadi", "" + mCurrentPage);
                        mCurrentPage++;
                        jmlhasortingbiasa[0] += 10;
                        mIsFinished = true;
//                    Toast.makeText(getContext(), "Total limit lagi" + jmlha[0], Toast.LENGTH_SHORT).show();

                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {

            }
        });

    }

    private void setupJsonSortingSearch() {
        Log.v("setupjsonsearch", " Mulai");

        if (produkAdapter==null){
            tbkosong.setText("Produk masih kosong");
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
                layoutManager = new LinearLayoutManager(getActivity());
                try {
                    if (response.body().getResult()){
                        if (response.body().getResponse().toString().equals("[]") && response.body().getResult()==true) {
                            progressBar.setVisibility(View.INVISIBLE);
                            listProduk.setVisibility(View.INVISIBLE);
                            tbkosong.setText("Data yang anda cari kosong");

                        }else {
                            listProduk.setVisibility(View.VISIBLE);
                            tbkosong.setText("");
                            int k = 1;
                            pds = response.body().getResponse();
                            produkAdapter = new ProdukAdapter(getActivity(), pds);
                            listProduk.setHasFixedSize(true);
                            listProduk.setLayoutManager(layoutManager);
                            listProduk.setAdapter(produkAdapter);
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
                    Toast.makeText(getContext(), "Mohon maaf ada kendala, harap dicoba kembali", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiMessages> call, Throwable t) {

            }
        });
        setHasOptionsMenu(true);
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

        mPaginate = Paginate.with(listProduk, paginateCallbacks)
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

    @NonNull
    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(MAX_ITEMS_PER_REQUEST, layoutManager) {
            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                Log.v("Tob", "Aw");

                ApiClient.getClient().create(ApiInterface.class).getPostLimitAwalProduk(api_key, 2).enqueue(new Callback<ApiMessages>() {
                    @Override
                    public void onResponse(Call<ApiMessages> call, Response<ApiMessages> response) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (response.body().getResult()){
                            refreshView(listProduk, new ProdukAdapter(getActivity(), pds), firstVisibleItemPosition);
                        }else{
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessages> call, Throwable t) {

                    }
                });


            }
        };
    }



    private void setupView() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setIcon(R.drawable.icon_hapus);
        builderSingle.setTitle("Pilih Sorting: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Harga Terendah - Tertinggi");
        arrayAdapter.add("Harga Tertinggi - Terendah");
        arrayAdapter.add("Nama A-Z");
        arrayAdapter.add("Nama Z-A");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setMessage(strName);
                builderInner.setTitle("Yang Kamu Pilih ialah");
                final String kSort;
                switch (which){
                    case 0:
                        kSort = "H1";
                        kodeSorting = kSort;
                        mIsFinished = false;
                        setupJsonSorting();
                        jmlhasortingbiasa = new int[]{10};
                        break;
                    case 1:
                        kSort = "H2";
                        kodeSorting = kSort;
                        mIsFinished = false;
                        setupJsonSorting();
                        jmlhasortingbiasa = new int[]{10};
                        break;
                    case 2:
                        kSort = "N1";
                        kodeSorting = kSort;
                        mIsFinished = false;
                        setupJsonSorting();
                        jmlhasortingbiasa = new int[]{10};
                        break;
                    case 3:
                        kSort = "N2";
                        kodeSorting = kSort;
                        mIsFinished = false;
                        setupJsonSorting();
                        jmlhasortingbiasa = new int[]{10};
                        break;
                }
                isiSearch="";
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });

        btnurutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderSingle.show();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final MenuItem badgeCount = menu.findItem(R.id.action_keranjang);
        LayerDrawable icon = (LayerDrawable)badgeCount.getIcon();
        Utils.setBadgeCount(getContext(), icon, mNotificationsCount);
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
                    produkAdapter.clearr();
                    produkAdapter.notifyDataSetChanged();
                    mCurrentPage = 0;
                    jmlha = new int[]{10};
                    tbkosong.setText("");
                    mIsFinished = false;
                    listProduk.setVisibility(View.VISIBLE);
                    setupJson();
                }else{
                    produkAdapter.clearr();
                    produkAdapter.notifyDataSetChanged();
                    tbkosong.setText("");
                    mCurrentPagesearch = 0;
                    jmlhasortingsearch = new int[]{0};
                    isiSearch=newText;
                    mIsFinished = false;
                    listProduk.setVisibility(View.VISIBLE);
                    setupJsonSortingSearch();
                }

                return false;
            }
        });



//        searchView.onActionViewCollapsed();

//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Log.v("Testing bro", "");
//                setupJson();
//                return false;
//            }
//        });
//
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when collapsed
//                setupJson();
//                Log.v("Testing bro", "");
//                return true;  // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when expanded
//                Log.v("Testing bro", "");
//                return true;  // Return true to expand action view
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_keranjang){
            Intent i = new Intent(getActivity(), KeranjangActivity.class);
            Bundle b = new Bundle();
            b.putString("judul","00");
            i.putExtras(b);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

//    private void updateNotificationsBadge(String count) {
//        mNotificationsCount = count;
//
//        // force the ActionBar to relayout its MenuItems.
//        // onCreateOptionsMenu(Menu) will be called again.
//        invalidateOptionsMenu(getActivity());
//    }
//
//    class FetchCountTask extends AsyncTask<Void, Void, String> {
//        String sum = String.valueOf(Prefs.getInt("totalqty", 0));
//
//        @Override
//        protected String doInBackground(Void... params) {
//            // example count. This is where you'd
//            // query your data store for the actual count.
////            RealmResults<Keranjang> results = realm.where(Keranjang.class).findAll();
////            long sum = results.sum("jumlah").longValue();
//            return sum;
//        }
//
//        @Override
//        public void onPostExecute(String count) {
//            updateNotificationsBadge(count);
//        }
//    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    HomeActivity.edttextsearch.setVisibility(View.GONE);
                    HomeActivity.txttoolbar.setText("PRODUK");

                    return true;

                }

                return false;
            }
        });
    }
}
