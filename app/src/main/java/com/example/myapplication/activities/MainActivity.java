package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiBooking;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.fragment.DetailFragment;
import com.example.myapplication.fragment.FragmentHistory;
import com.example.myapplication.fragment.FragmentTracking;
import com.example.myapplication.fragment.FragmentHome;
import com.example.myapplication.fragment.FragmentProfile;
import com.example.myapplication.fragment.FragmentTrackingEmpty;
import com.example.myapplication.fragment.MyBottomSheetDialogFragment;
import com.example.myapplication.model.BookingItem;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.LocationDriver;
import com.example.myapplication.model.LocationResponse;
import com.example.myapplication.utils.CalculatingDistance;
import com.example.myapplication.utils.GMapUtils;
import com.example.myapplication.utils.StreamingLocationUtils;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Driver driver;
    private DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private LocationRequest locationRequest;
    private LocationResponse locationResponse;
    public static LocationDriver locationDriver;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Bundle bundleDriver;
    Fragment fragmentHome;
    ExecutorService sendDriverLocationExecutor = Executors.newSingleThreadExecutor();
    ExecutorService sendClientLocationExecutor = Executors.newSingleThreadExecutor();
    private boolean isFlag = true;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) return;
            for(Location location : locationResult.getLocations()){
                Log.d("TAG", "onLocationResult" + location.toString());
                locationDriver.setLongitude(location.getLongitude());
                locationDriver.setLatitude(location.getLatitude());
                updateLocationDriver();
                FragmentHome.updateLocationMarker(driver.getRole(), new LatLng(location.getLatitude(), location.getLongitude()), locationDriver.getTypeOfVehicle(), location.getBearing());
                deleteAndUpdateRoute(location.getLatitude(),
                        location.getLongitude(),
                        10.7627,106.6823,
                        new LatLng(10.779382145655743, 106.66509467017718));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create location driver
        Driver driver = getDriver();
        String token = getToken();
        locationDriver = new LocationDriver(driver.getUserId(), driver.getType(), 0.0, 0.0, token);

        //navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //set bundle
        bundleDriver = getBundleDriver();

        //main fragment
        fragmentHome = new FragmentHome();
        fragmentHome.setArguments(bundleDriver);
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragmentHome)
                    .addToBackStack(null)
                    .commit();
            navigationView.setCheckedItem(R.id.nav_driver_home);
        }

        // toggle
        switchCompat = findViewById(R.id.btnWorking);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchCompat.isChecked()) {
                    checkSettingsAndStartLocationUpdates();
                } else {
                    stopLocationUpdate();
                }
            }
        });

        // get location request
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_driver_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragmentHome)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_profile:
                FragmentProfile fragmentProfile = new FragmentProfile();
                fragmentProfile.setArguments(bundleDriver);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragmentProfile)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.nav_tracking:
                Fragment fragment;
                if(locationDriver == null || locationResponse == null){
                    fragment = new FragmentTrackingEmpty();
                }else{
                    fragment = new FragmentTracking();
                    fragment.setArguments(setBundleDriverAndClient());
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout,fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_history:
                Fragment fragmentHistory = new FragmentHistory();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragmentHistory)
                        .addToBackStack(null)
                        .commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdate();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(MainActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    private void updateLocationDriver(){
        ApiService.apiService.updateLocationDriver(locationDriver).enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                Log.d("UPDATE LOCATION DRIVER", "successful");
                if(response.body()!=null){
                    locationResponse = (LocationResponse) response.body();
                    locationDriver.setStatus("MATCHED");
                    StreamingLocationUtils.sendLocation(sendDriverLocationExecutor, locationDriver.getDriverID());
                    StreamingLocationUtils.sendLocationClient(sendClientLocationExecutor, "0868944407", new LatLng(10.7627,106.6823));
                    showBottomDialog();
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.d("UPDATE LOCATION DRIVER", "failure");
            }
        });
    }

    private Bundle getBundleDriver(){
        Bundle bundle = null;
        if(getIntent().getExtras()!=null){
            driver = (Driver) getIntent().getExtras().get("object_driver_init");
            bundle = new Bundle();
            bundle.putSerializable("object_driver", driver);
        }
        return bundle;
    }

    private Driver getDriver(){
        if(getIntent().getExtras()!=null){
            return (Driver) getIntent().getExtras().get("object_driver_init");
        }
        return null;
    }

    private String getToken(){
        if(getIntent().getExtras()!=null){
            return (String) getIntent().getExtras().get("token");
        }
        return null;
    }

    private Bundle setBundleDriverAndClient(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_driver_information", locationDriver);
        bundle.putSerializable("object_client_information", locationResponse);
        return bundle;
    }
    private void showBottomDialog(){
        if(locationResponse==null) return;
        MyBottomSheetDialogFragment myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance(locationResponse, locationDriver);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), myBottomSheetDialogFragment.getTag());
        myBottomSheetDialogFragment.setCancelable(false);
    }

    public static LatLng getDriverForStream(){
        return new LatLng(locationDriver.getLatitude(), locationDriver.getLongitude());
    }

    public void goToDetailFragment(BookingItem bookingItem){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_history_item", bookingItem);
        detailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void deleteAndUpdateRoute(Double lat1, Double lon1, Double lat2, Double lon2, LatLng destination){
        double distance = CalculatingDistance.distance(lat1, lon1, lat2, lon2, 'M');
        Log.d("TAG123", String.valueOf(distance));
        if(distance*100 < 5) {
            if(GMapUtils.polylineFinal!=null&&isFlag){
                LatLng latLng2 = new LatLng(lat2, lon2);
                MainActivity.locationDriver.setStatus("WORKING");
                GMapUtils.deleteRoute();
                GMapUtils.direction(latLng2, destination, FragmentHome.mMap, this);
//                FragmentHome.updateLocationMarker(1, latLng2, -1, 0);
//                FragmentHome.updateLocationMarker(1, destination, -1, 0);
                isFlag = false;
            }

        }
    }


}