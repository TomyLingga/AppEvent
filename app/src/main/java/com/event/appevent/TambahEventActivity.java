package com.event.appevent;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.event.appevent.model.Event;
import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.event.appevent.network.ApiClient.BASE_URL;

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

    String imagePosterPath;

    User user;
    SharedPrefManager session;

    private static final int REQUEST_CODE_CHOOSE = 100;

    //private ApiInterface apiInterface;



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

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
            Log.i("dataUser2", ""+user.getId());
        }


//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//
//        apiInterface = retrofit.create(ApiInterface.class);

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
                File file = new File(imagePosterPath);
                Log.d("hahaha", "Selected: " + file.getName());

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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i("haha", "onActivityResult");
////        if (requestCode == INTENT_REQUEST_CODE) {
////
////            if (resultCode == RESULT_OK) {
////
////                try {
////
////                    InputStream is = getContentResolver().openInputStream(data.getData());
////
////                   // uploadImage(getBytes(is));
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
//    }

//    public byte[] getBytes(InputStream is) throws IOException {
//        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
//
//        int buffSize = 1024;
//        byte[] buff = new byte[buffSize];
//
//        int len = 0;
//        while ((len = is.read(buff)) != -1) {
//            byteBuff.write(buff, 0, len);
//        }
//
//        return byteBuff.toByteArray();
//    }

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
        String namaEventRequest = namaTambahEvent.getText().toString().trim();
        Log.i("nama event", "nama = " + namaEventRequest);
        String tanggalEventRequest = tanggalTambahEvent.getText().toString().trim();
        String jamEventRequest = jamTambahEvent.getText().toString().trim();
        String jumlahPesertaEventRequest = jumlahPesertaTambahEvent.getText().toString().trim();
        String lokasiEventRequest = lokasiTambahEvent.getText().toString().trim();
        String deskripsiEventRequest = deskripsiTambahEvent.getText().toString().trim();
        //inputNamaBrosurTambahEvent.setText(imagePosterPath);
        File file = new File(imagePosterPath);
        String uidRequest = user.getId().toString();


//        RequestBody namaEvent           = RequestBody.create(MediaType.parse("text/plain"), namaEventRequest);
//        RequestBody tanggalEvent        = RequestBody.create(MediaType.parse("text/plain"), tanggalEventRequest);
//        RequestBody jamEvent            = RequestBody.create(MediaType.parse("text/plain"), jamEventRequest);
//        RequestBody jumlahPesertaEvent  = RequestBody.create(MediaType.parse("text/plain"), jumlahPesertaEventRequest);
//        RequestBody lokasiEvent         = RequestBody.create(MediaType.parse("text/plain"), lokasiEventRequest);
//        RequestBody deskripsiEvent      = RequestBody.create(MediaType.parse("text/plain"), deskripsiEventRequest);
//        RequestBody uid                 = RequestBody.create(MediaType.parse("number"), uidRequest);
        RequestBody namaEvent           = RequestBody.create(MediaType.parse("text/plain"), namaEventRequest);
        RequestBody tanggalEvent        = RequestBody.create(MediaType.parse("text/plain"), tanggalEventRequest);
        RequestBody jamEvent            = RequestBody.create(MediaType.parse("text/plain"), jamEventRequest);
        RequestBody jumlahPesertaEvent  = RequestBody.create(MediaType.parse("text/plain"), jumlahPesertaEventRequest);
        RequestBody lokasiEvent         = RequestBody.create(MediaType.parse("text/plain"), lokasiEventRequest);
        RequestBody deskripsiEvent      = RequestBody.create(MediaType.parse("text/plain"), deskripsiEventRequest);
        RequestBody uid                 = RequestBody.create(MediaType.parse("number"), uidRequest);

        MultipartBody.Part brosurEvent = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        //MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call eventCall = mApiInterface.tambahEvent(uid, namaEvent, tanggalEvent, jamEvent, jumlahPesertaEvent, lokasiEvent, deskripsiEvent, brosurEvent);
        Log.d("responnya","wowo");
        Log.d("responnya","   "+uid+" "+uidRequest);
        Log.d("responnya","   "+namaEvent+"  "+namaEventRequest);
        Log.d("responnya","   "+tanggalEvent+"  "+tanggalEventRequest);
        Log.d("responnya","   "+jamEvent+"   "+jamEventRequest);
        Log.d("responnya","   "+jumlahPesertaEvent+"   "+jumlahPesertaEventRequest);
        Log.d("responnya","   "+lokasiEvent+"  "+lokasiEventRequest);
        Log.d("responnya","   "+deskripsiEvent+"   "+deskripsiEventRequest);
        Log.d("responnya","   "+brosurEvent+"   "+file);

        eventCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response
                    response) {
                    Log.d("responnya", "ini adalah respon " + response.toString());

                    Toast.makeText(getApplicationContext(), "AWOKWAWOKAWOKAWOKAWOK", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });

        if(eventCall.isExecuted()){
            Log.d("responnya", "sukses");

        }else{
            Log.d("responnya", "fail");
        }

    }

}
