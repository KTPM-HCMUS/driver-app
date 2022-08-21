package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.LocationDriver;
import com.example.myapplication.model.LocationResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class FragmentTracking extends Fragment {
    private View mView;
    private TextView price, method, nameCustomer, phoneCustomer, departAddress, destinationAddress, status;
    private Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tracking, container, false);
        init(mView);
        setTextForAllField();
        return mView;
    }

    private void init(View mView){
        price = mView.findViewById(R.id.price_confirm);
        nameCustomer = mView.findViewById(R.id.name_confirm);
        phoneCustomer = mView.findViewById(R.id.phone_confirm);
        departAddress = mView.findViewById(R.id.depart_confirm);
        destinationAddress = mView.findViewById(R.id.destination_confirm);
        status = mView.findViewById(R.id.status_confirm);
        btn = mView.findViewById(R.id.btn_confirm_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void setTextForAllField() {
        String a = "";
        LocationDriver driver = getDriver();
        LocationResponse client = getClient();
        price.setText(client.getPrice().toString());
        nameCustomer.setText(client.getName());
        phoneCustomer.setText(client.getPhoneNumber());
        departAddress.setText(client.getLocationClient().getAddressDepart());
        destinationAddress.setText(client.getLocationClient().getAddressDestination());
        switch (driver.getStatus()){
            case "MATCHED":
                a = "Đang đón khách";
                break;
            case "WORKING":
                a = "Đang trả khách";
                break;
            case "DONE":
                a = "Hoàn tất";
                break;
        }
        status.setText(a);
    }


    private LocationDriver getDriver(){
        LocationDriver driver = null;
        driver = (LocationDriver) getArguments().get("object_driver_information");
        return driver;
    }

    private LocationResponse getClient(){
        LocationResponse client = null;
        client = (LocationResponse) getArguments().get("object_client_information");
        return client;
    }
}