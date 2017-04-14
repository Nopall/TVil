package com.tehvilla.apps.tehvilla.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tehvilla.apps.tehvilla.HomeActivity;
import com.tehvilla.apps.tehvilla.KeranjangActivity;
import com.tehvilla.apps.tehvilla.KeranjangFragment;
import com.tehvilla.apps.tehvilla.R;
import com.tehvilla.apps.tehvilla.models.Keranjang;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class KeranjangFragmentAdapter extends RecyclerView.Adapter<KeranjangFragmentAdapter.KeranjangViewHolder> {
    private List<Keranjang> listKeranjang;
    private Context context;
    int jumlahBarang;
    Realm realm;
    private Fragment fragment;
    public KeranjangFragmentAdapter(List<Keranjang> listKeranjang,Context context) {
        this.listKeranjang = listKeranjang;
        this.context = context;
    }
    @Override
    public KeranjangViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_keranjang, viewGroup, false);
        realm = Realm.getDefaultInstance();
        return new KeranjangViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final KeranjangViewHolder holder, int i) {
        final int k;
        k=i;
        DecimalFormat df= new DecimalFormat("#,##0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        final Keranjang ki = listKeranjang.get(k);
//        Toast.makeText(context,"position :"+k,Toast.LENGTH_LONG).show();
        holder.judul.setText(ki.getJudul());
        holder.jumlah.setText(""+ki.getJumlah());
        holder.harga.setText("RP. "+df.format(Double.parseDouble(ki.getHarga())));
        Glide.with(context).load(ki.getGambar()).centerCrop().into(holder.gambar);

        holder.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlahBarang=0;
                jumlahBarang = ki.getJumlah();
                jumlahBarang++;
                holder.jumlah.setText(""+jumlahBarang);
                updateRecord(k,jumlahBarang);
            }
        });
        holder.kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlahBarang=0;
                jumlahBarang = ki.getJumlah();
                jumlahBarang--;
                if(jumlahBarang>0){
                    holder.jumlah.setText(""+jumlahBarang);
                }
                else{
                    jumlahBarang=0;
                    holder.jumlah.setText(""+jumlahBarang);
                }
                updateRecord(k,jumlahBarang);
            }
        });
        Prefs.putInt("tempJumlah",jumlahBarang);
        holder.btnSilang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord(k);
                Bundle b = new Bundle();
                b.putString("judul", "00");
                fragment = new KeranjangFragment();
                fragment.setArguments(b);
                if (fragment != null) {
                    FragmentTransaction ft = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fFrame, fragment);
                    ft.commit();
                }
//                notifyDataSetChanged();
            }
        });

        if (ki.getJumlah() <= 1){
            holder.kurang.setVisibility(View.INVISIBLE);
        }else {
            holder.kurang.setVisibility(View.VISIBLE);
        }

    }

    public void deleteRecord(int position){
        RealmResults<Keranjang> results = realm.where(Keranjang.class).findAll();
        realm.beginTransaction();
        Keranjang ker = results.get(position);
        ker.deleteFromRealm();
        realm.commitTransaction();
    }

    public void updateRecord(int position,int jumlah){
        RealmResults<Keranjang> results = realm.where(Keranjang.class).findAll();
        realm.beginTransaction();
        Keranjang ker = results.get(position);
        ker.setJumlah(jumlah);
        int total = jumlah*(Integer.parseInt(ker.getHarga()));
        Prefs.putInt("totalkeranjang",total);
        Bundle b = new Bundle();
        b.putString("judul", "00");
        fragment = new KeranjangFragment();
        fragment.setArguments(b);
        if (fragment != null) {
            FragmentTransaction ft = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fFrame, fragment);
            ft.commit();
        }
        realm.commitTransaction();
    }

    @Override
    public int getItemCount() {
        return listKeranjang.size();
    }

    public static class KeranjangViewHolder extends RecyclerView.ViewHolder {
        protected TextView judul;
        protected TextView jumlah;
        protected TextView harga;
        protected ImageView gambar;
        protected ImageButton tambah;
        protected ImageButton kurang;
        protected ImageButton btnSilang;
        public KeranjangViewHolder(View v) {
            super(v);
            btnSilang = (ImageButton) v.findViewById(R.id.silang);
            tambah = (ImageButton) v.findViewById(R.id.btnTambah);
            kurang = (ImageButton) v.findViewById(R.id.btnKurang);
            judul = (TextView) v.findViewById(R.id.judul);
            jumlah = (TextView) v.findViewById(R.id.jumlah);
            harga = (TextView) v.findViewById(R.id.harga);
            gambar = (ImageView) v.findViewById(R.id.gambar);
        }
    }
}