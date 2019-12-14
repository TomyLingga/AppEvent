package com.event.appevent.model;


public class Event {
    private Integer jumlahPeserta;
    private Boolean statusAda;
    private Integer id;
    private Integer uid;
    private String namaEvent;
    private String tanggalEvent;
    private String jamEvent;
    private Integer jumlahPesertaEvent;
    private String lokasiEvent;
    private String brosurEvent;
    private String deskripsiEvent;
    private String qrEvent;
    private String created_at;
    private String updated_at;

    public Event() {
    }

    public Event(Integer jumlahPeserta, Boolean statusAda, Integer id, Integer uid, String namaEvent, String tanggalEvent, String jamEvent, Integer jumlahPesertaEvent, String lokasiEvent, String brosurEvent, String deskripsiEvent, String qrEvent, String created_at, String updated_at) {
        this.jumlahPeserta = jumlahPeserta;
        this.statusAda = statusAda;
        this.id = id;
        this.uid = uid;
        this.namaEvent = namaEvent;
        this.tanggalEvent = tanggalEvent;
        this.jamEvent = jamEvent;
        this.jumlahPesertaEvent = jumlahPesertaEvent;
        this.lokasiEvent = lokasiEvent;
        this.brosurEvent = brosurEvent;
        this.deskripsiEvent = deskripsiEvent;
        this.qrEvent = qrEvent;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getJumlahPeserta() {
        return jumlahPeserta;
    }

    public void setJumlahPeserta(Integer jumlahPeserta) {
        this.jumlahPeserta = jumlahPeserta;
    }

    public Boolean getStatusAda() {
        return statusAda;
    }

    public void setStatusAda(Boolean statusAda) {
        this.statusAda = statusAda;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNamaEvent() {
        return namaEvent;
    }

    public void setNamaEvent(String namaEvent) {
        this.namaEvent = namaEvent;
    }

    public String getTanggalEvent() {
        return tanggalEvent;
    }

    public void setTanggalEvent(String tanggalEvent) {
        this.tanggalEvent = tanggalEvent;
    }

    public String getJamEvent() {
        return jamEvent;
    }

    public void setJamEvent(String jamEvent) {
        this.jamEvent = jamEvent;
    }

    public Integer getJumlahPesertaEvent() {
        return jumlahPesertaEvent;
    }

    public void setJumlahPesertaEvent(Integer jumlahPesertaEvent) {
        this.jumlahPesertaEvent = jumlahPesertaEvent;
    }

    public String getLokasiEvent() {
        return lokasiEvent;
    }

    public void setLokasiEvent(String lokasiEvent) {
        this.lokasiEvent = lokasiEvent;
    }

    public String getBrosurEvent() {
        return brosurEvent;
    }

    public void setBrosurEvent(String brosurEvent) {
        this.brosurEvent = brosurEvent;
    }

    public String getDeskripsiEvent() {
        return deskripsiEvent;
    }

    public void setDeskripsiEvent(String deskripsiEvent) {
        this.deskripsiEvent = deskripsiEvent;
    }

    public String getQrEvent() {
        return qrEvent;
    }

    public void setQrEvent(String qrEvent) {
        this.qrEvent = qrEvent;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}