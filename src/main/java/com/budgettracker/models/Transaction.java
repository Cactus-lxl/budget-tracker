package com.budgettracker.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int tid;
    private String description;
    private BigDecimal amount;
    private Timestamp time;
    private String type;
    private int uid;
    private int cid;

    public Transaction(){}

    public Transaction(int tid, String description, BigDecimal amount, Timestamp time, String type, int uid, int cid) {
        this.tid = tid;
        this.description = description;
        this.amount = amount;
        this.time = time;
        this.type = type;
        this.uid = uid;
        this.cid = cid;
    }

    public int getTid() {
        return tid;
    }
    public void setTid(int tid) {
        this.tid = tid;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
