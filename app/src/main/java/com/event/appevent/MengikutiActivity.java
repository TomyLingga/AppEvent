package com.event.appevent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.event.appevent.adapter.EventAdapter;
import com.event.appevent.model.Event;

import java.util.ArrayList;
import java.util.List;

public class MengikutiActivity extends AppCompatActivity {

    RecyclerView rec_list_mengikuti_event;
    EventAdapter eventAdapter;
    List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mengikuti);

        rec_list_mengikuti_event = (RecyclerView) this.findViewById(R.id.rec_mengikuti_event);

//        eventList = new ArrayList<>();
//        eventList.add(new Event("Seminar A", R.drawable.gambar));
//        eventList.add(new Event("Seminar b", R.drawable.gambar));
//        eventList.add(new Event("Seminar c", R.drawable.ic_launcher_background));
//        eventList.add(new Event("Seminar d", R.drawable.ic_launcher_background));
//        eventList.add(new Event("Seminar E", R.drawable.ic_launcher_background));

        rec_list_mengikuti_event.setLayoutManager(new GridLayoutManager(this, 2));
        rec_list_mengikuti_event.setHasFixedSize(true);

//        EventAdapter.RecyclerViewClickListener listener = (view, position) ->    {
//            Intent intent5 = new Intent(MengikutiActivity.this, DetailEventActivity.class);
//            startActivity(intent5);
//        };
//        eventAdapter = new EventAdapter(this, eventList, listener);
//        rec_list_mengikuti_event.setAdapter(eventAdapter);

    }
}