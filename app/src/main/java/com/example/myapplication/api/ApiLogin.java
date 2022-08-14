package com.example.myapplication.api;

import com.example.myapplication.model.Driver;
import com.example.myapplication.model.LocationDriver;
import com.example.myapplication.model.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiLogin {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy")
            .create();

    ApiLogin apiService = new Retrofit.Builder()
            .baseUrl("34.72.120.102:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiLogin.class);

    @POST("register")
    Call<Result> registerDriver(@Body Driver driver);

    @POST("login")
    Call<Driver> loginDriver(@Query("userId") String userId, @Query("password") String password);

    @POST("refreshToken")
    Call<Driver> getNewToken(@Query("userId") String userId, @Query("refreshToken") String refreshToken);


}
