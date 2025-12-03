package com.budgettracker.models;

import java.math.BigDecimal;
import java.sql.*;

public class Goal {
    private int gid;
    private BigDecimal target;
    private BigDecimal current;
    private Date create_date;
    private Date end_date;
    private int uid;

    public Goal(){}

    public Goal(int gid, BigDecimal target, BigDecimal current, Date create_date, Date end_date, int uid) {
        this.gid = gid;
        this.target = target;
        this.current = current;
        this.create_date = create_date;
        this.end_date = end_date;
        this.uid = uid;
    }

    public int getGid() {
        return gid;
    }
    public void setGid(int gid) {
        this.gid = gid;
    }
    public BigDecimal getTarget() {
        return target;
    }
    public void setTarget(BigDecimal target) {
        this.target = target;
    }
    public BigDecimal getCurrent() {
        return current;
    }
    public void setCurrent(BigDecimal current) {
        this.current = current;
    }
    public java.sql.Date getCreate_date() {
        return create_date;
    }
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
    public java.sql.Date getEnd_date() {
        return end_date;
    }
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
}
