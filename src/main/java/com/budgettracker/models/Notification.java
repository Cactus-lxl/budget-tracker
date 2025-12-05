package com.budgettracker.models;

import java.sql.Timestamp;

public class Notification {
    private int nid;
    private String message;
    private boolean is_read;
    private Timestamp createDate;
    private int uid;
    private Integer bid;

    public Notification() {}

    public Notification(int nid, String message, boolean is_read, Timestamp createDate, int uid, Integer bid) {
        this.nid = nid;
        this.message = message;
        this.is_read = is_read;
        this.createDate = createDate;
        this.uid = uid;
        this.bid = bid;
    }

    public int getNid() {
        return nid;
    }
    public void setNid(int nid) {
        this.nid = nid;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isRead() {
        return is_read;
    }
    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
    public Timestamp getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public Integer getBid() {
        return bid;
    }
    public void setBid(Integer bid) {
        this.bid = bid;
    }
}
