package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapter.HistoryAdapter;
import com.example.myapplication.model.ItemHistory;

import java.util.ArrayList;
import java.util.List;

public class FragmentHistory extends Fragment {
    public RecyclerView recyclerView;
    private View mView;
    private MainActivity mainActivity;
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

        HistoryAdapter historyAdapter
                 = new HistoryAdapter(getListItem(), new HistoryAdapter.IClickItemListener() {
            @Override
            public void onClickItem(ItemHistory itemHistory) {
                mainActivity.goToDetailFragment(itemHistory);
            }
        });
        recyclerView.setAdapter(historyAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        return mView;
    }

    private List<ItemHistory> getListItem() {
        List<ItemHistory> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(new ItemHistory("12" + i + "Nguyễn Trãi", "Tổng tiền: 10" + i, "12/2/2001 - 12:15"));
        }
        return list;
    }
}