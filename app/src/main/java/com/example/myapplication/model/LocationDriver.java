package com.example.myapplication.model;

public class LocationDriver {
    private String driverID;
    private double latitude;
    private double longitude;
    private int typeOfVehicle;

    public LocationDriver(String driverID, int typeOfVehicle, double latitude, double longitude) {
        this.driverID = driverID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typeOfVehicle = typeOfVehicle;
    }
    public LocationDriver(){}

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
