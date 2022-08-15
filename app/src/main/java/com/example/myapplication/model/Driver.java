package com.example.myapplication.model;

import android.text.Editable;

import java.io.Serializable;

public class Driver implements Serializable {
    private String userId;
    private String password;
    private String name;
    private String dob;
    private int role;
    private String email;
    private int type;
    private String vehicle_plate;

    public Driver(String userId, String password, String name, String dob, int role, String email, int type, String vehicle_plate) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.role = role;
        this.email = email;
        this.type = type;
        this.vehicle_plate = vehicle_plate;
    }

    public Driver(String userId, String name, String dob, String email, int type, String vehicle_plate) {
        this.userId = userId;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.type = type;
        this.vehicle_plate = vehicle_plate;
    }

    public Driver(String userId, String password){
        this.userId = userId;
        this.password = password;
    }

    public Driver(){

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVehicle_plate() {
        return vehicle_plate;
    }

    public void setVehicle_plate(String vehicle_plate) {
        this.vehicle_plate = vehicle_plate;
    }
}
