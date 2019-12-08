package com.event.appevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.event.appevent.adapter.EventAdapter;
import com.event.appevent.model.Event;
import com.event.appevent.model.Ticket;
import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class TiketActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SharedPrefManager session;
    TextView namaEvent, waktuEvent, lokasiEvent;
    ImageView qr;
    Button simpan;

    Integer idTicket = 0;
    String idEvent, idUser, namaEvent2, lokasiEvent2, tanggalEvent, jamEvent;
    String hash;

    User user;
    Bitmap bitmap ;
    Ticket ticket;
    Event event;

    public final static int QRcodeWidth = 500 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        namaEvent           = findViewById(R.id.tv_nama_event_tiket);
        waktuEvent          = findViewById(R.id.tv_waktu_event_tiket);
        lokasiEvent         = findViewById(R.id.tv_lokasi_event_tiket);
        qr                  = findViewById(R.id.img_tiket);
        simpan              = findViewById(R.id.btn_download_tiket);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        idUser = extras.getString("EXTRA_uid");
        idEvent= extras.getString("EXTRA_eid");
        namaEvent2= extras.getString("EXTRA_namaEvent");
        lokasiEvent2= extras.getString("EXTRA_lokasiEvent");
        tanggalEvent= extras.getString("EXTRA_tanggalEvent");
        jamEvent= extras.getString("EXTRA_jamEvent");

        Log.i("winri", "id - " + idTicket+" uid: "+idUser+" eid: "+idEvent);

        namaEvent.setText(namaEvent2);
        lokasiEvent.setText(lokasiEvent2);
        waktuEvent.setText(tanggalEvent+" "+jamEvent);

        simpan.setOnClickListener(view -> {
            simpan.setVisibility(View.GONE);
            takeScreenshot();
            Toast.makeText(getApplicationContext(), "Tiket Sudah Tersimpan", Toast.LENGTH_LONG).show();
            simpan.setVisibility(View.VISIBLE);
        });

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
            Log.i("winri", ""+user.getId());
        }
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        getQrHash();
        Log.i("winri", "ini try catch "+hash);

    }

    public void getQrHash(){
        Log.i("winri", "start qr func");
        Log.i("winri", idEvent+" "+idUser);
        Call<Ticket> getTicket = mApiInterface.getTicketById(idEvent, idUser);
        getTicket.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                Log.i("winri", "start qr func3");
                if (response.body() != null) {
                    ticket = response.body();
                    hash = ticket.getQrCode();

                    Log.i("winri", "ini  "+hash);
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try{
                        BitMatrix bitMatrix = multiFormatWriter.encode(hash, BarcodeFormat.QR_CODE,200,200);
                        Log.i("winri", "ini try catch "+hash);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Data Event tidak ada", Toast.LENGTH_LONG).show();
                    Log.i("winri", "hash gagal");

                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

            }
        });


    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}
