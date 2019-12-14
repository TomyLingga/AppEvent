package com.event.appevent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
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
    AlertDialog.Builder dialog;
    LayoutInflater inflater;

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

        inputFileBrosurTambahEvent  = this.findViewById(R.id.btn_upload_brosur);
        tambahEvent                 = this.findViewById(R.id.btn_upload_event);
        inputNamaBrosurTambahEvent  = this.findViewById(R.id.tv_brosur_event);
        namaTambahEvent             = this.findViewById(R.id.edit_nama_event);
        tanggalTambahEvent          = this.findViewById(R.id.edit_tanggal_event);
        jamTambahEvent              = this.findViewById(R.id.edit_waktu_event);
        jumlahPesertaTambahEvent    = this.findViewById(R.id.edit_max_peserta);
        lokasiTambahEvent           = this.findViewById(R.id.edit_lokasi_event);
        deskripsiTambahEvent        = this.findViewById(R.id.edit_deskripsi_event);
        imageView                   = this.findViewById(R.id.ivAttachment);
        mApiInterface               = ApiClient.getClient().create(ApiInterface.class);

        // date picker
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        // time picker untuk isi jam event
        jamTambahEvent.setOnClickListener(view -> {
            //myCalendar = Calendar.getInstance();
            currentHour = myCalendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = myCalendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(TambahEventActivity.this, (timePicker, hourOfDay, minutes) -> {
                if (hourOfDay >= 12) {
                    amPm = " PM";
                } else {
                    amPm = " AM";
                }
                jamTambahEvent.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);  // format waktu 00:00 PM/AM
            }, currentHour, currentMinute, false);

            timePickerDialog.show();
        });

        //panggil date picker saat isi tanggal
        tanggalTambahEvent.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(TambahEventActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
            Log.i("dataUser2", ""+user.getId());
        }

        tambahEvent.setOnClickListener(v -> konfirmasiTambahEvent());

        inputFileBrosurTambahEvent.setOnClickListener(v -> imagePicker());
    }

    // mengubah format tanggal yang di tampilkan
    private void updateLabel() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tanggalTambahEvent.setText(sdf.format(myCalendar.getTime()));
    }

    // image picker untuk upload brosur event
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
                File file = new File(imagePosterPath);
                inputNamaBrosurTambahEvent.setText(file.getName());
            }else{
                Toast.makeText(getApplicationContext(), "Tolong upload brosur Event anda", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Tolong upload brosur Event anda", Toast.LENGTH_LONG).show();
        }
    }

    // Request Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imagePicker();
            } else {
                Toast.makeText(TambahEventActivity.this, "Approve permissions untuk membuka Pix ImagePicker", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Alert dialog saat Menambahkan Event
    public void konfirmasiTambahEvent(){
        dialog = new AlertDialog.Builder(TambahEventActivity.this);
        inflater = getLayoutInflater();
        dialog.setCancelable(true);

        dialog.setTitle("Tambahkan Event Saya");
        dialog.setMessage("Data Event sudah benar?");

        dialog.setPositiveButton("Ya", (dialog, which) -> {
            tambahEventBaru();
            dialog.dismiss();
        });

        dialog.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());

        dialog.show();
    }

    // fungsi tambah event
    private void tambahEventBaru() {
        if(namaTambahEvent != null &&
                myCalendar != null &&
                jamTambahEvent != null &&
                jumlahPesertaTambahEvent != null &&
                lokasiTambahEvent != null &&
                deskripsiTambahEvent != null &&
                imagePosterPath != null){
        String namaEventRequest = namaTambahEvent.getText().toString().trim();

        // sesuaikan format tanggal yang akan disimpan di database
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String tanggalEventRequest = sdf.format(myCalendar.getTime()).trim();

        String jamEventRequest = jamTambahEvent.getText().toString().trim();
        String jumlahPesertaEventRequest = jumlahPesertaTambahEvent.getText().toString().trim();
        String lokasiEventRequest = lokasiTambahEvent.getText().toString().trim();
        String deskripsiEventRequest = deskripsiTambahEvent.getText().toString().trim();

        // mengambil user id yang buat Event
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

                if (response.isSuccessful()) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    Toast.makeText(getApplicationContext(), "Event berhasil di tambah", Toast.LENGTH_LONG).show();
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "unsukses", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseEvent> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
            }
        });
        }else{
            // Jika data Event belum lengkap di toast seperti ini
            Toast.makeText(getApplicationContext(), "Data Event belum lengkap", Toast.LENGTH_LONG).show();
        }
    }
}
