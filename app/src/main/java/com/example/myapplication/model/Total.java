package com.example.myapplication.model;

public class Total {
    private Double price;
    private Double total;

    public Total(Double price, Double total) {
        this.price = price;
        this.total = total;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
