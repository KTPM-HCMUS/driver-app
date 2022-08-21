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
    //    public static void testStreaming(LatLng location, String driverId, String customerId, int typeOfVehicle, ExecutorService executorService) {
    static LocationStreamingClient client = new LocationStreamingClient();

    //            ExecutorService sendDriverLocationExecutor = Executors.newSingleThreadExecutor();
//            ExecutorService getLocationExecutor = Executors.newSingleThreadExecutor();
//            ExecutorService sendCustomerLocationExecutor = Executors.newSingleThreadExecutor();
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
        Log.d("TAG123", "A,B " );
        client.getLocation(customerId, driverId, response -> {
            Log.d("TAG123", response.getDriverLocation().getLatitude() + ", " + response.getDriverLocation().getLongitude());
            double distance = CalculatingDistance.distance(response.getDriverLocation().getLatitude(), response.getDriverLocation().getLongitude(),
                    response.getCustomerLocation().getLatitude(), response.getCustomerLocation().getLongitude(), 'M');
            if(distance < 0.05D) {
//                FragmentHome.mMap.clear();
                executorService1.shutdown();
                executorService2.shutdown();
                LatLng latLng = new LatLng(response.getCustomerLocation().getLatitude(), response.getCustomerLocation().getLongitude());
                FragmentHome.updateLocationMarker(2, latLng, typeOfVehicle);
                FragmentHome.updateLocationMarker(1, destination, typeOfVehicle);
            }
//                GMapUtils.direction(latLng, destination, FragmentHome.mMap, activity);
//            }

//            FragmentHome.updateLocationMarker(2, new LatLng(response.getDriverLocation().getLatitude(), response.getDriverLocation().getLongitude()), typeOfVehicle);
//            FragmentHome.updateLocationMarker(1, new LatLng(response.getCustomerLocation().getLatitude(), response.getCustomerLocation().getLongitude()), 0);
        }, executorService);
    }
}


//            client.sendLocation("driver_test", () -> {
//                Random rd = new Random();
//                return LocationOuterClass.Location.newBuilder()
//                        .setLatitude(rd.nextDouble())
//                        .setLongitude(rd.nextDouble())
//                        .build();
//            }, sendDriverLocationExecutor);

//            CountDownLatch countDownLatch = new CountDownLatch(1);
//            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//                sendCustomerLocationExecutor.shutdown();
//                sendDriverLocationExecutor.shutdown();
//                getLocationExecutor.shutdown();
//                countDownLatch.countDown();
//            }));
//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

