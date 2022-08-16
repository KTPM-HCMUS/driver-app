package com.example.myapplication.model;

public class LocationDriver {
    private int driverID;
    private double latitude;
    private double longitude;

    public LocationDriver(int driverID, double latitude, double longitude) {
        this.driverID = driverID;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public LocationDriver(){}
//
//    public String getDriverID() {
//        return driverID;
//    }
//
//    public void setDriverID(String driverID) {
//        this.driverID = driverID;
//    }

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
