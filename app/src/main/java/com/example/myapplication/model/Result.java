package com.example.myapplication.model;

public class Result {
    private String token;
    private String refreshToken;
    private String loginError;

    public Result(String token, String refreshToken, String loginError) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.loginError = loginError;
    }

    public Result(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getLoginError() {
        return loginError;
    }

    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }
}
