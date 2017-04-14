package com.tehvilla.apps.tehvilla.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tehvilla.apps.tehvilla.DetailFragment;
import com.tehvilla.apps.tehvilla.HomeActivity;
import com.tehvilla.apps.tehvilla.R;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProdukShorcutAdapter extends RecyclerView.Adapter<ProdukShorcutAdapter.ViewHolder> {
    private Context context;
    private List<Produk> produks;
    public ProdukShorcutAdapter(Context context, List<Produk> produks) {
        this.produks = produks;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shorcut_produk_item, parent, false);
        return new ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTittle;
        ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            txtTittle = (TextView) view.findViewById(R.id.txtJudul);
            imageView = (ImageView) view.findViewById(R.id.imgProduk);
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Produk pd = produks.get(position);
        holder.txtTittle.setText(pd.getNama());
        Glide.with(context).load(pd.getGambar1()).centerCrop().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailFragment.class);
                Bundle kode = new Bundle();
                kode.putString("kode_produk", pd.getKode());
                i.putExtras(kode);
                context.startActivity(i);
            }
        });
    }

//    private void fragmentJump(Produk mItemSelected) {
//        Fragment mFragment = new DetailFragment();
//        Bundle mBundle = new Bundle();
//        mBundle.putString("kode_produk", mItemSelected.getKode());
//        mFragment.setArguments(mBundle);
//        switchContent(R.id.fFrame, mFragment);
//    }

//    public void switchContent(int id, Fragment fragment) {
//        if (context == null)
//            return;
//        if (context instanceof HomeActivity) {
//            HomeActivity mainActivity = (HomeActivity) context;
//            Fragment frag = fragment;
//            mainActivity.switchContent(id, frag);
//        }
//    }

    @Override
    public int getItemCount() {
        return produks.size();
    }
}
