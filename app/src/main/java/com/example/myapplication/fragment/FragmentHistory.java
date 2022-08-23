package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapter.HistoryAdapter;
import com.example.myapplication.adapter.ViewModelHistory;
import com.example.myapplication.model.BookingItem;
import com.example.myapplication.model.LocationDriver;

import java.util.List;

public class FragmentHistory extends Fragment {
    public RecyclerView recyclerView;
    private View mView;
    private MainActivity mainActivity;
    private ViewModelHistory viewModel;

    public FragmentHistory(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView =  inflater.inflate(R.layout.fragment_history, container, false);
        mainActivity =(MainActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        viewModel = new ViewModelProvider(this).get(ViewModelHistory.class);
        viewModel.getListLiveData().observe(getViewLifecycleOwner(), new Observer<List<BookingItem>>() {
            @Override
            public void onChanged(List<BookingItem> bookingItems) {
                HistoryAdapter historyAdapter
                        = new HistoryAdapter(bookingItems, new HistoryAdapter.IClickItemListener() {
                    @Override
                    public void onClickItem(BookingItem bookingItem) {
                        mainActivity.goToDetailFragment(bookingItem);
                    }
                });
                recyclerView.setAdapter(historyAdapter);
            }
        });

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        return mView;
    }

//    private void getListItem(String token) {
//        ApiBooking.apiBooking.getAllBookingHistory(token).enqueue( new Callback<BookingList>() {
//            @Override
//            public void onResponse(Call<BookingList> call, Response<BookingList> response) {
//                BookingList s = new BookingList(response.body().getBookingItems());
//                for (int i = 0; i < s.getBookingItems().size(); i++) {
//                    list.add(new BookingItem(s.getBookingItems().get(i).getPrice(),
//                            s.getBookingItems().get(i).getName(),
//                            s.getBookingItems().get(i).getPhoneNumber(),
//                            s.getBookingItems().get(i).getAddressDepart(),
//                            s.getBookingItems().get(i).getAddressDestination(),
//                            s.getBookingItems().get(i).getTimeStamp()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BookingList> call, Throwable t) {
//                System.out.println(t.toString());
//            }
//        });
//    }

    private LocationDriver getDriver(){
        LocationDriver driver = null;
        driver = (LocationDriver) getArguments().get("object_driver_information");
        return driver;
    }

}