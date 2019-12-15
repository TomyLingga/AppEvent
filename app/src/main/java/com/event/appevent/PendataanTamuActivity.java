package com.event.appevent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendataanTamuActivity extends AppCompatActivity {

    RecyclerView rec_data_tamu;
    DataTamuAdapter dataTamuAdapter;
    List<DataTamu> dataTamuList;
    ApiInterface mApiInterface;
    Integer idEvent = 0;
    Button scan, pdf;

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
        pdf = this.findViewById(R.id.btn_pdf);

        scan.setOnClickListener(view -> {
            Intent i = new Intent(PendataanTamuActivity.this, ScannerActivity.class);
            startActivityForResult(i, 1);
        });

        pdf.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Mengunduh Daftar Tamu", Toast.LENGTH_LONG).show();
            printPdf();
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

    //fungsi tarik data peserta
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

    // fungsi download list data tamu dengan format pdf
    public void printPdf() {
        Call<ResponseBody> pdfCall = mApiInterface.getPdf(idEvent);
        pdfCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                    Log.d("File", String.valueOf(writtenToDisk));
                }
                Toast.makeText(getApplicationContext(), "Daftar Tamu Telah diunduh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();       //path penyimpanan file
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            File futureStudioIconFile = new File(baseDir + File.separator + "Data Tamu " + now + ".pdf"); // format nama file

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("File Download: ", fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
