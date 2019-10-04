package com.event.appevent.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.event.appevent.R;
import com.event.appevent.model.DataTamu;
import com.event.appevent.model.Event;

import java.util.List;

public class DataTamuAdapter extends RecyclerView.Adapter<DataTamuAdapter.DataTamuViewHolder> {

    Context context;
    List<DataTamu> dataTamuList;

    public DataTamuAdapter(Context context, List<DataTamu> dataTamuList) {
        this.context = context;
        this.dataTamuList = dataTamuList;

    }

    @NonNull
    @Override
    public DataTamuAdapter.DataTamuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_tamu, null);
        return new DataTamuAdapter.DataTamuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataTamuViewHolder dataTamuViewHolder, int i) {
        DataTamu dataTamu = dataTamuList.get(i);

        dataTamuViewHolder.namaPeserta.setText(dataTamu.getNamaPeserta());

    }


    @Override
    public int getItemCount() { return dataTamuList.size(); }

    class DataTamuViewHolder extends RecyclerView.ViewHolder {

        TextView namaPeserta;

        public DataTamuViewHolder(@NonNull View itemView) {
            super(itemView);

            namaPeserta = itemView.findViewById(R.id.tv_nama_peserta_tamu);


        }

    }
}