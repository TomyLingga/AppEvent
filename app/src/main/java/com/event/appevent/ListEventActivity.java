package com.event.appevent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.event.appevent.adapter.EventAdapter;
import com.event.appevent.model.Event;
import com.event.appevent.model.GetEvent;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;

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
    SimpleSearchView searchView;
    Boolean isSearch = false;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        //session login
        session = new SharedPrefManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        setSupportActionBar(toolbar);

        //recycler view
        rec_list_event = this.findViewById(R.id.rec_event);

        FloatingActionButton btn_tambah_event = (FloatingActionButton) this.findViewById(R.id.btn_tambah_event);



        btn_tambah_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ListEventActivity.this, TambahEventActivity.class);
                startActivityForResult(intent4, 1);
            }
        });

        rec_list_event.setLayoutManager(new GridLayoutManager(this, 2));
        rec_list_event.setHasFixedSize(true);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        refresh();

        // fungsi search
        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String cari) {
                isSearch = true;
                Call<GetEvent> eventCall = mApiInterface.search(cari);
                eventCall.enqueue(new Callback<GetEvent>() {
                    @Override
                    public void onResponse(Call<GetEvent> call, Response<GetEvent> response) {
                        if (response.body() != null) {
                            eventList = response.body().getListDataEvent();

                            eventAdapter = new EventAdapter(eventList, ListEventActivity.this, session.getUserDetails().getId(), session.getUserDetails().getName());
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
                Log.d("SimpleSearchView", "Text changed:" + newText);
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");
                return false;
            }
        });
    }

    // Buat fitur menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    // untuk merefresh list data event
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                refresh();
            }
        }
    }

    // fungsi tombol back
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen() || isSearch) {     // jika dalam mode search, reload semua data
            searchView.closeSearch();
            isSearch = false;
            refresh();
        } else {
            dialog = new AlertDialog.Builder(ListEventActivity.this);  // jika tidak dalam mode search, panggil alert dialog
            inflater = getLayoutInflater();
            dialog.setCancelable(true);

            dialog.setTitle("Perhatian");
            dialog.setMessage("Apakah anda yakin ingin keluar ?");

            dialog.setPositiveButton("Ya", (dialog, which) -> {
                ListEventActivity.super.onBackPressed();
                dialog.dismiss();
                //finish();
            });

            dialog.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());

            dialog.show();
        }
    }

    //Fungsi fitur Menu
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
                finish();
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
                    eventAdapter = new EventAdapter(eventList, ListEventActivity.this, session.getUserDetails().getId(), session.getUserDetails().getName());
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
