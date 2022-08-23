package com.example.myapplication.adapter;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.api.ApiBooking;
import com.example.myapplication.fragment.FragmentHome;
import com.example.myapplication.model.BookingItem;
import com.example.myapplication.model.BookingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelHistory extends androidx.lifecycle.ViewModel {
    private MutableLiveData<List<BookingItem>> mListLiveData;
    private List<BookingItem> bookingItemList;

    public ViewModelHistory(){
        mListLiveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        bookingItemList = new ArrayList<>();
        ApiBooking.apiBooking.getAllBookingHistory(MainActivity.locationDriver.getToken()).enqueue(new Callback<BookingList>() {
            @Override
            public void onResponse(Call<BookingList> call, Response<BookingList> response) {
                BookingList s = new BookingList();
                s.setBookingItems(response.body().getBookingItems());
                for (int i = 0; i < s.getBookingItems().size(); i++) {
                    BookingItem bookingItem = new BookingItem(s.getBookingItems().get(i).getPrice(),
                            s.getBookingItems().get(i).getName(),
                            s.getBookingItems().get(i).getPhoneNumber(),
                            s.getBookingItems().get(i).getAddressDepart(),
                            s.getBookingItems().get(i).getAddressDestination(),
                            s.getBookingItems().get(i).getTimeStamp());
                    bookingItemList.add(bookingItem);
                }
                mListLiveData.setValue(bookingItemList);
            }

            @Override
            public void onFailure(Call<BookingList> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    public MutableLiveData<List<BookingItem>> getListLiveData() {
        return mListLiveData;
    }
}
