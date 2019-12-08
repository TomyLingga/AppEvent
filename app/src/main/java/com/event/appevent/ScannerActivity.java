package com.event.appevent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.event.appevent.model.Event;
import com.event.appevent.model.Ticket;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
//        Log.v("TAG", rawResult.getText()); // Prints scan results
//        Log.v("TAG", rawResult.getBarcodeFormat().toString());

        qrCode = rawResult.getText();
//        Log.i("haha","HAnDle  "+qrCode);

        mScannerView.resumeCameraPreview(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        hadir();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void hadir(){
        Call<Ticket> getHadir = mApiInterface.scanned(qrCode);
//        Log.i("haha","HADIR  "+qrCode);
        getHadir.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(retrofit2.Call<Ticket> call, Response<Ticket> response) {

                Toast.makeText(getApplicationContext(), "SUKSES", Toast.LENGTH_LONG).show();

//                Log.i("haha","HADIR SUKSES");

            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Log.e("haha", t.toString());
            }
        });
    }

}
