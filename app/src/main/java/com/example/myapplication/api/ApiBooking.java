package com.example.myapplication.api;

import com.example.myapplication.model.BookingItem;
import com.example.myapplication.model.BookingList;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.DriverTemp;
import com.example.myapplication.model.LoginModel;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.ResponseTT;
import com.example.myapplication.model.Total;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiBooking {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy")
            .create();

    ApiBooking apiBooking = new Retrofit.Builder()
            .baseUrl("https://api-location-v1.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiBooking.class);

    @POST("api/v1/location/booking")
    Call<Message> postBooking(@Body BookingItem bookingItem);

    @GET("api/v1/location/history/all/{token}")
    Call<BookingList> getAllBookingHistory(@Path("token") String token);

    @GET("api/v1/location/total/{token}")
    Call<List<Double>> getSummary(@Path("token") String token);
}
