package com.example.myapplication.api;

import android.location.LocationListener;

import com.example.myapplication.model.LocationDriver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://driver-location.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("api/v1/location")
    Call<LocationDriver> updateLocationDriver(@Body LocationDriver locationDriver);
}
