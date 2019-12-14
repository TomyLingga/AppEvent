package com.event.appevent.adapter;

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
        return mViewHolder;
    }

    class DataTamuViewHolder extends RecyclerView.ViewHolder {

        TextView namaPeserta;

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

        // penanda kehadiran
        if (kehadiran == 1) {
            uncek.setVisibility(View.GONE);
            cek.setVisibility(View.VISIBLE);
        } else {
            uncek.setVisibility(View.VISIBLE);
            cek.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataTamuList.size();

    }
}