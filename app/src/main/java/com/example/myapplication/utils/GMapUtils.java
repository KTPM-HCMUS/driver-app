package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GMapUtils {
    public static ArrayList<Polyline> polylineFinal;
    public static Marker markerCurrent, markerDestination;
    public static void direction(LatLng address1, LatLng address2, GoogleMap mMap, Activity context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        polylineFinal = new ArrayList<>();
        markerCurrent = null;
        markerDestination = null;
        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("destination", address1.latitude + "," + address1.longitude)
                .appendQueryParameter("origin", address2.latitude + "," + address2.longitude)
                .appendQueryParameter("mode", "driving")
                .appendQueryParameter("key", "AIzaSyCyOWzvbd05Y2YN3fEMwQ1Rxm5VSSlDZHA")
                .toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("OK")) {
                        JSONArray routes = response.getJSONArray("routes");
                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;
                        for (int i = 0; i < routes.length(); i++) {
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JSONArray steps = routes.getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                            for (int k = 0; k < steps.length(); k++) {
                                String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                                List<LatLng> list = decodePoly(polyline);
                                for (int l = 0; l < list.size(); l++) {
                                    LatLng position = new LatLng(list.get(l).latitude, list.get(l).longitude);
                                    points.add(position);
                                }
                            }
                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(context, R.color.red));
                            polylineOptions.geodesic(true);
                        }
                        polylineFinal.add(mMap.addPolyline(polylineOptions));
                        markerCurrent = mMap.addMarker(new MarkerOptions().position(new LatLng(address1.latitude, address1.longitude)));
                        markerDestination = mMap.addMarker(new MarkerOptions().position(new LatLng(address2.latitude, address2.longitude)));

                        LatLngBounds bounds = new LatLngBounds.Builder()
                                .include(new LatLng(address1.latitude, address1.longitude))
                                .include(new LatLng(address2.latitude, address2.longitude))
                                .build();

                        Point point = new Point();
                        context.getWindowManager().getDefaultDisplay().getSize(point);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 150, 30));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        });
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(jsonObjectRequest);
    }
    private static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static void deleteRoute(){
        for(Polyline polyline : polylineFinal){
            polyline.remove();
        }
        polylineFinal.clear();
        if(markerCurrent != null){
            markerCurrent.remove();
            markerCurrent = null;
        }
        if(markerDestination != null){
            markerDestination.remove();
            markerDestination = null;
        }
    }
}
