package com.event.appevent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.event.appevent.model.Event;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.vincent.filepicker.activity.ImagePickActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    ApiInterface mApiInterface;

    private static final int INTENT_REQUEST_CODE = 100;



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


            }
        });
    }

//    private void initViews() {
//
//        inputFileBrosurTambahEvent.setOnClickListener((View view) -> {
//
//            Intent intent = new Intent(this, FilePickerActivity.class);
//
////            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////            intent.setType("image/jpeg");
//
//            try {
//                startActivityForResult(intent, INTENT_REQUEST_CODE);
//
//            } catch (ActivityNotFoundException e) {
//
//                e.printStackTrace();
//            }
//
//        });

//        mBtImageShow.setOnClickListener(view -> {
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(mImageUrl));
//            startActivity(intent);
//
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("haha", "onActivityResult");
//        if (requestCode == INTENT_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK) {
//
//                try {
//
//                    InputStream is = getContentResolver().openInputStream(data.getData());
//
//                   // uploadImage(getBytes(is));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

//    private void uploadImage(byte[] imageBytes) {

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
//        Call<Response> call = retrofitInterface.uploadImage(body);
//        mProgressBar.setVisibility(View.VISIBLE);
//        call.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//
//                mProgressBar.setVisibility(View.GONE);
//
//                if (response.isSuccessful()) {
//
//                    Response responseBody = response.body();
//                    mBtImageShow.setVisibility(View.VISIBLE);
//                    mImageUrl = URL + responseBody.getPath();
//                    Snackbar.make(findViewById(R.id.content), responseBody.getMessage(),Snackbar.LENGTH_SHORT).show();
//
//                } else {
//
//                    ResponseBody errorBody = response.errorBody();
//
//                    Gson gson = new Gson();
//
//                    try {
//
//                        Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
//                        Snackbar.make(findViewById(R.id.content), errorResponse.getMessage(),Snackbar.LENGTH_SHORT).show();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//
//                //mProgressBar.setVisibility(View.GONE);
//                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
//            }
//        });
//    }

    private void tambahEventBaru() {
        String namaEvent = namaTambahEvent.getText().toString().trim();
        Log.i("nama User", "nama = " + namaEvent);
        String tanggalEvent = tanggalTambahEvent.getText().toString().trim();
        String jamEvent = jamTambahEvent.getText().toString().trim();
        String jumlahPesertaEvent = jumlahPesertaTambahEvent.getText().toString().trim();
        String lokasiEvent = lokasiTambahEvent.getText().toString().trim();
        String deskripsiEvent = deskripsiTambahEvent.getText().toString().trim();
        //inputNamaBrosurTambahEvent.setText(event.getNamaEvent());

//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
//
//        Call<Event> eventCall = mApiInterface.tambahEvent(namaEvent, tanggalEvent, jamEvent, jumlahPesertaEvent, lokasiEvent, deskripsiEvent);
//        eventCall.enqueue(new Callback<Event>() {
//            @Override
//            public void onResponse(Call<Event> call, Response<Event>
//                    response) {
//                Toast.makeText(getApplicationContext(), "berhasil", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(Call<Event> call, Throwable t) {
//                Log.e("Retrofit Get", t.toString());
//            }
//        });
    }

}
