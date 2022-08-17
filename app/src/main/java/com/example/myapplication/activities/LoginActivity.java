package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiLogin;
import com.example.myapplication.client.LocationStreamingClient;
import com.example.myapplication.database.DBHandler;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.DriverTemp;
import com.example.myapplication.model.LoginModel;
import com.example.myapplication.model.ResponseTT;
import com.example.myapplication.model.Result;
import com.example.myapplication.utils.JWTUtils;
import com.ktpm.vehiclebooking.LocationOuterClass;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneNumber, password;
    private Button btn;
    private DBHandler handler = new DBHandler(LoginActivity.this);
    private TextView registerForm;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = findViewById(R.id.loginPhoneNumberEditText);
        password  = findViewById(R.id.loginPasswordEditText);
        btn = findViewById(R.id.loginLoginBtn);
        registerForm = findViewById(R.id.moveToRegisterTextView);
        testStreaming();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginWithUserIdAndPassword();
            }
        });

        registerForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginWithToken();
    }

    private void LoginWithUserIdAndPassword(){
        LoginModel driver = new LoginModel(phoneNumber.getText().toString(), password.getText().toString());
        ApiLogin.apiService.loginDriver(driver).enqueue(new Callback<ResponseTT>() {
            @Override
            public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
                success(response);
            }

            @Override
            public void onFailure(Call<ResponseTT> call, Throwable t) {
                failure();
            }
        });
    }
    private void loginWithToken(){
        ArrayList<String> s = handler.readDB();
        if(s.size() != 0){
            checkLogin(handler.readDB().get(1));
        }
    }

    private void checkLogin(String token){
        ApiLogin.apiService.revokeToken("Bearer " + token).enqueue(new Callback<ResponseTT>() {
            @Override
            public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
                success(response);
            }

            @Override
            public void onFailure(Call<ResponseTT> call, Throwable t) {
                failure();
            }
        });
    }


    private void moveToMainPage(Driver driver){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_driver", driver);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getRefreshToken(String userId){
        ArrayList<String> list = handler.readDB();
        String refreshToken = list.get(0);
        DriverTemp driverTemp = new DriverTemp(userId, refreshToken);
        ApiLogin.apiService.getNewToken(driverTemp).enqueue(new Callback<ResponseTT>() {
            @Override
            public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
               success(response);
            }

            @Override
            public void onFailure(Call<ResponseTT> call, Throwable t) {
                failure();
            }
        });

    }



    private void success(Response<ResponseTT> response){
        ArrayList<String> s = handler.readDB();
        Driver driver = JWTUtils.parseTokenToGetDriver(s.get(1));
        try {
            String isValid = response.body().getResult().getLoginError();
            String refreshToken = response.body().getResult().getRefreshToken();
            String token = response.body().getResult().getToken();
            if(isValid.equals("SUCCESS")){
                writeDB(refreshToken, token, isValid);
                moveToMainPage(driver);
            }else{
                if(s.size() != 0){
                    getRefreshToken(driver.getUserId());
                }
                Toast.makeText(LoginActivity.this, "Username or password is wrong", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            getRefreshToken(driver.getUserId());
        }

    }

    private void failure(){
        Toast.makeText(LoginActivity.this, "Login failure!", Toast.LENGTH_SHORT).show();
    }

    public void writeDB(String refreshToken, String token, String isValid){
        Result result = new Result(refreshToken, token, isValid);
        ArrayList<String> s  = handler.readDB();
        if(s.size()==0){
            handler.addNewToken(refreshToken, token);
        }else{
            handler.update(refreshToken, token);
        }
    }

    public void testStreaming() {
        Thread thread = new Thread(() -> {
            LocationStreamingClient client = new LocationStreamingClient();
            ExecutorService sendDriverLocationExecutor = Executors.newSingleThreadExecutor();
            ExecutorService getLocationExecutor = Executors.newSingleThreadExecutor();
            ExecutorService sendCustomerLocationExecutor = Executors.newSingleThreadExecutor();

            client.sendLocation("customer_test", () -> {
                Random rd = new Random();
                return LocationOuterClass.Location.newBuilder()
                        .setLatitude(rd.nextDouble())
                        .setLongitude(rd.nextDouble())
                        .build();
            }, sendCustomerLocationExecutor);
            client.sendLocation("driver_test", () -> {
                Random rd = new Random();
                return LocationOuterClass.Location.newBuilder()
                        .setLatitude(rd.nextDouble())
                        .setLongitude(rd.nextDouble())
                        .build();
            }, sendDriverLocationExecutor);
            client.getLocation("customer_test", "driver_test", response -> {
                System.out.println("======================================");
                System.out.println(Thread.currentThread().getName());
                System.out.println("customer_test latitude=" + response.getCustomerLocation().getLatitude() + ", longitude=" + response.getCustomerLocation().getLongitude());
                System.out.println("driver_test location=" + response.getDriverLocation().getLatitude() + ", longitude=" + response.getDriverLocation().getLongitude());
                System.out.println("======================================");
            }, getLocationExecutor);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                sendCustomerLocationExecutor.shutdown();
                sendDriverLocationExecutor.shutdown();
                getLocationExecutor.shutdown();
                countDownLatch.countDown();
            }));
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            thread.stop();
        }));
    }
}
