package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiLogin;
import com.example.myapplication.database.DBHandler;
import com.example.myapplication.model.Driver;
import com.example.myapplication.model.ResponseTT;
import com.example.myapplication.model.Result;
import com.example.myapplication.utils.JWTUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText phoneNumber, password, name, dob, role, email, vehiclePlate;
    private RadioGroup type;
    private Button btn, btnBack;
    private static final int TYPE_DRIVER = 1;
    private DBHandler dbHandler = new DBHandler(RegisterActivity.this);

    //1: motorbike //2: car
    private int type_vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_final);

        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        email = findViewById(R.id.email);
        type = findViewById(R.id.rdoPlate);
        vehiclePlate = findViewById(R.id.vehiclePlate);
        btnBack = findViewById(R.id.registerFinalBackBtn);
        btn = findViewById(R.id.registerFinalRegisterBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onRegister(){
        int chooseTypeOfVehicle = type.getCheckedRadioButtonId();
        if(chooseTypeOfVehicle == -1){
            Toast.makeText(this, "Please fulfill form register!", Toast.LENGTH_SHORT).show();
        }else{
            if(chooseTypeOfVehicle == R.id.rdoMotorbike){
                type_vehicle = 1;
            }else if(chooseTypeOfVehicle == R.id.rdoCar){
                type_vehicle = 2;
            }
        }
        Driver driver = new Driver(phoneNumber.getText().toString(), password.getText().toString(),
                name.getText().toString(), dob.getText().toString(),TYPE_DRIVER,
                email.getText().toString(), type_vehicle, vehiclePlate.getText().toString());

        ApiLogin.apiService.registerDriver(driver).enqueue(new Callback<ResponseTT>() {
            @Override
            public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
                if(response.isSuccessful()){
                    try {
                        String isValid = response.body().getResult().getLoginError();
                        String refreshToken = response.body().getResult().getRefreshToken();
                        String token = response.body().getResult().getToken();
                        Driver driver = JWTUtils.parseTokenToGetDriver(token);
                        if(isValid.equals("SUCCESS")){
                            writeDB(refreshToken, token, isValid);
                            Toast.makeText(RegisterActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();
                            moveToMainPage(driver);
                        }else{
                            Toast.makeText(RegisterActivity.this, "Register failure!", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTT> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(RegisterActivity.this, "Register failure!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void writeDB(String refreshToken, String token, String isValid){
        Result result = new Result(refreshToken, token, isValid);
        ArrayList<String> s  = dbHandler.readDB();
        if(s.size()==0){
            dbHandler.addNewToken(refreshToken, token);
        }else{
            dbHandler.update(refreshToken, token);
        }
    }

    private void moveToMainPage(Driver driver){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_driver", driver);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
