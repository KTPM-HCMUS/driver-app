package com.example.myapplication.utils;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.client.LocationStreamingClient;
import com.example.myapplication.fragment.FragmentHome;
import com.google.android.gms.maps.model.LatLng;
import com.ktpm.vehiclebooking.LocationOuterClass;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class StreamingLocationUtils {
    static LocationStreamingClient client = new LocationStreamingClient();
    public static void sendLocation(ExecutorService executorService, String driverId) {
        client.sendLocation(driverId, () -> {
            LatLng location = MainActivity.getDriverForStream();
            Log.d("TAGCLIENT", location.latitude + "," + location.longitude);
            return LocationOuterClass.Location.newBuilder()
                    .setLatitude(location.latitude)
                    .setLongitude(location.longitude)
                    .build();
        }, executorService);
    }

    public static void sendLocationClient(ExecutorService executorService, String clientId, LatLng location) {
        client.sendLocation(clientId, () -> {
            Log.d("TAGaaaa", location.latitude + "," + location.longitude);
            return LocationOuterClass.Location.newBuilder()
                    .setLatitude(location.latitude)
                    .setLongitude(location.longitude)
                    .build();
        }, executorService);
    }

//    public static void getLocationID(String customerId, String driverId, int typeOfVehicle, ExecutorService executorService, LatLng destination, Activity activity, ExecutorService executorService1, ExecutorService executorService2) {
//        client.getLocation(customerId, driverId, response -> {
//
//            }, executorService);
//    }
}
