package com.example.myapplication.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.LocationDriver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class FragmentHome extends Fragment implements OnMapReadyCallback {
    private View mView;
    public static GoogleMap mMap;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    Driver driver = null;
    static Marker locationMarker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        driver = (Driver) getArguments().get("object_driver");
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getCurrentLocation();
        return mView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(current));
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    updateLocationMarker(2,new LatLng(location.getLatitude(), location.getLongitude()), driver.getType());
//                                    currentLocation.setLatitude(location.getLatitude());
//                                    currentLocation.setLongitude(location.getLongitude());
                                }
                            });
                        }
                    }
                });
    }


    public static void updateLocationMarker(int typeOfUser, LatLng latLng, int typeOfVehicle){
        if(locationMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            if(typeOfUser==2){
                if(typeOfVehicle==1){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.motor_marker));
                }else{
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
                }
            }
            locationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }else{
            locationMarker.setPosition(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }

}