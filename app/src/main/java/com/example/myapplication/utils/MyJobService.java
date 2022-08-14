package com.example.myapplication.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.LocationDriver;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobService extends JobService {
    public static final String TAG = MyJobService.class.getName();
    private boolean jobCancelled;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        doBackgroundWork(jobParameters);
        return true;
    }

    private void doBackgroundWork(JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Double longtitude = jobParameters.getExtras().getDouble("lon");
                Double latitude = jobParameters.getExtras().getDouble("lat");
                LocationDriver locationDriver = new LocationDriver(1, longtitude, latitude);
                ApiService.apiService.updateLocationDriver(locationDriver).enqueue(new Callback<LocationDriver>() {
                    @Override
                    public void onResponse(Call<LocationDriver> call, Response<LocationDriver> response) {
                        System.out.println("LOG TRUE" + response.body());
                    }

                    @Override
                    public void onFailure(Call<LocationDriver> call, Throwable t) {
                        System.out.println("LOG FAIL");
                    }
                });
                jobFinished(jobParameters, false);
            }
        }).start();
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobCancelled = true;
        return true;
    }

    private LocationDriver getLocationData(JobParameters parameters){
        PersistableBundle bundle = new PersistableBundle();
        String a = bundle.getString("data");
        String json = parameters.getExtras().getString("data");
        Gson g = new Gson();
        return g.fromJson(json, LocationDriver.class);
    }
}
