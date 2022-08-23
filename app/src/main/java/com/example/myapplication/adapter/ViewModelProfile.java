package com.example.myapplication.adapter;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.api.ApiBooking;
import com.example.myapplication.model.BookingItem;
import com.example.myapplication.model.BookingList;
import com.example.myapplication.model.Total;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelProfile extends androidx.lifecycle.ViewModel {
    private MutableLiveData<List<Double>> mListLiveData;
    private List<Double> totalList;

    public ViewModelProfile(){
        mListLiveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        totalList = new ArrayList<>();
        ApiBooking.apiBooking.getSummary(MainActivity.locationDriver.getToken()).enqueue(new Callback<List<Double>>() {
            @Override
            public void onResponse(Call<List<Double>> call, Response<List<Double>> response) {
                    totalList.add(response.body().get(0));
                    totalList.add(response.body().get(1));
                    mListLiveData.setValue(totalList);
            }

            @Override
            public void onFailure(Call<List<Double>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    public MutableLiveData<List<Double>> getTotalLiveDate() {
        return mListLiveData;
    }
}
