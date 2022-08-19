package com.example.myapplication.utils;

import com.example.myapplication.client.LocationStreamingClient;
import com.google.android.gms.maps.model.LatLng;
import com.ktpm.vehiclebooking.LocationOuterClass;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamingLocationUtils {
    public static void testStreaming(LatLng location, String userId) {
        Thread thread = new Thread(() -> {
            LocationStreamingClient client = new LocationStreamingClient();
            ExecutorService sendDriverLocationExecutor = Executors.newSingleThreadExecutor();
            ExecutorService getLocationExecutor = Executors.newSingleThreadExecutor();
            ExecutorService sendCustomerLocationExecutor = Executors.newSingleThreadExecutor();

            client.sendLocation(userId, () -> {
//                Random rd = new Random();
                return LocationOuterClass.Location.newBuilder()
                        .setLatitude(location.latitude)
                        .setLongitude(location.longitude)
                        .build();
            }, sendCustomerLocationExecutor);
//            client.sendLocation("driver_test", () -> {
//                Random rd = new Random();
//                return LocationOuterClass.Location.newBuilder()
//                        .setLatitude(rd.nextDouble())
//                        .setLongitude(rd.nextDouble())
//                        .build();
//            }, sendDriverLocationExecutor);
//            client.getLocation("customer_test", "driver_test", response -> {
//                System.out.println("======================================");
//                System.out.println(Thread.currentThread().getName());
//                System.out.println("customer_test latitude=" + response.getCustomerLocation().getLatitude() + ", longitude=" + response.getCustomerLocation().getLongitude());
//                System.out.println("driver_test location=" + response.getDriverLocation().getLatitude() + ", longitude=" + response.getDriverLocation().getLongitude());
//                System.out.println("======================================");
//            }, getLocationExecutor);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                sendCustomerLocationExecutor.shutdown();
                sendDriverLocationExecutor.shutdown();
                getLocationExecutor.shutdown();
                countDownLatch.countDown();
            }));
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            thread.stop();
        }));
    }

}
