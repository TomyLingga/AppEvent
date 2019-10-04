package com.event.appevent.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEvent {
    @SerializedName("events")
    List<Event> events;

    public List<Event> getListDataEvent() {
        return events;
    }

    public void setListDataEvent(List<Event> listDataEvent) {
        this.events = listDataEvent;
    }

}


