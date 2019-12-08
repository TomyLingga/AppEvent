package com.event.appevent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.event.appevent.adapter.DataTamuAdapter;
import com.event.appevent.adapter.EventAdapter;
import com.event.appevent.model.DataTamu;
import com.event.appevent.model.GetEvent;
import com.event.appevent.model.ListDataTamu;
import com.event.appevent.model.Ticket;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendataanTamuActivity extends AppCompatActivity {

    RecyclerView rec_data_tamu;
    DataTamuAdapter dataTamuAdapter;
    List<DataTamu> dataTamuList;
    ApiInterface mApiInterface;
    Integer idEvent = 0;
    Button scan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendataan_tamu);

        rec_data_tamu = this.findViewById(R.id.rec_data_tamu);
        scan = this.findViewById(R.id.btn_scan);

        scan.setOnClickListener(view -> {
            Intent i = new Intent(PendataanTamuActivity.this, ScannerActivity.class);
            startActivityForResult(i, 1);
        });

        Intent intent = getIntent();
        idEvent = intent.getIntExtra("idEvent" , 0);
        Log.i("haha", "id - " + idEvent);

        rec_data_tamu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_data_tamu.setLayoutManager(layoutManager);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        getDataPeserta();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                getDataPeserta();
            }
        }
    }//onActivityResult

    public void getDataPeserta() {
        Call<ListDataTamu> tamuCall = mApiInterface.getDataPeserta(idEvent);
        tamuCall.enqueue(new Callback<ListDataTamu>() {
            @Override
            public void onResponse(Call<ListDataTamu> call, Response<ListDataTamu>
                    response) {
                if (response.body() != null) {
                    dataTamuList = response.body().getListDataTamu();

                    Log.i("haha","  "+dataTamuList);

                    dataTamuAdapter = new DataTamuAdapter(dataTamuList);
                    rec_data_tamu.setAdapter(dataTamuAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "Data Tamu tidak ada", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ListDataTamu> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }
}
