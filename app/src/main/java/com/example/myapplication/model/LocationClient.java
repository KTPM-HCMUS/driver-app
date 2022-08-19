package com.example.myapplication.model;

import java.io.Serializable;

public class LocationClient implements Serializable {
    private Double longitudeDepart;
    private Double latitudeDepart;
    private Double longitudeDestination;
    private Double latitudeDestination;
    private String addressDepart;
    private String addressDestination;

    public LocationClient(Double longitudeDepart, Double latitudeDepart, Double longitudeDestination, Double latitudeDestination, String addressDepart, String addressDestination) {
        this.longitudeDepart = longitudeDepart;
        this.latitudeDepart = latitudeDepart;
        this.longitudeDestination = longitudeDestination;
        this.latitudeDestination = latitudeDestination;
        this.addressDepart = addressDepart;
        this.addressDestination = addressDestination;
    }

    public LocationClient(){

    }

    public Double getLongitudeDepart() {
        return longitudeDepart;
    }

    public void setLongitudeDepart(Double longitudeDepart) {
        this.longitudeDepart = longitudeDepart;
    }

    public Double getLatitudeDepart() {
        return latitudeDepart;
    }

    public void setLatitudeDepart(Double latitudeDepart) {
        this.latitudeDepart = latitudeDepart;
    }

    public Double getLongitudeDestination() {
        return longitudeDestination;
    }

    public void setLongitudeDestination(Double longitudeDestination) {
        this.longitudeDestination = longitudeDestination;
    }

    public Double getLatitudeDestination() {
        return latitudeDestination;
    }

    public void setLatitudeDestination(Double latitudeDestination) {
        this.latitudeDestination = latitudeDestination;
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
}