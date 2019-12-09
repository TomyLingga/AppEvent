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

        btnDaftar = this.findViewById(R.id.btn_register);
        daftarUsername = this.findViewById(R.id.edit_username_daftar);
        daftarPassword = this.findViewById(R.id.edit_password_daftar);
        daftarPassword2 = this.findViewById(R.id.edit_password_daftar2);
        daftarEmail = this.findViewById(R.id.edit_email_daftar);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnDaftar.setOnClickListener(v -> {

            kirimData();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });


    }

    private void kirimData(){
        String namaUser = daftarUsername.getText().toString().trim();
        Log.i("nama User", "nama = "+namaUser);
        String passwordUser = daftarPassword.getText().toString().trim();
        String password2User = daftarPassword2.getText().toString().trim();
        String emailUser = daftarEmail.getText().toString().trim();

        Call<User> eventCall = mApiInterface.register(namaUser, emailUser, passwordUser, password2User);
        eventCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User>
                    response) {
                        Toast.makeText(getApplicationContext(),"berhasil", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }
}
