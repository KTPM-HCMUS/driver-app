package com.example.myapplication.activities;

import android.Manifest;
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
import com.example.myapplication.database.DBHandler;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.DriverTemp;
import com.example.myapplication.model.ResponseTT;
import com.example.myapplication.model.Result;
import com.example.myapplication.utils.JWTUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

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
        Driver driver = new Driver(phoneNumber.getText().toString(), password.getText().toString());
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
        Driver driver = JWTUtils.parseTokenToGetDriver(handler.readDB().get(1));
            try {
                String isValid = response.body().getResult().getLoginError();
                String refreshToken = response.body().getResult().getRefreshToken();
                String token = response.body().getResult().getToken();
                if(isValid.equals("SUCCESS")){
                    handler.update(refreshToken, token);
                    moveToMainPage(driver);
                }else{
                    getRefreshToken(driver.getUserId());
                    Toast.makeText(LoginActivity.this, "Login failure!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                getRefreshToken(driver.getUserId());
            }

    }

    private void failure(){
        Toast.makeText(LoginActivity.this, "Login failure!", Toast.LENGTH_SHORT).show();
    }


}
