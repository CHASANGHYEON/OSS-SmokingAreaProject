package com.example.smokingareaproject;

public class infoItem {
    private int i=0;
    private String no;
    private String userID;
    private String userPassword;
    private String address;


    public int geti() {
        return i;
    }

    public String getno() {
        return no;
    }

    public String getuserID() {
        return userID;
    }

    public String getuserPassword() {
        return userPassword;
    }

    public String getaddress() {
        return address;
    }

    public void seti(int i) {
        this.i = i;
    }

    public void setno(String no) {
        this.no = no;
    }

    public void setuserID(String userID) {
        this.userID = userID;
    }

    public void setuserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setaddress(String address) {
        this.address = address;
    }

}