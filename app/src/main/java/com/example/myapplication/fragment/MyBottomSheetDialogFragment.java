package com.example.myapplication.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.model.LocationDriver;
import com.example.myapplication.model.LocationResponse;
import com.example.myapplication.utils.GMapUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private LocationResponse locationResponse;
    private LocationDriver locationDriver;
    private TextView price, name, phoneNumber, addressDestination, addressDepart;
    private Button btn_confirm;
    public static MyBottomSheetDialogFragment newInstance(LocationResponse locationResponse, LocationDriver locationDriver){
        MyBottomSheetDialogFragment myBottomSheetDialogFragment = new MyBottomSheetDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_client", locationResponse);
        bundle.putSerializable("object_driver", locationDriver);
        myBottomSheetDialogFragment.setArguments(bundle);


        return myBottomSheetDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleReceive = getArguments();
        if(bundleReceive != null){
            locationResponse = (LocationResponse) bundleReceive.get("object_client");
            locationDriver = (LocationDriver) bundleReceive.get("object_driver");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);
        initView(view);
        setData();
        btn_confirm = view.findViewById(R.id.btn_confirm_bottom);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                GMapUtils.direction(new LatLng(locationResponse.getLocationClient().getLatitudeDepart(),
                                                locationResponse.getLocationClient().getLongitudeDepart()),
                        new LatLng(locationDriver.getLatitude(), locationDriver.getLongitude()), FragmentHome.mMap, getActivity());
            }
        });

//        BottomSheetBehavior behavior = BottomSheetBehavior.from((View)view.getParent());
//        behavior.setPeekHeight(150);

        return  bottomSheetDialog;
    }

    private void initView(View view){
        price = view.findViewById(R.id.price_bottom);
        name = view.findViewById(R.id.customer_bottom);
        phoneNumber = view.findViewById(R.id.phone_bottom);
        addressDepart = view.findViewById(R.id.depart_bottom);
        addressDestination = view.findViewById(R.id.destination_bottom);
        price = view.findViewById(R.id.price_bottom);
    }

    private void setData(){
        if(locationResponse == null){
            return;
        }
        price.setText(locationResponse.getPrice() + "VND");
        name.setText(locationResponse.getName());
        phoneNumber.setText(locationResponse.getPhoneNumber());
        addressDepart.setText(locationResponse.getLocationClient().getAddressDepart());
        addressDestination.setText(locationResponse.getLocationClient().getAddressDestination());
    }
}
