package com.event.appevent.model;

public class DataTamu {
    String namaPeserta;
    boolean statusKehadiran;

    public DataTamu(){

    }

    public DataTamu(String namaPeserta, Boolean statusKehadiran){
        this.namaPeserta = namaPeserta;
        this.statusKehadiran = statusKehadiran;
    }

    public String getNamaPeserta() {
        return namaPeserta;
    }

    public void setNamaPeserta(String namaPeserta) {
        this.namaPeserta = namaPeserta;
    }

    public boolean isStatusKehadiran() {
        return statusKehadiran;
    }

    public void setStatusKehadiran(boolean statusKehadiran) {
        this.statusKehadiran = statusKehadiran;
    }
}
