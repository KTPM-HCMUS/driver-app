package com.example.myapplication.utils;

import android.util.Base64;
import android.util.Log;

import com.example.myapplication.model.Driver;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JWTUtils {

    public static JSONObject decoded(String JWTEncoded) throws Exception {
        JSONObject s1 = null;
        try {
            String[] split = JWTEncoded.split("\\.");
            s1 = new JSONObject(getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            //Error
            e.printStackTrace();
        }
        return s1;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static Driver parseTokenToGetDriver(String token){
        Driver driver = null;
        try {
            JSONObject s = JWTUtils.decoded(token);
            String userId = s.getString("userId");
            String name = s.getString("userName");
            String dob = s.getString("dob");
            String email = s.getString("email");
            int type = s.getInt("type");
            String vehiclePlate = s.getString("vehicle_plate");
            driver = new Driver(userId, name, dob, email, type, vehiclePlate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return driver;
    }
}