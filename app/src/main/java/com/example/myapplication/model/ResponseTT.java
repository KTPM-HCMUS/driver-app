package com.example.myapplication.model;


public class ResponseTT {
    public Result result;

    public ResponseTT(){

    }
    public ResponseTT(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}

