package com.tehvilla.apps.tehvilla.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tehvilla.apps.tehvilla.KeranjangActivity;
import com.tehvilla.apps.tehvilla.R;
import com.tehvilla.apps.tehvilla.models.Keranjang;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class UlasanAdapter extends RecyclerView.Adapter<UlasanAdapter.KeranjangViewHolder> {
    private List<Keranjang> listKeranjang;
    private Context context;
    int jumlahBarang;
    Realm realm;
    public UlasanAdapter(List<Keranjang> listKeranjang, Context context) {
        this.listKeranjang = listKeranjang;
        this.context = context;
    }
    @Override
    public KeranjangViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_ulasan, viewGroup, false);
        Realm.init(context);
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
        holder.judul.setText(ki.getJudul());
        holder.jumlah.setText(""+ki.getJumlah());
        holder.harga.setText("RP. "+df.format(Double.parseDouble(ki.getHarga())));
        Glide.with(context).load(ki.getGambar()).centerCrop().into(holder.gambar);
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
        public KeranjangViewHolder(View v) {
            super(v);
            judul = (TextView) v.findViewById(R.id.judul);
            jumlah = (TextView) v.findViewById(R.id.jumlah);
            harga = (TextView) v.findViewById(R.id.harga);
            gambar = (ImageView) v.findViewById(R.id.gambar);
        }
    }
}