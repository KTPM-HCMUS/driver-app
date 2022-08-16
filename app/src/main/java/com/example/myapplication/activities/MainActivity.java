package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FragmentHome;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.LocationDriver;
import com.example.myapplication.utils.MyJobService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

//public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private static final int JOB_ID = 123;
//    private SwitchCompat switchCompat;
//    GoogleMap mMap;
//    SupportMapFragment mapFragment;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    LocationDriver currentLocation = new LocationDriver();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
//            getCurrentLocation();
//        }else{
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }
//
//        switchCompat = findViewById(R.id.btnWorking);
//        switchCompat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(switchCompat.isChecked()){
//                    onClickStartWorking();
//                }else{
//                    onClickEndWorking();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(current));
////        mMap.setMyLocationEnabled(true);
////        mMap.getUiSettings().setZoomControlsEnabled(true);
//    }
//
//    private void getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location != null){
//                    mapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(@NonNull GoogleMap googleMap) {
//                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                            MarkerOptions options = new MarkerOptions().position(latLng);
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                            googleMap.addMarker(options);
//                            currentLocation.setLatitude(location.getLatitude());
//                            currentLocation.setLongitude(location.getLongitude());
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 44) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
//            }
//        }
//    }
//
//    private void onClickStartWorking(){
//        PersistableBundle bundle = new PersistableBundle();
//        bundle.putDouble("lat", currentLocation.getLatitude());
//        bundle.putDouble("lon", currentLocation.getLongitude());
//        ComponentName componentName = new ComponentName(this, MyJobService.class);
//        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                .setPeriodic(15*60*1000)
//                .setExtras(bundle)
//                .build();
//
//        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(jobInfo);
//    }
//
//    private void onClickEndWorking(){
//        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        jobScheduler.cancel(JOB_ID);
//    }
//
//    private void setDataObject(LocationDriver locationDriver){
//        LocationDriver locationDriver1 = new LocationDriver(1, locationDriver.getLongitude(), locationDriver.getLatitude());
//        Gson g = new Gson();
//        String json = g.toJson(locationDriver1);
//
//        PersistableBundle bundle = new PersistableBundle();
//        bundle.putString("data", json);
//    }
//}

public class MainActivity extends AppCompatActivity {
    private Driver driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = new FragmentHome();

        if(getIntent().getExtras()!=null){
            driver = (Driver) getIntent().getExtras().get("object_driver");
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_driver", driver);
            fragment.setArguments(bundle);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}