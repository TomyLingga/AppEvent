package com.event.appevent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.event.appevent.model.Event;
import com.event.appevent.model.Ticket;
import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TiketActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SharedPrefManager session;

    TextView namaEvent, waktuEvent, lokasiEvent;
    ImageView qr;
    Button simpan;

    String idEvent, idUser, namaEvent2, lokasiEvent2, tanggalEvent, jamEvent;
    String hash;

    User user;
    Ticket ticket;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(v -> {
            super.onBackPressed();
            finish();
        });

        namaEvent = findViewById(R.id.tv_nama_event_tiket);
        waktuEvent = findViewById(R.id.tv_waktu_event_tiket);
        lokasiEvent = findViewById(R.id.tv_lokasi_event_tiket);
        qr = findViewById(R.id.img_tiket);
        simpan = findViewById(R.id.btn_download_tiket);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        idUser = extras.getString("EXTRA_uid");
        idEvent = extras.getString("EXTRA_eid");
        namaEvent2 = extras.getString("EXTRA_namaEvent");
        lokasiEvent2 = extras.getString("EXTRA_lokasiEvent");
        tanggalEvent = extras.getString("EXTRA_tanggalEvent");
        jamEvent = extras.getString("EXTRA_jamEvent");

        //ubah format jam dan tanggal
        namaEvent.setText(namaEvent2);
        lokasiEvent.setText(lokasiEvent2);
        String myStrDate = tanggalEvent;
        SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatOutput = new SimpleDateFormat("dd MMMM yyyy");
        try {
            Date date = formatInput.parse(myStrDate);
            String datetime = formatOutput.format(date);
            waktuEvent.setText(datetime + " " + jamEvent);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        simpan.setOnClickListener(view -> {
            simpan.setVisibility(View.GONE);
            takeScreenshot();
            Toast.makeText(getApplicationContext(), "Tiket Sudah Tersimpan di Galeri", Toast.LENGTH_LONG).show();
            simpan.setVisibility(View.VISIBLE);
        });

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
        }
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        getQrHash();

    }

    //Generate QR Code dari data yang dikirim backend
    public void getQrHash() {
        Call<Ticket> getTicket = mApiInterface.getTicketById(idEvent, idUser);
        getTicket.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.body() != null) {
                    ticket = response.body();
                    hash = ticket.getQrCode();

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(hash, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Data Event Tidak Ada", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // untuk download tiket, jika diperlukan
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
