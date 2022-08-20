package com.example.myapplication.model;

import java.io.Serializable;

public class LocationDriver implements Serializable {
    private String driverID;
    private double latitude;
    private double longitude;
    private int typeOfVehicle;
    private String token;
    //NOT MATCH - MATCHED - WORKING - DONE
    private String status;

    public LocationDriver(String driverID, int typeOfVehicle, double latitude, double longitude, String token) {
        this.driverID = driverID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typeOfVehicle = typeOfVehicle;
        this.token = token;
        this.status = "NOT_MATCH";
    }
    public LocationDriver(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDriver(){}

    public int getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTypeOfVehicle(int typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
