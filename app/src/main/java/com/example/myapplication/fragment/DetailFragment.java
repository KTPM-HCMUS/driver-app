package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.model.BookingItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailFragment extends Fragment {
    private TextView phone, name, price, addressDepart, addressDestination, time, method;
    private Button btn;
    private View mView;

    public  DetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail, container, false);
        phone = mView.findViewById(R.id.phone_detail);
        name = mView.findViewById(R.id.name_detail);
        price = mView.findViewById(R.id.price_detail);
        addressDepart = mView.findViewById(R.id.depart_detail);
       addressDestination = mView.findViewById(R.id.destination_detail);
        time = mView.findViewById(R.id.time_detail);
        btn = mView.findViewById(R.id.btn_back_detail);

        Bundle bundle = getArguments();
        if(bundle != null){
            BookingItem itemHistory = (BookingItem) bundle.get("object_history_item");
            if(itemHistory != null){
                Date date = new Date(itemHistory.getTimeStamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
                price.setText(itemHistory.getPrice());
                addressDepart.setText(itemHistory.getAddressDepart());
                time.setText(simpleDateFormat.format(date));
                name.setText(itemHistory.getName());
                addressDestination.setText(itemHistory.getAddressDestination());
                phone.setText(itemHistory.getPhoneNumber());
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager()!=null){
                    getFragmentManager().popBackStack();
                }
            }
        });

        return mView;
    }
}
