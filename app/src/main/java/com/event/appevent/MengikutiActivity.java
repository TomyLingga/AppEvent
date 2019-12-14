package com.event.appevent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.event.appevent.adapter.EventAdapter;
import com.event.appevent.model.Event;
import com.event.appevent.model.GetEvent;
import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MengikutiActivity extends AppCompatActivity {

    RecyclerView rec_list_mengikuti_event;
    EventAdapter eventAdapter;
    List<Event> eventList;
    ApiInterface mApiInterface;
    User user;
    SharedPrefManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mengikuti);

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();

        }

        rec_list_mengikuti_event = this.findViewById(R.id.rec_mengikuti_event);
        rec_list_mengikuti_event.setLayoutManager(new GridLayoutManager(this, 2));
        rec_list_mengikuti_event.setHasFixedSize(true);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mengikuti();
    }

    public void mengikuti() {
        Call<GetEvent> mengikutiCall = mApiInterface.mengikuti(user.getId());
        mengikutiCall.enqueue(new Callback<GetEvent>() {
            @Override
            public void onResponse(Call<GetEvent> call, Response<GetEvent>
                    response) {
                if (response.body() != null) {
                    eventList = response.body().getListDataEvent();

                    eventAdapter = new EventAdapter(eventList, MengikutiActivity.this, session.getUserDetails().getId(), session.getUserDetails().getName());
                    rec_list_mengikuti_event.setAdapter(eventAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "Data Event Tidak Ada", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GetEvent> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }
}