package com.budgettracker.models;

import java.math.BigDecimal;

public class Budget {
    private int bid;
    private int cid;
    private int uid;
    private BigDecimal limit_amount;
    private int month;
    private int year;

    public Budget(){}

    public Budget(int bid, int cid, int uid, BigDecimal limit_amount, int month, int year) {
        this.bid = bid;
        this.cid = cid;
        this.uid = uid;
        this.limit_amount = limit_amount;
        this.month = month;
        this.year = year;
    }

    public int getBid() {
        return bid;
    }
    public void setBid(int bid) {
        this.bid = bid;
    }
    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public BigDecimal getLimit_amount() {
        return limit_amount;
    }
    public void setLimit_amount(BigDecimal limit_amount) {
        this.limit_amount = limit_amount;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
