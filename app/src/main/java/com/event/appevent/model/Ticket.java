package com.event.appevent.model;

public class Ticket {
    private Integer id;
    private Integer uidMengikuti;
    private Integer eid;
    private String qrCode;
    private Integer kehadiran;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uidMengikuti;
    }

    public void setUid(Integer uid) {
        this.uidMengikuti = uid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getKehadiran() {
        return kehadiran;
    }

    public void setKehadiran(Integer kehadiran) {
        this.kehadiran = kehadiran;
    }
}
