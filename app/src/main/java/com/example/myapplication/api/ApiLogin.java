package com.example.myapplication.api;

import com.example.myapplication.model.Driver;
import com.example.myapplication.model.DriverTemp;
import com.example.myapplication.model.LoginModel;
import com.example.myapplication.model.ResponseTT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiLogin {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy")
            .create();

    ApiLogin apiService = new Retrofit.Builder()
            .baseUrl("http://34.121.234.226:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiLogin.class);

    @POST("register")
    Call<ResponseTT> registerDriver(@Body Driver driver);

    @POST("login")
    Call<ResponseTT> loginDriver(@Body LoginModel loginModel);

    @POST("refreshToken")
    Call<ResponseTT> getNewToken(@Body DriverTemp driverTemp);

    @POST("v1/revokeToken")
    Call<ResponseTT> revokeToken(@Header("Authorization") String Authorization);

}
