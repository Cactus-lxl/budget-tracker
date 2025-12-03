//containers to convert between data and java object
package com.budgettracker.models;

import java.math.BigDecimal;

public class Users {
    private int user_id;
    private String uname;
    private String fName;
    private String lName;
    private String email;
    private String pw;
    private BigDecimal balance;

    //constructor
    public Users() {
        this.balance = BigDecimal.ZERO;
    }
    
    public Users(int user_id, String uname, String fName, String lName, String email, String pw, BigDecimal balance) {
        this.user_id = user_id;
        this.uname = uname;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.pw = pw;
        this.balance = balance;
    }

    //getters and setters
    public int getUserId() {
        return user_id;
    }
    public void setUserId(int userId) {
        this.user_id = userId;
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
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
