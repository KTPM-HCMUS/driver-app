package com.example.myapplication.model;

import java.io.Serializable;

public class LocationResponse implements Serializable {
    private String phoneNumber;
    private String name;
    private LocationClient locationClient;
    private Double price;
    private String id;

    public LocationResponse(String phoneNumber, String name, LocationClient locationClient, Double price, String id) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.locationClient = locationClient;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationClient getLocationClient() {
        return locationClient;
    }

    public void setLocationClient(LocationClient locationClient) {
        this.locationClient = locationClient;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
