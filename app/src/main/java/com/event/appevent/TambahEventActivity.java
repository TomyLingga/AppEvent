package com.event.appevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.event.appevent.model.ResponseEvent;
import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class TambahEventActivity extends AppCompatActivity {
    EditText namaTambahEvent;
    EditText tanggalTambahEvent;
    EditText jamTambahEvent;
    EditText jumlahPesertaTambahEvent;
    EditText lokasiTambahEvent;
    TextView inputNamaBrosurTambahEvent;
    Button inputFileBrosurTambahEvent;
    EditText deskripsiTambahEvent;
    Button tambahEvent;
    ImageView imageView;

    Calendar myCalendar = Calendar.getInstance();
    TimePickerDialog timePickerDialog;
    int currentHour;
    int currentMinute;
    String amPm;

    ApiInterface mApiInterface;
    User user;
    SharedPrefManager session;

    String imagePosterPath;
    private static final int REQUEST_CODE_CHOOSE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);

        inputFileBrosurTambahEvent  = (Button) this.findViewById(R.id.btn_upload_brosur);
        tambahEvent                 = (Button) this.findViewById(R.id.btn_upload_event);
        inputNamaBrosurTambahEvent  = (TextView) this.findViewById(R.id.tv_brosur_event);
        namaTambahEvent             = (EditText) this.findViewById(R.id.edit_nama_event);
        tanggalTambahEvent          = (EditText) this.findViewById(R.id.edit_tanggal_event);
        jamTambahEvent              = (EditText) this.findViewById(R.id.edit_waktu_event);
        jumlahPesertaTambahEvent    = (EditText) this.findViewById(R.id.edit_max_peserta);
        lokasiTambahEvent           = (EditText) this.findViewById(R.id.edit_lokasi_event);
        deskripsiTambahEvent        = (EditText) this.findViewById(R.id.edit_deskripsi_event);
        imageView                   = (ImageView) this.findViewById(R.id.ivAttachment);
        mApiInterface               = ApiClient.getClient().create(ApiInterface.class);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        jamTambahEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar = Calendar.getInstance();
                currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = myCalendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TambahEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = " PM";
                        } else {
                            amPm = " AM";
                        }
                        jamTambahEvent.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        tanggalTambahEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TambahEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
            Log.i("dataUser2", ""+user.getId());
        }

        tambahEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(TambahEventActivity.this, ListEventActivity.class);
                startActivity(intent3);
                tambahEventBaru();
            }
        });

        inputFileBrosurTambahEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePicker();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tanggalTambahEvent.setText(sdf.format(myCalendar.getTime()));
    }

    protected void imagePicker(){
        Pix.start(TambahEventActivity.this, Options.init().setRequestCode(100));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if(returnValue.size()>0){
                imagePosterPath = returnValue.get(0);
                Bitmap bm = BitmapFactory.decodeFile(imagePosterPath);
                imageView.setImageBitmap(bm);
                inputNamaBrosurTambahEvent.setText(imagePosterPath);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imagePicker();
                } else {
                    Toast.makeText(TambahEventActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void tambahEventBaru() {
        String namaEventRequest = namaTambahEvent.getText().toString().trim();
        String tanggalEventRequest = tanggalTambahEvent.getText().toString().trim();
        String jamEventRequest = jamTambahEvent.getText().toString().trim();
        String jumlahPesertaEventRequest = jumlahPesertaTambahEvent.getText().toString().trim();
        String lokasiEventRequest = lokasiTambahEvent.getText().toString().trim();
        String deskripsiEventRequest = deskripsiTambahEvent.getText().toString().trim();
        String uidRequest = user.getId().toString();

        File file = new File(imagePosterPath);

        RequestBody namaEvent           = RequestBody.create(namaEventRequest, MediaType.parse("text/plain"));
        RequestBody tanggalEvent        = RequestBody.create(tanggalEventRequest, MediaType.parse("text/plain") );
        RequestBody jamEvent            = RequestBody.create(jamEventRequest, MediaType.parse("text/plain") );
        RequestBody jumlahPesertaEvent  = RequestBody.create(jumlahPesertaEventRequest, MediaType.parse("text/plain"));
        RequestBody lokasiEvent         = RequestBody.create(lokasiEventRequest, MediaType.parse("text/plain") );
        RequestBody deskripsiEvent      = RequestBody.create(deskripsiEventRequest, MediaType.parse("text/plain"));
        RequestBody uid                 = RequestBody.create(uidRequest, MediaType.parse("number") );

        MultipartBody.Part brosurEvent = MultipartBody.Part.createFormData("brosurEvent", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")));

        Call<ResponseEvent> eventCall = mApiInterface.tambahEvent(uid, namaEvent, tanggalEvent, jamEvent, jumlahPesertaEvent, lokasiEvent,brosurEvent, deskripsiEvent);

        eventCall.enqueue(new Callback<ResponseEvent>() {
            @Override
            public void onResponse(Call<ResponseEvent> call, Response<ResponseEvent>
                    response) {

                if (response.isSuccessful())
                    Log.i("Success", new Gson().toJson(response.body()));
                else
                    Log.i("unSuccess", new Gson().toJson(response.errorBody()));

            }

            @Override
            public void onFailure(Call<ResponseEvent> call, Throwable t) {
                Log.i("gagal", t.toString(), t);
            }
        });
    }
}
