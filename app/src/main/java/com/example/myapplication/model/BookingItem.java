package com.example.myapplication.model;

import java.io.Serializable;

public class BookingItem implements Serializable {
    private String price;
    private String token;
    private String name;
    private String phoneNumber;
    private String addressDepart;
    private String addressDestination;
    private long timeStamp;
    private String userId;

    public BookingItem(String price, String name, String phoneNumber, String addressDepart, String addressDestination, long timeStamp) {
        this.price = price;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.addressDepart = addressDepart;
        this.addressDestination = addressDestination;
        this.timeStamp = timeStamp;
        this.userId = userId;
    }

    public BookingItem(String price, String token, String name, String phoneNumber, String addressDepart, String addressDestination, long timeStamp, String userID) {
        this.price = price;
        this.token = token;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.addressDepart = addressDepart;
        this.addressDestination = addressDestination;
        this.timeStamp = timeStamp;
        this.userId = userID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressDepart() {
        return addressDepart;
    }

    public void setAddressDepart(String addressDepart) {
        this.addressDepart = addressDepart;
    }

    public String getAddressDestination() {
        return addressDestination;
    }

    public void setAddressDestination(String addressDestination) {
        this.addressDestination = addressDestination;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
