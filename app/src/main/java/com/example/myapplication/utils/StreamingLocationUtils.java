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
            Log.d("TAGCLIENT", location.latitude + "," + location.longitude);
            return LocationOuterClass.Location.newBuilder()
                    .setLatitude(location.latitude)
                    .setLongitude(location.longitude)
                    .build();
        }, executorService);
    }

    public static void getLocationID(String customerId, String driverId, int typeOfVehicle, ExecutorService executorService, LatLng destination, Activity activity, ExecutorService executorService1, ExecutorService executorService2) {
        client.getLocation(customerId, driverId, response -> {
            Log.d("TAG123", response.getDriverLocation().getLatitude() + ", " + response.getDriverLocation().getLongitude());
            double distance = CalculatingDistance.distance(response.getDriverLocation().getLatitude(), response.getDriverLocation().getLongitude(),
                    response.getCustomerLocation().getLatitude(), response.getCustomerLocation().getLongitude(), 'M');
            if(distance < 0.05D) {
                if(GMapUtils.polylineFinal!=null){
                    GMapUtils.polylineFinal.remove();
                }
                executorService1.shutdown();
                executorService2.shutdown();
                GMapUtils.direction(new LatLng(response.getDriverLocation().getLatitude(), response.getDriverLocation().getLongitude()), destination, FragmentHome.mMap, activity);
                LatLng latLng = new LatLng(response.getCustomerLocation().getLatitude(), response.getCustomerLocation().getLongitude());
                FragmentHome.updateLocationMarker(2, latLng, typeOfVehicle);
                FragmentHome.updateLocationMarker(1, destination, typeOfVehicle);
            }
            }, executorService);
    }
}
