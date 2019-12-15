package com.event.appevent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    Integer idEvent = 0;
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

        //set value xml
        btn_join = this.findViewById(R.id.btn_join);
        btn_daftar_peserta = this.findViewById(R.id.btn_daftar_peserta);
        btn_lihat_tiket = this.findViewById(R.id.btn_lihat_tiket);
        poster = this.findViewById(R.id.img_poster_detail);
        detailNamaEvent = this.findViewById(R.id.tv_nama_event_detail);
        detailLokasiEvent = this.findViewById(R.id.tv_lokasi_event_detail);
        detailJamEvent = this.findViewById(R.id.tv_jam_event_detail);
        detailTanggalEvent = this.findViewById(R.id.tv_tanggal_event_detail);
        detailDeskripsiEvent = this.findViewById(R.id.tv_deskripsi_event_detail);

        //ambil waktu sekarang
        currentTime = Calendar.getInstance().getTime();

        Intent intent = getIntent();
        idEvent = intent.getIntExtra("idEvent", 0);

        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()) {
            user = session.getUserDetails();
        }

        uIdEvent = intent.getIntExtra("Uid", 0);

        btn_lihat_tiket.setOnClickListener(view -> {
            i = new Intent(DetailEventActivity.this, TiketActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EXTRA_uid", user.getId().toString());
            extras.putString("EXTRA_eid", event.getId().toString());
            extras.putString("EXTRA_namaEvent", event.getNamaEvent());
            extras.putString("EXTRA_lokasiEvent", event.getLokasiEvent());
            extras.putString("EXTRA_tanggalEvent", event.getTanggalEvent());
            extras.putString("EXTRA_jamEvent", event.getJamEvent());

            i.putExtras(extras);
            startActivityForResult(i, 1);
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
                extras.putString("EXTRA_uid", user.getId().toString());
                extras.putString("EXTRA_eid", event.getId().toString());
                extras.putString("EXTRA_namaEvent", event.getNamaEvent());
                extras.putString("EXTRA_lokasiEvent", event.getLokasiEvent());
                extras.putString("EXTRA_tanggalEvent", event.getTanggalEvent());
                extras.putString("EXTRA_jamEvent", event.getJamEvent());

                i.putExtras(extras);
                startActivityForResult(i, 1);
                join();
                dialog.dismiss();
            });

            dialog.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
            dialog.show();
        });

        btn_daftar_peserta.setOnClickListener(view -> {
            Intent i = new Intent(DetailEventActivity.this, PendataanTamuActivity.class);
            i.putExtra("idEvent", event.getId());
            startActivity(i);
        });

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage();
            }
        });

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        getEventById();
    }

    // zoom image
    public void showImage() {
        Dialog builder = new Dialog(this, android.R.style.Theme_Light);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        builder.setOnDismissListener(dialogInterface -> {
            //nothing;
        });

        ImageView imageView = new ImageView(this);
        Picasso.get().load(event.getBrosurEvent())
                .into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getEventById();
            }
        }
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

                    // Format penulisan tanggal event
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

                    Boolean status = event.getStatusAda();

                    // kalau pemilik event membuka detail event, tombol daftar peserta dimunculkan
                    // tombol lain hilang
                    if (session.getUserDetails().getId().equals(uIdEvent)) {
                        btn_join.setVisibility(View.GONE);
                        btn_lihat_tiket.setVisibility(View.GONE);
                        btn_daftar_peserta.setVisibility(View.VISIBLE);
                    } else {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                        String formattedDate = df.format(currentTime);
                        String formattedTime = tf.format(currentTime);
                        String waktuSekarang = formattedDate + " " + formattedTime;
                        String waktuEvent = event.getTanggalEvent() + " " + event.getJamEvent();

                        // pengecekan event masih tersedia atau tidak
                        if (waktuSekarang.compareTo(waktuEvent) >= 0) {
                            btn_join.setEnabled(false);
                            btn_join.setText("Event Telah Kedaluwarsa");
                            btn_join.setBackgroundColor(getResources().getColor(R.color.colorGrey));

                        }

                        // pengecekan apakah user sudah join atau belum
                        if (status) {
                            btn_join.setVisibility(View.GONE);
                            btn_lihat_tiket.setVisibility(View.VISIBLE);
                            btn_daftar_peserta.setVisibility(View.GONE);
                        } else {

                            // pengecekan max jumlah peserta event dengan yang sudah join
                            if (event.getJumlahPeserta().equals(event.getJumlahPesertaEvent())) {
                                btn_join.setEnabled(false);
                                btn_join.setText("Kuota Habis");
                                btn_join.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                            } else if (event.getJumlahPesertaEvent() > event.getJumlahPeserta()) {
                                btn_join.setVisibility(View.VISIBLE);
                                btn_lihat_tiket.setVisibility(View.GONE);
                                btn_daftar_peserta.setVisibility(View.GONE);
                            }
                        }


                    }

                    // Jika klik text view lokasi event, maka akan di arahkan ke Google Maps Navigation
                    // dan mencari lokasi Event
                    detailLokasiEvent.setOnClickListener(view -> {
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q=" + event.getLokasiEvent()));
                        startActivity(i);
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Data Event Tidak Ada", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }

    //fungsi join Event
    public void join() {

        uid = user.getId();
        eid = event.getId();
        Call<Ticket> eventCall = mApiInterface.tambahTicket(uid, eid);
        eventCall.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                Toast.makeText(getApplicationContext(), "Join Berhasil", Toast.LENGTH_LONG).show();
                Log.i("Retrofit Get", response.toString());
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }
}
