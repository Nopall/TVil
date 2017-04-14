package com.tehvilla.apps.tehvilla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.adapters.PesananAdapter;
import com.tehvilla.apps.tehvilla.models.ApiMember;
import com.tehvilla.apps.tehvilla.models.ApiPesananSaya;
import com.tehvilla.apps.tehvilla.models.PesananSaya;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananSayaActivity extends Fragment {
    @BindView(R.id.rView) RecyclerView recyclerView;
    @BindView(R.id.tvKosong) TextView tvKosong;
    List<PesananSaya> psn;
    PesananAdapter pAdapter;
    Context context;
    String api_key = "7a2594cd-d6ff-440f-a950-f605342f55e4";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pesanan_saya, container, false);
        ButterKnife.bind(this,rootView);
        HomeActivity.txttoolbar.setText("PESANAN SAYA");
        psn = new ArrayList<PesananSaya>();

        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class)
                .getPengguna(api_key, Prefs.getString("Memberkode", ""), Prefs.getString("Memberlasttoken", ""), Prefs.getString("player_id", "")).enqueue(new Callback<ApiMember>() {
            @Override
            public void onResponse(Call<ApiMember> call, Response<ApiMember> response) {
                try {
                    mProgressDialog.dismiss();
                    if (!response.body().getResult()) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        Bundle b = new Bundle();
                        b.putString("ceklogin", "pesanansaya");
                        i.putExtras(b);
                        startActivity(i);
                        ((HomeActivity)getActivity()).finish();

                    }else{

                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Mohon maaf ada kendala, harap coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiMember> call, Throwable t) {

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayout = new LinearLayoutManager(getActivity());
        mLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayout);
        setupListPesananSaya();
    }

    private void setupListPesananSaya() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        ApiClient.getClient().create(ApiInterface.class).discoverPesanan("7a2594cd-d6ff-440f-a950-f605342f55e4", Prefs.getString("Memberkode",""), Prefs.getString("Memberlasttoken","")).enqueue(new Callback<ApiPesananSaya>() {
            @Override
            public void onResponse(Call<ApiPesananSaya> call, Response<ApiPesananSaya> response) {
                try {
                    if (response.body().getResult()){
                        psn = response.body().getResponse();
                        pAdapter = new PesananAdapter(psn, context);
                        pAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(pAdapter);
                        if(psn.size()==0){
                            tvKosong.setText("Pesanan Masih Kosong");
                        }
                    }else{
                        tvKosong.setText("Pesanan Masih Kosong");
                    }
                    mProgressDialog.dismiss();
                }catch (Exception e){
                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), "Mohon maaf ada kendala, harap coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<ApiPesananSaya> call, Throwable t) {

            }
        });
    }

}
