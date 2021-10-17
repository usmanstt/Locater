package com.example.locater;

public class UserInfo {
    String name, emailID, phoneNumber, userid;

//    public UserInfo(String name, String emailID, String phoneNumber, String userid) {
//        this.name = name;
//        this.emailID = emailID;
//        this.phoneNumber = phoneNumber;
//        this.userid = userid;
//    }

    public UserInfo() {
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
