package com.event.appevent;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import com.event.appevent.adapter.EventAdapter;
import com.event.appevent.model.Event;
import com.event.appevent.model.GetEvent;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEventActivity extends AppCompatActivity {

    RecyclerView rec_list_event;
    EventAdapter eventAdapter;
    List<Event> eventList;
    ApiInterface mApiInterface;
    SharedPrefManager session;
    MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        setSupportActionBar(toolbar);

        rec_list_event = this.findViewById(R.id.rec_event);

        FloatingActionButton btn_tambah_event = (FloatingActionButton) this.findViewById(R.id.btn_tambah_event);

        session = new SharedPrefManager(getApplicationContext());

        btn_tambah_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ListEventActivity.this, TambahEventActivity.class);
                startActivity(intent4);
            }
        });

        rec_list_event.setLayoutManager(new GridLayoutManager(this, 2));
        rec_list_event.setHasFixedSize(true);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        refresh();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String cari) {
                Toast.makeText(getBaseContext(), cari, Toast.LENGTH_LONG).show();
                Call<GetEvent> eventCall = mApiInterface.search(cari);
                eventCall.enqueue(new Callback<GetEvent>() {
                    @Override
                    public void onResponse(Call<GetEvent> call, Response<GetEvent> response) {
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

                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                Log.i("haha","newText"+newText);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.header1:
                Intent intent6 = new Intent(ListEventActivity.this, MyEventActivity.class);
                startActivity(intent6);
                return true;
            case R.id.header2:
                Intent intent7 = new Intent(ListEventActivity.this, MengikutiActivity.class);
                startActivity(intent7);
                return true;
            case R.id.header3:
                session.logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        Call<GetEvent> eventCall = mApiInterface.getEvent();
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
