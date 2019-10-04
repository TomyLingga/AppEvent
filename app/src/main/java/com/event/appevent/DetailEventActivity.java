package com.event.appevent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.event.appevent.model.Event;
import com.event.appevent.model.EventTes;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEventActivity extends AppCompatActivity {
    Integer idEvent = 0;
    ApiInterface mApiInterface;
    ImageView poster;
    Button btn_join;
    TextView detailNamaEvent;
    TextView detailLokasiEvent;
    TextView detailJamEvent;
    TextView detailTanggalEvent;
    TextView detailDeskripsiEvent;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        btn_join = (Button) this.findViewById(R.id.btn_join);
        poster = (ImageView) this.findViewById(R.id.img_poster_detail);
        detailNamaEvent = (TextView) this.findViewById(R.id.tv_nama_event_detail);
        detailLokasiEvent = (TextView) this.findViewById(R.id.tv_lokasi_event_detail);
        detailJamEvent = (TextView) this.findViewById(R.id.tv_jam_event_detail);
        detailTanggalEvent = (TextView) this.findViewById(R.id.tv_tanggal_event_detail);
        detailDeskripsiEvent = (TextView) this.findViewById(R.id.tv_deskripsi_event_detail);

        Intent intent = getIntent();
        idEvent = intent.getIntExtra("Id", 0);
        Log.i("IdEvent", "id - " + idEvent);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailEventActivity.this, TiketActivity.class);
                startActivity(i);
            }
        });

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        getEventById();
    }

    public void getEventById() {
        Call<Event> eventCall = mApiInterface.getEventById(idEvent);
        eventCall.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event>
                    response) {
                if (response.body() != null) {
                    event = response.body();

                    // set xml jadi nilai event
                    detailNamaEvent.setText(event.getNamaEvent());
                    detailLokasiEvent.setText(event.getLokasiEvent());
                    detailJamEvent.setText(event.getJamEvent());
                    detailTanggalEvent.setText(event.getTanggalEvent());
                    detailDeskripsiEvent.setText(event.getDeskripsiEvent());
                    Picasso.get().load(event.getBrosurEvent())
                            .fit()
                            .into(poster);

                } else {
                    Toast.makeText(getApplicationContext(), "Data Event tidak ada", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }

}
