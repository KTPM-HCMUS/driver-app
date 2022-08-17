package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Driver;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentProfile extends Fragment {
    private View mView;
    private TextView fullName;
    private TextInputEditText fullNameEdt, phoneNumberEdt, dobEdt, emailEdt, typeEdt, plateEdt;

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

        setTextForAllField();
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