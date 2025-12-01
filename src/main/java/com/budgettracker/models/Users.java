//containers to convert between data and java object
package com.budgettracker.models;

import java.sql.Timestamp;

public class Users {
    private int userId;
    private String uname;
    private String fName;
    private String lName;
    private String email;
    private String pw;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    //constructor
    public Users() {}
    public Users(int userId, String uname, String fName, String lName, String email, String pw, Timestamp createAt, Timestamp updatedAt) {
        this.userId = userId;
        this.uname = uname;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.pw = pw;
        this.createdAt = createAt;
        this.updatedAt = updatedAt;
    }

    //getters and setters
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUname() {
        return uname;
    }
    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getFName() {
        return fName;
    }
    public void setFName(String fName) {
        this.fName = fName;
    }
    public String getlName() {
        return lName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
}
