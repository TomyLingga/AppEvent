package com.event.appevent.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.event.appevent.R;
import com.event.appevent.model.DataTamu;

import java.util.List;

public class DataTamuAdapter extends RecyclerView.Adapter<DataTamuAdapter.DataTamuViewHolder> {

    Context context;
    List<DataTamu> dataTamuList;
    Integer kehadiran;
    ImageView uncek;
    ImageView cek;

    public DataTamuAdapter(List<DataTamu> dataTamuList) {
        this.dataTamuList = dataTamuList;
    }

    @NonNull
    @Override
    public DataTamuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tamu, parent, false);
        DataTamuViewHolder mViewHolder = new DataTamuViewHolder(view);
        Log.i("namaPeserta","Onkritviewholder");
        return mViewHolder;
    }

    class DataTamuViewHolder extends RecyclerView.ViewHolder {

        TextView namaPeserta;
        //Integer kehadiran;

        public DataTamuViewHolder(@NonNull View itemView) {
            super(itemView);

            namaPeserta = itemView.findViewById(R.id.tv_nama_peserta_tamu);
            uncek = itemView.findViewById(R.id.img_cek_kehadiran);
            cek = itemView.findViewById(R.id.img_cek_kehadiran_true);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull DataTamuViewHolder dataTamuViewHolder, int i) {
        DataTamu dataTamu = dataTamuList.get(i);

        dataTamuViewHolder.namaPeserta.setText(dataTamu.getName());
        kehadiran = dataTamu.getKehadiran();
        Log.i("namaPeserta", dataTamu.getName());
        Log.i("namaPeserta", "kehadiran    "+kehadiran);

        if(kehadiran==1){
            uncek.setVisibility(View.GONE);
            cek.setVisibility(View.VISIBLE);
        }else{
            uncek.setVisibility(View.VISIBLE);
            cek.setVisibility(View.GONE);
        }

//        if(session.getUserDetails().getId().equals(uIdEvent)){
//            btn_join.setVisibility(View.GONE);
//            btn_daftar_peserta.setVisibility(View.VISIBLE);
//        } else {
//            btn_join.setVisibility(View.VISIBLE);
//            btn_daftar_peserta.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return dataTamuList.size();

    }
}