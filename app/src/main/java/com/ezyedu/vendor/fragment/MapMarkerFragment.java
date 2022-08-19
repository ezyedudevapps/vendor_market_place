package com.ezyedu.vendor.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.vendor.Map_dragger_activity;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationSettingsResponse;

import com.google.android.gms.location.places.PlaceReport;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MapMarkerFragment extends Fragment implements OnMapReadyCallback {

    private static final int RESULT_OK = -1;
    View view;
    private GoogleMap mMap;
     Double latitude;
    Double longitude;
    SharedPreferences sharedPreferences;
    String session_id = null;
    RequestQueue requestQueue;

    LinearLayout linearLayout;
    private final static int PLACE_PICKER_REQUEST = 999;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_Histry_activity",session_id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_map_marker, container, false);



        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.google_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        requestQueue= Volley.newRequestQueue(getContext());

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        return view;

    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        String url = base_app_url+"api/vendor/info";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                String message = null;
                try {
                    message = response.getString("message");
                    if (message.equals("success")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        String lati = jsonObject.getString("latitude");
                        String longi = jsonObject.getString("longitude");
                        Log.i("Lati",lati);
                        latitude = Double.parseDouble(lati);
                        longitude = Double.parseDouble(longi);

                        Log.i("lati_frag",lati);
                        Log.i("Longi_frag",longi);

                        if (latitude != null)
                        {
                            LatLng latLng = new LatLng(latitude,longitude);
                            mMap = googleMap;
                            mMap.addMarker(new MarkerOptions().position(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                            float zoomLevel = 11.0f;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    Log.i("MapClicked","success");
                                    Intent intent1 = new Intent(getContext(), Map_dragger_activity.class);
                                    intent1.putExtra("latitude",latitude);
                                    intent1.putExtra("longitude",longitude);
                                    startActivity(intent1);
                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
}