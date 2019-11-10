package com.event.appevent.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.event.appevent.R;
import com.event.appevent.model.DataTamu;

import java.util.List;

public class DataTamuAdapter extends RecyclerView.Adapter<DataTamuAdapter.DataTamuViewHolder> {

    Context context;
    List<DataTamu> dataTamuList;

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

    @Override
    public void onBindViewHolder(@NonNull DataTamuViewHolder dataTamuViewHolder, int i) {
        DataTamu dataTamu = dataTamuList.get(i);

        dataTamuViewHolder.namaPeserta.setText(dataTamu.getName());
        Log.i("namaPeserta", dataTamu.getName());
    }

    // Errornya sizenya null
    // int java.util.List.size()' on a null object reference2
    @Override
    public int getItemCount() {
        return dataTamuList.size();

    }

    class DataTamuViewHolder extends RecyclerView.ViewHolder {

        TextView namaPeserta;

        public DataTamuViewHolder(@NonNull View itemView) {
            super(itemView);

            namaPeserta = itemView.findViewById(R.id.tv_nama_peserta_tamu);

        }

    }
}