package com.event.appevent.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.event.appevent.DetailEventActivity;
import com.event.appevent.R;
import com.event.appevent.SharedPrefManager;
import com.event.appevent.model.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {


    private List<Event> eventList;
    Context context;
    Integer uId = 0;
    String uNama;


    public EventAdapter(List<Event> eventList, Activity context, Integer uId, String uNama) {
        this.eventList = eventList;
        this.context = context;
        this.uId = uId;
        this.uNama = uNama;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        EventViewHolder mViewHolder = new EventViewHolder(mView);
        return mViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {

        Event event = eventList.get(i);

        eventViewHolder.eventtitle.setText(event.getNamaEvent());
        Picasso.get().load(event.getBrosurEvent())
                .resize(800, 1000)
                .onlyScaleDown()
                .centerCrop()
                .into(eventViewHolder.eventpicture);

        if(event.getUid().equals(uId)) {
            eventViewHolder.eventauthor.setVisibility(View.VISIBLE);
        }

        eventViewHolder.itemView.setOnClickListener(view -> {
            Intent mIntent = new Intent(view.getContext(), DetailEventActivity.class);
            mIntent.putExtra("idEvent", eventList.get(i).getId());
            Log.i("idPutEvent", "id : "+eventList.get(i).getId());
            mIntent.putExtra("Nama", eventList.get(i).getNamaEvent());
            mIntent.putExtra("Uid", eventList.get(i).getUid());
            view.getContext().startActivity(mIntent);
        });


    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventtitle;
        ImageView eventpicture;
        ImageView eventauthor;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventtitle = itemView.findViewById(R.id.tv_nama_event);
            eventpicture = itemView.findViewById(R.id.img_poster);
            eventauthor = itemView.findViewById(R.id.img_authority);

        }

    }

}
