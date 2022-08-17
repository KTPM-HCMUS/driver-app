package com.example.myapplication.client;

import android.util.Log;

import com.ktpm.vehiclebooking.LocationOuterClass;
import com.ktpm.vehiclebooking.LocationStreamingGrpc;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class LocationStreamingClient {
    private static final String TAG = "LocationStreamingClient";
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("192.168.2.210", 8089)
            .usePlaintext()
            .enableRetry()
            .enableFullStreamDecompression()
            .executor(Executors.newSingleThreadExecutor())
            .build();

    private final LocationStreamingGrpc.LocationStreamingStub stub = LocationStreamingGrpc.newStub(channel);

    public void sendLocation(String userId, Callable<LocationOuterClass.Location> callback, Executor executor) {
        executor.execute(() -> {
            AtomicReference<StreamObserver<LocationOuterClass.SendLocationRequest>> responseObserver = new AtomicReference<>();
            StreamObserver<LocationOuterClass.SendLocationRequest> observer = stub.sendLocation(
                    new StreamObserver<LocationOuterClass.SendLocationResponse>() {
                        @Override
                        public void onNext(LocationOuterClass.SendLocationResponse response) {
                            try {
                                responseObserver.get().onNext(
                                        LocationOuterClass.SendLocationRequest.newBuilder()
                                                .setUserId(userId)
                                                .setLocation(callback.call())
                                                .build()
                                );
                            } catch (Exception e) {
                                Log.e(TAG, "onNext: ", e);
                                responseObserver.get().onNext(
                                        LocationOuterClass.SendLocationRequest.newBuilder()
                                                .setUserId(userId)
                                                .setLocation(LocationOuterClass.Location.getDefaultInstance())
                                                .build()
                                );
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            t.printStackTrace();
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("onCompleted");
                        }
                    });
            responseObserver.set(observer);
            try {
                observer.onNext(
                        LocationOuterClass.SendLocationRequest.newBuilder()
                                .setUserId(userId)
                                .setLocation(callback.call())
                                .build());
            } catch (Exception e) {
                observer.onNext(
                        LocationOuterClass.SendLocationRequest.newBuilder()
                                .setUserId(userId)
                                .setLocation(LocationOuterClass.Location.getDefaultInstance())
                                .build()
                );
            }
        });
    }

    public void getLocation(String customerId, String driverId, Consumer<LocationOuterClass.GetLocationResponse> func, Executor executor) {
        executor.execute(() -> {
            AtomicReference<StreamObserver<LocationOuterClass.GetLocationRequest>> responseObserver = new AtomicReference<>();
            StreamObserver<LocationOuterClass.GetLocationRequest> observer = stub.getLocation(
                    new StreamObserver<LocationOuterClass.GetLocationResponse>() {
                        @Override
                        public void onNext(LocationOuterClass.GetLocationResponse response) {
                            func.accept(response);
                            responseObserver.get().onNext(LocationOuterClass.GetLocationRequest.newBuilder()
                                    .setCustomerId(customerId)
                                    .setDriverId(driverId)
                                    .build());
                        }

                        @Override
                        public void onError(Throwable t) {
                            t.printStackTrace();
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("onCompleted");
                        }
                    });
            responseObserver.set(observer);
            observer.onNext(
                    LocationOuterClass.GetLocationRequest.newBuilder()
                            .setCustomerId(customerId)
                            .setDriverId(driverId)
                            .build());
        });
    }
}
