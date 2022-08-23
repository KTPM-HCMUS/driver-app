package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.HistoryAdapter;
import com.example.myapplication.adapter.ViewModelHistory;
import com.example.myapplication.adapter.ViewModelProfile;
import com.example.myapplication.model.BookingItem;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.Total;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FragmentProfile extends Fragment {
    private View mView;
    private TextView fullName,  payment, booking;
    private TextInputEditText fullNameEdt, phoneNumberEdt, dobEdt, emailEdt, typeEdt, plateEdt;
    private ViewModelProfile viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        fullName = mView.findViewById(R.id.fullname_field);
        fullNameEdt = (TextInputEditText) mView.findViewById(R.id.full_name_profile);
        phoneNumberEdt = (TextInputEditText) mView.findViewById(R.id.phone_number);
        dobEdt = (TextInputEditText) mView.findViewById(R.id.date_of_birth);
        emailEdt = (TextInputEditText) mView.findViewById(R.id.email_id);
        typeEdt = (TextInputEditText) mView.findViewById(R.id.type_of_vehicle);
        plateEdt = (TextInputEditText) mView.findViewById(R.id.vehicle_plate);
        payment = mView.findViewById(R.id.payment_label);
        booking = mView.findViewById(R.id.booking_label);

        setTextForAllField();

        viewModel = new ViewModelProvider(this).get(ViewModelProfile.class);
        viewModel.getTotalLiveDate().observe(getViewLifecycleOwner(), new Observer<List<Double>>() {
            @Override
            public void onChanged(List<Double> totals) {
                payment.setText(totals.get(0).toString() + "VND");
                booking.setText(String.valueOf(totals.get(1).intValue()));
            }
        });
        return mView;
    }

    private void setTextForAllField() {
        Driver driver = getDriver();
        fullName.setText(driver.getName());
        fullNameEdt.setText(driver.getName());
        phoneNumberEdt.setText(driver.getUserId());
        dobEdt.setText(driver.getDob());
        emailEdt.setText(driver.getEmail());
        if(driver.getType()==1){
            typeEdt.setText("MOTORBIKE");
        }else{
            typeEdt.setText("CAR");
        }
        plateEdt.setText(driver.getVehicle_plate());
    }


    private Driver getDriver(){
        Driver driver = null;
        driver = (Driver) getArguments().get("object_driver");
        return driver;
    }
}