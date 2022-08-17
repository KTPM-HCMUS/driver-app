package com.example.myapplication.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.util.Log;

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
                Double longitude = jobParameters.getExtras().getDouble("lon");
                Double latitude = jobParameters.getExtras().getDouble("lat");
//                String userId = jobParameters.getExtras().getString("userId");
//                int typeOfVehicle = jobParameters.getExtras().getInt("type");
                LocationDriver locationDriver = new LocationDriver("1",1, latitude, longitude);
                ApiService.apiService.updateLocationDriver(locationDriver).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        System.out.println("zh");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("zh");
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

}
