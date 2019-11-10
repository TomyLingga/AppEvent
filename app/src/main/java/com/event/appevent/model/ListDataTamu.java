package com.event.appevent.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListDataTamu {
    @SerializedName("peserta")
    List<DataTamu> dataTamus;

    public List<DataTamu> getListDataTamu() {
        return dataTamus;
    }

    public void setListDataTamu(List<DataTamu> listDataTamu) {
        this.dataTamus = listDataTamu;
    }

}
