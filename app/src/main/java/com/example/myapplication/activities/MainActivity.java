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
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.fragment.FragmentHome;
import com.example.myapplication.fragment.FragmentProfile;
import com.example.myapplication.fragment.MyBottomSheetDialogFragment;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.LocationDriver;
import com.example.myapplication.model.LocationResponse;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Driver driver;
    private DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private LocationRequest locationRequest;
    private LocationResponse locationResponse;
    private LocationDriver locationDriver;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Bundle bundleDriver;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) return;
            for(Location location : locationResult.getLocations()){
                Log.d("TAG", "onLocationResult" + location.toString());
                locationDriver.setLongitude(location.getLongitude());
                locationDriver.setLatitude(location.getLatitude());
                updateLocationDriver(location.getLongitude(), location.getLatitude());
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
        Fragment fragment = new FragmentHome();

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
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
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(4000);
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
                        .replace(R.id.frame_layout, new FragmentHome())
                        .commit();
                break;
            case R.id.nav_profile:
                FragmentProfile fragmentProfile = new FragmentProfile();
                fragmentProfile.setArguments(bundleDriver);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragmentProfile)
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

    private void updateLocationDriver(Double longitude, Double latitude){
        locationDriver.setLatitude(latitude);
        locationDriver.setLongitude(longitude);
        ApiService.apiService.updateLocationDriver(locationDriver).enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                Log.d("UPDATE LOCATION DRIVER", "successful");
                if(response.body()!=null){
                    locationResponse = (LocationResponse) response.body();
//                    bundleClient = new Bundle();
//                    bundleClient.putSerializable("object_client", locationResponse);
                    locationDriver.setStatus("MATCHED");
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

    private void showBottomDialog(){
        if(locationResponse==null) return;
        MyBottomSheetDialogFragment myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance(locationResponse, locationDriver);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), myBottomSheetDialogFragment.getTag());
        myBottomSheetDialogFragment.setCancelable(false);
    }
}