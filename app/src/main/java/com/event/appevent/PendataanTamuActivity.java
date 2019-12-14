package com.event.appevent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.event.appevent.adapter.DataTamuAdapter;
import com.event.appevent.model.DataTamu;
import com.event.appevent.model.ListDataTamu;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(v -> {
            super.onBackPressed();
            finish();
        });

        rec_data_tamu = this.findViewById(R.id.rec_data_tamu);
        scan = this.findViewById(R.id.btn_scan);

        scan.setOnClickListener(view -> {
            Intent i = new Intent(PendataanTamuActivity.this, ScannerActivity.class);
            startActivityForResult(i, 1);
        });

        Intent intent = getIntent();
        idEvent = intent.getIntExtra("idEvent", 0);

        rec_data_tamu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_data_tamu.setLayoutManager(layoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        getDataPeserta();

    }

    //onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getDataPeserta();
            }
        }
    }

    public void getDataPeserta() {
        Call<ListDataTamu> tamuCall = mApiInterface.getDataPeserta(idEvent);
        tamuCall.enqueue(new Callback<ListDataTamu>() {
            @Override
            public void onResponse(Call<ListDataTamu> call, Response<ListDataTamu>
                    response) {
                if (response.body() != null) {
                    dataTamuList = response.body().getListDataTamu();
                    dataTamuAdapter = new DataTamuAdapter(dataTamuList);
                    rec_data_tamu.setAdapter(dataTamuAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "Data Peserta Tidak Ada", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ListDataTamu> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }
}
