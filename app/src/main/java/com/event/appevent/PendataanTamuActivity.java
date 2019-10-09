package com.event.appevent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.event.appevent.adapter.DataTamuAdapter;
import com.event.appevent.model.DataTamu;

import java.util.ArrayList;
import java.util.List;

public class PendataanTamuActivity extends AppCompatActivity {

    RecyclerView rec_data_tamu;
    DataTamuAdapter dataTamuAdapter;
    List<DataTamu> dataTamuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendataan_tamu);

        rec_data_tamu = (RecyclerView) this.findViewById(R.id.rec_data_tamu);

        dataTamuList = new ArrayList<>();
        dataTamuList.add(new DataTamu("Narto1", false));
        dataTamuList.add(new DataTamu("Narto2", false));
        dataTamuList.add(new DataTamu("Narto3", false));
        dataTamuList.add(new DataTamu("Narto4", false));

        rec_data_tamu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_data_tamu.setLayoutManager(layoutManager);


        dataTamuAdapter = new DataTamuAdapter(this, dataTamuList);
        rec_data_tamu.setAdapter(dataTamuAdapter);

    }
}
