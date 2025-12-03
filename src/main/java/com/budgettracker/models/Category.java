package com.budgettracker.models;

public class Category {
    private int cid;
    private String cname;
    private String type;

    public Category() {}

    public Category(int cid, String cname, String type) {
        this.cid = cid;
        this.cname = cname;
        this.type = type;
    }

    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
