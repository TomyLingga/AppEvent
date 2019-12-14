package com.event.appevent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.event.appevent.model.Event;
import com.event.appevent.model.Ticket;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    ApiInterface mApiInterface;
    String qrCode;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        qrCode = rawResult.getText();
        mScannerView.resumeCameraPreview(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        hadir();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void hadir() {
        Call<Ticket> getHadir = mApiInterface.scanned(qrCode);
        getHadir.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(retrofit2.Call<Ticket> call, Response<Ticket> response) {

                Toast.makeText(getApplicationContext(), "Data Peserta di Update", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "GAGAL", Toast.LENGTH_LONG).show();
            }
        });
    }

}
