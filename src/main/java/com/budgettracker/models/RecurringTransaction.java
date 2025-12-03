package com.budgettracker.models;

import java.math.BigDecimal;
import java.sql.Date;

public class RecurringTransaction {
    private int rid;
    private BigDecimal amount;
    private java.sql.Date s_date;
    private java.sql.Date e_date;
    private boolean is_active;
    private String frequency;
    private String type;
    private String description;
    private int uid;
    private int cid;

    public RecurringTransaction() {}

    public RecurringTransaction(int rid, BigDecimal amount, java.sql.Date s_date, java.sql.Date e_date, boolean is_active, String frequency, String type, String description,  int uid, int cid) {
        this.rid = rid;
        this.amount = amount;
        this.s_date = s_date;
        this.e_date = e_date;
        this.is_active = is_active;
        this.frequency = frequency;
        this.type = type;
        this.description = description;
        this.uid = uid;
        this.cid = cid;
    }

    public int getRid() {
        return rid;
    }
    public void setRid(int rid) {
        this.rid = rid;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public java.sql.Date getS_date() {
        return s_date;
    }
    public void setS_date(java.sql.Date s_date) {
        this.s_date = s_date;
    }
    public java.sql.Date getE_date() {
        return e_date;
    }
    public void setE_date(java.sql.Date e_date) {
        this.e_date = e_date;
    }
    public boolean getIs_active() {
        return is_active;
    }
    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
}
