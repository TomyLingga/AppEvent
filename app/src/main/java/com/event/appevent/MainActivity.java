package com.event.appevent;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    Button btn_login;
    TextView tv_register;
    EditText email;
    EditText password;
    SharedPrefManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set value xml
        btn_login = this.findViewById(R.id.btn_login);      //tombol login
        tv_register = this.findViewById(R.id.tv_register);  //text view register
        email = this.findViewById(R.id.edit_email);         //edit text untuk mengisi email
        password = this.findViewById(R.id.edit_password);   //edit text untuk mengisi password



        // onclick tombol login panggil function login()
        btn_login.setOnClickListener(view ->{
                login();
        });

        //onclick text view register, masuk ke halaman register
        tv_register.setOnClickListener(v -> {
            Intent intent2 = new Intent(MainActivity.this, DaftarActivity.class);
            startActivity(intent2);
        });

        //api interface
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    //untuk merefresh halaman, jika resultnya sama dengan result di activity sebelumnya
    //panggil function login() lagi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                login();
            }
        }
    }

    //function login()
    public void login() {
        //panggil api interface login, kasih parameter email dan password yang diberikan pada edit text
        Call<User> loginCall = mApiInterface.login(email.getText().toString(), password.getText().toString());
        loginCall.enqueue(new Callback<User>() { //panggil backend

            @Override
            public void onResponse(Call<User> call, Response<User>
                    response) {
                if (response.body() != null) {
                    User user = response.body();

                    session.createLoginSession(user);
                    session.getUserDetails();
                    Toast.makeText(getApplicationContext(), "Selamat Datang " + user.getName(), Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Email atau Password Salah", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Periksa Koneksi Internet Anda", Toast.LENGTH_LONG).show();
            }
        });
    }

}
