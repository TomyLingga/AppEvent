package com.event.appevent;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        btn_login = this.findViewById(R.id.btn_login);
        tv_register = this.findViewById(R.id.tv_register);
        email = this.findViewById(R.id.edit_email);
        password = this.findViewById(R.id.edit_password);

        // Session Manager
        session = new SharedPrefManager(getApplicationContext());

        btn_login.setOnClickListener(v -> login());


        tv_register.setOnClickListener(v -> {
            Intent intent2 = new Intent(MainActivity.this, DaftarActivity.class);
            startActivity(intent2);
        });

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public void login() {
        Call<User> loginCall = mApiInterface.login(email.getText().toString(), password.getText().toString());
        loginCall.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User>
                    response) {
                if(response.body() != null) {
                    User user = response.body();

                    Log.i("dataUser", ""+response.body().getId());
                    Log.i("dataUser", ""+user.getName());
                    session.createLoginSession(user);
                    session.getUserDetails();

                } else {
                    Toast.makeText(getApplicationContext(), "Data kosong", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Periksa kembali email dan password anda", Toast.LENGTH_LONG).show();
            }
        });
    }


}
