package com.example.myapplication.model;

import java.io.Serializable;

public class ItemHistory  implements Serializable {
    private String address;
    private String price;
    private String time;

    public ItemHistory(String address, String price, String time) {
        this.address = address;
        this.price = price;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
