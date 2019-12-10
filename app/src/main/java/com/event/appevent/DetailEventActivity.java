package com.event.appevent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.event.appevent.model.Event;
import com.event.appevent.model.Ticket;
import com.event.appevent.model.User;
import com.event.appevent.network.ApiClient;
import com.event.appevent.network.ApiInterface;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEventActivity extends AppCompatActivity {
    Integer idEvent = 0 ;
    Integer uIdEvent = 0;
    ApiInterface mApiInterface;
    ImageView poster;
    Button btn_join, btn_daftar_peserta, btn_lihat_tiket;
    TextView detailNamaEvent;
    TextView detailLokasiEvent;
    TextView detailJamEvent;
    TextView detailTanggalEvent;
    TextView detailDeskripsiEvent;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    Event event;
    SharedPrefManager session;
    User user;
    Intent i;
    Date currentTime;

    Integer uid;
    Integer eid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        btn_join = this.findViewById(R.id.btn_join);
        btn_daftar_peserta = this.findViewById(R.id.btn_daftar_peserta);
        btn_lihat_tiket = this.findViewById(R.id.btn_lihat_tiket);
        poster = this.findViewById(R.id.img_poster_detail);
        detailNamaEvent = this.findViewById(R.id.tv_nama_event_detail);
        detailLokasiEvent = this.findViewById(R.id.tv_lokasi_event_detail);
        detailJamEvent = this.findViewById(R.id.tv_jam_event_detail);
        detailTanggalEvent = this.findViewById(R.id.tv_tanggal_event_detail);
        detailDeskripsiEvent = this.findViewById(R.id.tv_deskripsi_event_detail);

        currentTime = Calendar.getInstance().getTime();

        Intent intent = getIntent();
        idEvent = intent.getIntExtra("idEvent",0);

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
            Log.i("dataUser2", ""+user.getId());
        }

        uIdEvent = intent.getIntExtra("Uid", 0);
        Log.i("user Id", uIdEvent.toString());

        btn_lihat_tiket.setOnClickListener(view -> {
            i = new Intent(DetailEventActivity.this, TiketActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EXTRA_uid",user.getId().toString());
            extras.putString("EXTRA_eid",event.getId().toString());
            extras.putString("EXTRA_namaEvent",event.getNamaEvent());
            extras.putString("EXTRA_lokasiEvent",event.getLokasiEvent());
            extras.putString("EXTRA_tanggalEvent",event.getTanggalEvent());
            extras.putString("EXTRA_jamEvent",event.getJamEvent());

            i.putExtras(extras);

            Log.i("IdEvent",  "uid: "+user.getId()+" eid: "+event.getId());

            startActivity(i);
            join();
        });
        btn_join.setOnClickListener(v -> {
            dialog = new AlertDialog.Builder(DetailEventActivity.this);
            inflater = getLayoutInflater();
            dialogView = inflater.inflate(R.layout.dialog_custom, null);
            dialog.setView(dialogView);
            dialog.setCancelable(true);

            dialog.setPositiveButton("Ya", (dialog, which) -> {
                i = new Intent(DetailEventActivity.this, TiketActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_uid",user.getId().toString());
                extras.putString("EXTRA_eid",event.getId().toString());
                extras.putString("EXTRA_namaEvent",event.getNamaEvent());
                extras.putString("EXTRA_lokasiEvent",event.getLokasiEvent());
                extras.putString("EXTRA_tanggalEvent",event.getTanggalEvent());
                extras.putString("EXTRA_jamEvent",event.getJamEvent());

                i.putExtras(extras);

                Log.i("IdEvent",  "uid: "+user.getId()+" eid: "+event.getId());

                startActivity(i);
                join();
                dialog.dismiss();
            });

            dialog.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());

            dialog.show();
        });

        btn_daftar_peserta.setOnClickListener(view -> {
            Intent i = new Intent(DetailEventActivity.this, PendataanTamuActivity.class);
            i.putExtra("idEvent",event.getId());
            startActivity(i);
        });

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        getEventById();
    }

    public void getEventById() {
        Call<Event> eventCall = mApiInterface.getEventById(idEvent, user.getId());
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

                    String myStrDate = event.getTanggalEvent();
                    SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatOutput = new SimpleDateFormat("dd MMMM yyyy");
                    try {
                        Date date = formatInput.parse(myStrDate);
                        String datetime = formatOutput.format(date);
                        detailTanggalEvent.setText(datetime);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    detailDeskripsiEvent.setText(event.getDeskripsiEvent());
                    Picasso.get().load(event.getBrosurEvent())
                            .fit()
                            .into(poster);

                    // Periksa user id yang login dengan yang membuat event
                    // untuk menghilangkan button JOIN

                    Boolean status = event.getStatusAda();
                    if(session.getUserDetails().getId().equals(uIdEvent)){
                        btn_join.setVisibility(View.GONE);
                        btn_lihat_tiket.setVisibility(View.GONE);
                        btn_daftar_peserta.setVisibility(View.VISIBLE);
                    } else {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                        String formattedDate = df.format(currentTime);
                        String formattedTime = tf.format(currentTime);
                        Log.i("waktu","jam formatted"+formattedTime);
                        Log.i("waktu","jam current"+currentTime.toString());

                        String waktuSekarang = formattedDate+" "+formattedTime;
                        String waktuEvent = event.getTanggalEvent()+" "+event.getJamEvent();

                        Log.i("waktu","jam waktusekarang"+waktuSekarang);
                        Log.i("waktu","jam waktuevent"+waktuEvent);

                        if(waktuSekarang.compareTo(waktuEvent) >= 0) {
                            btn_join.setEnabled(false);
                            btn_join.setText("Event telah berlalu");
                            btn_join.setBackgroundColor(getResources().getColor(R.color.colorGrey));

                        }

                        if(status) {
                            btn_join.setVisibility(View.GONE);
                            btn_lihat_tiket.setVisibility(View.VISIBLE);
                            btn_daftar_peserta.setVisibility(View.GONE);
                        } else {
                            if(event.getJumlahPeserta().equals(event.getJumlahPesertaEvent())) {
                                btn_join.setEnabled(false);
                                btn_join.setText("Kuota Habis");
                                btn_join.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                            }else if(event.getJumlahPesertaEvent()>event.getJumlahPeserta()){
                                btn_join.setVisibility(View.VISIBLE);
                                btn_lihat_tiket.setVisibility(View.GONE);
                                btn_daftar_peserta.setVisibility(View.GONE);
                            }
                        }





                    }

                    detailLokasiEvent.setOnClickListener(view -> {
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q="+event.getLokasiEvent()));
                        startActivity(i);
                    });

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

    public void join() {

        uid = user.getId();
        eid = event.getId();
        Call<Ticket> eventCall = mApiInterface.tambahTicket(uid, eid);
            eventCall.enqueue(new Callback<Ticket>() {
                @Override
                public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                    Toast.makeText(getApplicationContext(),"berhasil", Toast.LENGTH_LONG).show();
                    Log.i("Retrofit Get", response.toString());

                }

                @Override
                public void onFailure(Call<Ticket> call, Throwable t) {
                    Log.e("Retrofit Get", t.toString());
                }
            });
    }
}
