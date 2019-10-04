package com.event.appevent;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.event.appevent.adapter.EventAdapter;
import com.event.appevent.model.Event;
import com.event.appevent.model.GetEvent;
import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEventActivity extends AppCompatActivity {

    RecyclerView rec_list_event;
    EventAdapter eventAdapter;
    List<Event> eventList;
    ApiInterface mApiInterface;
    User user;
    SharedPrefManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
            Log.i("dataUser2", ""+user.toString());

        }

        rec_list_event = (RecyclerView)this.findViewById(R.id.rec_event_saya);
        rec_list_event.setLayoutManager(new GridLayoutManager(this, 2));
        rec_list_event.setHasFixedSize(true);


        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        refresh();

    }

    public void refresh() {
        Log.i("dataUser3", ""+user.getId());
        Call<GetEvent> eventCall = mApiInterface.getEventByUid(user.getId());
        eventCall.enqueue(new Callback<GetEvent>() {
            @Override
            public void onResponse(Call<GetEvent> call, Response<GetEvent>
                    response) {
                if (response.body() != null) {
                    eventList = response.body().getListDataEvent();

                    eventAdapter = new EventAdapter(eventList);
                    rec_list_event.setAdapter(eventAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "Data Event tidak ada", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GetEvent> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }

}

