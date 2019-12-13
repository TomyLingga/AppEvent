package com.event.appevent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.event.appevent.network.ApiClient.BASE_URL;

public class DaftarActivity extends AppCompatActivity {
    EditText daftarUsername;
    EditText daftarPassword;
    EditText daftarPassword2;
    EditText daftarEmail;
    Button btnDaftar;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        // set value xml
        btnDaftar = this.findViewById(R.id.btn_register);                   //tombol daftar
        daftarUsername = this.findViewById(R.id.edit_username_daftar);      //edit text untuk mengisi nama
        daftarPassword = this.findViewById(R.id.edit_password_daftar);      //edit text untuk mengisi password
        daftarPassword2 = this.findViewById(R.id.edit_password_daftar2);    //edit text untuk mengisi confirm password
        daftarEmail = this.findViewById(R.id.edit_email_daftar);            //edit text untuk mengisi email

        //api interface
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        //onclick tombol daftar
        btnDaftar.setOnClickListener(v -> {

            // panggil funtion kirimData()
            kirimData();


        });


    }

    private void kirimData(){
        // data2 yang sudah diisi tampung ke variable baru
        String namaUser = daftarUsername.getText().toString().trim();
        Log.i("nama User", "nama = "+namaUser);
        String passwordUser = daftarPassword.getText().toString().trim();
        String password2User = daftarPassword2.getText().toString().trim();
        String emailUser = daftarEmail.getText().toString().trim();

        //panggil api interface register, simpan data2 variable diatas
        Call<User> eventCall = mApiInterface.register(namaUser, emailUser, passwordUser, password2User);
        eventCall.enqueue(new Callback<User>() {    //panggil backend
            @Override
            public void onResponse(Call<User> call, Response<User>
                    response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Register Berhasil", Toast.LENGTH_LONG).show();

                    Intent returnIntent = new Intent();            // Balik ke activity sebelumnya (login)
                    setResult(Activity.RESULT_OK, returnIntent);

                    //supaya kalau di tekan back tidak balik ke activity ini lagi tambah kan method finish()
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "Periksa kembali Email atau Password anda", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_LONG).show();
            }
        });
    }
}
