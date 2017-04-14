package com.tehvilla.apps.tehvilla.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tehvilla.apps.tehvilla.R;
import com.tehvilla.apps.tehvilla.models.PesananDetail;
import com.tehvilla.apps.tehvilla.models.PesananSaya;

import java.util.List;

/**
 * Created by AkhmadNaufal on 2/15/17.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    List<PesananDetail> mDetail;
    Context context;

    public RecyclerviewAdapter(List<PesananDetail> mDetail, Context context) {
        this.mDetail = mDetail;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PesananDetail pdt = mDetail.get(position);
        holder.judul.setText(pdt.getNamaProduk().toString());
        holder.jumlah.setText(pdt.getJumlahBeli().toString());
        holder.harga.setText(pdt.getHargaSatuan().toString());
        Glide.with(holder.gambarProduk.getContext()).load(pdt.getUrlGambar()).centerCrop().into(holder.gambarProduk);
//        Toast.makeText(context,"size"+mDetail.size(),Toast.LENGTH_LONG).show();
    }


    @Override
    public int getItemCount() {
        return mDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul, harga, jumlah;
        ImageView gambarProduk;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            gambarProduk = (ImageView) itemView.findViewById(R.id.gambarProduk);
            judul = (TextView) itemView.findViewById(R.id.judul);
            harga = (TextView) itemView.findViewById(R.id.harga);
            jumlah = (TextView) itemView.findViewById(R.id.jumlah);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }
}