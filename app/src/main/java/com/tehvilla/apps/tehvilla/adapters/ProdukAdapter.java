package com.tehvilla.apps.tehvilla.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tehvilla.apps.tehvilla.DetailFragment;
import com.tehvilla.apps.tehvilla.HomeActivity;
import com.tehvilla.apps.tehvilla.R;
import com.tehvilla.apps.tehvilla.helpers.StrikeLine;
import com.tehvilla.apps.tehvilla.models.Produk;
import com.tehvilla.apps.tehvilla.models.ProdukSorting;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.name;
import static android.R.attr.switchMinWidth;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {
    private Context context;
    private List<Produk> produks;
    private int num = 1;

    public ProdukAdapter(Context context, List<Produk> produks) {
        this.produks = produks;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.produk_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void swap(List<Produk> datas){
        produks.clear();
        produks.addAll(datas);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView keterangan;
        TextView harga;
        TextView persenDiskon;
        TextView hargaDiskon;
        TextView txtTittle;
        ImageView imgDiskon;
        ImageView imageView;
        View ViewStrike;
        RelativeLayout rltv;
        public ViewHolder(View view) {
            super(view);
//            imgDiskon = (ImageView) view.findViewById(R.id.imgDiskon);
            persenDiskon = (TextView) view.findViewById(R.id.tvDiskon);
            hargaDiskon = (TextView) view.findViewById(R.id.hargaDiskon);
            txtTittle = (TextView) view.findViewById(R.id.judul);
            keterangan = (TextView) view.findViewById(R.id.keterangan);
            harga = (TextView) view.findViewById(R.id.harga);
            imageView = (ImageView) view.findViewById(R.id.poster);
            ViewStrike = (View)view.findViewById(R.id.View_Strike);
            rltv = (RelativeLayout) view.findViewById(R.id.rltv);
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Produk pd = produks.get(position);
//        NumberFormat nf=NumberFormat.getInstance();
        DecimalFormat df= new DecimalFormat("#,##0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        if (Html.fromHtml(pd.getDeskripsi()).length()<50){
            holder.keterangan.setText(Html.fromHtml(pd.getDeskripsi()));
        }else {
            holder.keterangan.setText(Html.fromHtml(pd.getDeskripsi()).subSequence(0, 30)+"...");
        }
        holder.txtTittle.setText(pd.getNama());
        Glide.with(context).load(pd.getGambar1()).centerCrop().into(holder.imageView);
        if(!pd.getDiskon().equals("0")){
            holder.harga.setText("RP. "+df.format(Double.parseDouble(pd.getHargaAwal())));
            holder.ViewStrike.setVisibility(View.VISIBLE);
            holder.persenDiskon.setVisibility(View.VISIBLE);
            holder.hargaDiskon.setText("RP. "+df.format(Double.parseDouble(pd.getHargaAkhir())));
            holder.persenDiskon.setText("DISCOUNT "+pd.getDiskon()+"%");
        }
        else{
            holder.harga.setText("RP. "+df.format(Integer.parseInt(pd.getHargaAwal())));
            holder.persenDiskon.setVisibility(View.INVISIBLE);
            holder.ViewStrike.setVisibility(View.INVISIBLE);
            holder.hargaDiskon.setText("");
        }
        holder.rltv.setOnClickListener(new View.OnClickListener() {
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
    @Override
    public int getItemCount() {
        return produks.size();
//        if(num*10 > produks.size()){
//            return produks.size();
//        }else{
//            return num*10;
//        }
    }

    public void setFilter(List<Produk> produk){
        produks = new ArrayList<>();
        produks.addAll(produk);
        notifyDataSetChanged();
    }

    public void clearr() {
        int size = this.produks.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.produks.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
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

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public Produk getItem(int positon){
        return produks.get(positon);
    }

    public void remove(Produk item) {
        int position = produks.indexOf(item);
        if (position > -1) {
            produks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setItem(List<Produk> item){
        this.produks = item;
    }
}