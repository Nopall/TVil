package com.tehvilla.apps.tehvilla.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.R;
import com.tehvilla.apps.tehvilla.models.ApiPesananSaya;
import com.tehvilla.apps.tehvilla.models.PesananDetail;
import com.tehvilla.apps.tehvilla.models.PesananSaya;
import com.tehvilla.apps.tehvilla.models.StatusPemesanan;
import com.tehvilla.apps.tehvilla.rest.ApiClient;
import com.tehvilla.apps.tehvilla.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SahabatDeveloper on 2/13/2017.
 */

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> {
    List<PesananDetail> pesananDetail;
    List<PesananSaya> mDetail = new ArrayList<PesananSaya>();
    RecyclerviewAdapter mAdapter;

    Context context;

    public PesananAdapter(List<PesananSaya> mDetail, Context context) {
        this.mDetail = mDetail;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pesanansaya, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //mDetail = mDiscover.get(position).getResponse();
        final PesananSaya pn = mDetail.get(position);
        final StatusPemesanan sp1 = pn.getStatusPemesanan().get(0);
        final StatusPemesanan sp2 = pn.getStatusPemesanan().get(1);
        final StatusPemesanan sp3 = pn.getStatusPemesanan().get(2);
//        final StatusPemesanan sp4 = pn.getStatusPemesanan().get(3);

        pesananDetail = new ArrayList<>();
//        Toast.makeText(context,"aaa"+pn.getKodePemesanan(),Toast.LENGTH_LONG).show();
        holder.nomorPesanan.setText("Nomor ID Order : "+pn.getKodePemesanan());
        holder.tglPesanan.setText("Date : "+pn.getTanggalPemesanan());
        holder.jmlPesanan.setText(""+pn.getPesananDetail().size()+" Barang");
        holder.total.setText("Rp. "+pn.getTotalHarga());

//        Toast.makeText(context,""+sp.getNama(),Toast.LENGTH_LONG).show();

        if(sp1.getKodeTipePembayaran().equals("COD")){
            holder.tglTerkirim.setVisibility(View.INVISIBLE);
            holder.garis1.setVisibility(View.INVISIBLE);
            holder.garis2.setVisibility(View.VISIBLE);
            holder.tv4.setVisibility(View.INVISIBLE);
            holder.img4.setVisibility(View.INVISIBLE);
            holder.tv2.setText("Pengiriman");
            holder.tv3.setText("Terkirim");

            if(sp1.getStatus().equals("1")){
                holder.img1.setBackgroundResource(R.drawable.image_sudah);
                holder.tglOrderditerima.setVisibility(View.VISIBLE);
                holder.tglOrderditerima.setText(sp1.getTanggal().toString());
            }
            if(sp2.getStatus().equals("1")){
                holder.img2.setBackgroundResource(R.drawable.image_sudah);
//                holder.tglPengiriman.setVisibility(View.VISIBLE);
                holder.tglKonfirmbayar.setText(sp2.getTanggal().toString());
            }
            if(sp3.getStatus().equals("1")){
                holder.img3.setBackgroundResource(R.drawable.image_sudah);
//                holder.tglTerkirim.setVisibility(View.VISIBLE);
                holder.tglPengiriman.setText(sp3.getTanggal().toString());
                holder.btnstatus.setBackgroundResource(R.drawable.btn_merah);
            }

        }
        if(sp1.getKodeTipePembayaran().equals("TF")){
            final StatusPemesanan sp4 = pn.getStatusPemesanan().get(3);
            holder.garis1.setVisibility(View.VISIBLE);
            holder.garis2.setVisibility(View.INVISIBLE);
            if(sp1.getStatus().equals("1")){
                holder.img1.setBackgroundResource(R.drawable.image_sudah);
                holder.tglOrderditerima.setText(sp1.getTanggal().toString());
            }
            if(sp2.getStatus().equals("1")){
                holder.img2.setBackgroundResource(R.drawable.image_sudah);
                holder.tglKonfirmbayar.setText(sp2.getTanggal().toString());
            }
            if(sp3.getStatus().equals("1")){
                holder.img3.setBackgroundResource(R.drawable.image_sudah);
                holder.tglPengiriman.setText(sp3.getTanggal().toString());
            }
            if(sp4.getStatus().equals("1")){
                holder.img4.setBackgroundResource(R.drawable.image_sudah);
                holder.tglTerkirim.setText(sp4.getTanggal().toString());
                holder.btnstatus.setBackgroundResource(R.drawable.btn_merah);
            }
        }

        pesananDetail = pn.getPesananDetail();
        mAdapter = new RecyclerviewAdapter(pesananDetail, context);
        mAdapter.notifyDataSetChanged();
        holder.mRecycler.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return mDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableRelativeLayout expandableLayout;
        Button detail, btnstatus;
        TextView total,nomorPesanan,tglPesanan,jmlPesanan,tglOrderditerima,tglKonfirmbayar,tglPengiriman,tglTerkirim,tv1,tv2,tv3,tv4;
        RecyclerView mRecycler;
        ImageView img1,img2,img3,img4;
        LinearLayoutManager mLayout;
        LinearLayout linearexpand;
        View garis1, garis2;

        public ViewHolder(View itemView) {
            super(itemView);
            expandableLayout = (ExpandableRelativeLayout) itemView.findViewById(R.id.expandableLayout1);
            detail = (Button) itemView.findViewById(R.id.btnexpand1);
            btnstatus = (Button) itemView.findViewById(R.id.btnstatus);
            total = (TextView) itemView.findViewById(R.id.totalharga);
            nomorPesanan = (TextView) itemView.findViewById(R.id.nomorOrder);
            tglPesanan = (TextView) itemView.findViewById(R.id.tglPesanan);
            jmlPesanan = (TextView) itemView.findViewById(R.id.jmlPesanan);
            tglOrderditerima = (TextView) itemView.findViewById(R.id.tgl_orderDiTerima);
            tglKonfirmbayar = (TextView) itemView.findViewById(R.id.tgl_konfirmDiTerima);
            tglPengiriman = (TextView) itemView.findViewById(R.id.tgl_Pengiriman);
            tglTerkirim = (TextView) itemView.findViewById(R.id.tgl_terkirim);
            img1 = (ImageView) itemView.findViewById(R.id.imgOrderDiterima);
            img2 = (ImageView) itemView.findViewById(R.id.imgKonfirmDiterima);
            img3 = (ImageView) itemView.findViewById(R.id.imgPengiriman);
            img4 = (ImageView) itemView.findViewById(R.id.imgTerkirim);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            garis1 = (View) itemView.findViewById(R.id.garis1);
            garis2 = (View) itemView.findViewById(R.id.garis2);
            linearexpand = (LinearLayout) itemView.findViewById(R.id.Linearexpand);
            expandableLayout.collapse();
            linearexpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expandableLayout.isExpanded()) {
                        detail.setBackgroundResource(R.drawable.ic_psnsaya);
                        expandableLayout.collapse();

                    }
                    if (!expandableLayout.isExpanded()){
                        detail.setBackgroundResource(R.drawable.ic_psnsaya2);
                        expandableLayout.expand();

                    }
                }
            });

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (expandableLayout.isExpanded()) {
                        detail.setBackgroundResource(R.drawable.ic_psnsaya);
                        expandableLayout.collapse();

                    }
                    if (!expandableLayout.isExpanded()){
                        detail.setBackgroundResource(R.drawable.ic_psnsaya2);
                        expandableLayout.expand();

                    }
                }
            });

            mRecycler = (RecyclerView) itemView.findViewById(R.id.recylerview_detail);
            mRecycler.setHasFixedSize(true);
            mLayout = new LinearLayoutManager(context);
            mLayout.setOrientation(LinearLayoutManager.VERTICAL);
            mRecycler.setLayoutManager(mLayout);
        }
    }
}