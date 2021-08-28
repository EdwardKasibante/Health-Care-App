package com.example.health_care_app;

public class UserList {
    private String fname;
    private String lname;
    private String gender;
    private String phone;
    private String time;
    private String image;
    private String date;
    private String address;
    private String dob;
    private String firebaseKeys;
    private String added_by;

    public UserList() {
    }

    public UserList(String fname, String lname, String gender, String phone, String time, String image, String date, String address, String dob, String firebaseKeys, String added_by) {
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.phone = phone;
        this.time = time;
        this.image = image;
        this.date = date;
        this.address = address;
        this.dob = dob;
        this.firebaseKeys = firebaseKeys;
        this.added_by = added_by;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFirebaseKeys() {
        return firebaseKeys;
    }

    public void setFirebaseKeys(String firebaseKeys) {
        this.firebaseKeys = firebaseKeys;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }
}
