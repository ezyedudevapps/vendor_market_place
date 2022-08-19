package com.ezyedu.vendor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;




public class Map_dragger_activity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    GoogleMap gMap;
    Double latitude;
    Double longitude;
    String address = null;
    EditText editText;
    Button button;
    String session_id = null;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ImageView imageView,current_location;







    //current location...
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    private LocationRequest locationRequest;
    public static final int REQUEST_CHECK_SETTINGS = 1001;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_dragger_activity);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_chat_activity",session_id);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();


        //current location..
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        current_location = findViewById(R.id.my_loc_icon);
        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Map_dragger_activity.this, "My Location", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(Map_dragger_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    progressDialog = new ProgressDialog(Map_dragger_activity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                } else {
                    ActivityCompat.requestPermissions(Map_dragger_activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });


        imageView = findViewById(R.id.img_map_move);
        editText = findViewById(R.id.ed_address);
        button = findViewById(R.id.address_select_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (latitude != null && longitude != null && address != null)
                    {
                        progressDialog = new ProgressDialog(Map_dragger_activity.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        updateLocation();
                    }
                    else
                    {
                        Toast.makeText(Map_dragger_activity.this, "Location Not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {

            getAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_mark);
        supportMapFragment.getMapAsync(this);

    }



    private void updateLocation() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("longitude", longitude);
        jsonObject.put("latitude", latitude);
        jsonObject.put("address", address);
        Log.i("jsonAddressReq",jsonObject.toString());
        String url = base_app_url+"api/vendor/edit";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonAddress",response.toString());
                try {
                    String mesage = response.getString("message");
                    if (mesage.equals("update successfull"))
                    {
                        progressDialog.dismiss();
                        Intent intent1 = new Intent(Map_dragger_activity.this,Edit_profile_activity.class);
                        startActivity(intent1);
                        Toast.makeText(Map_dragger_activity.this, mesage, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Map_dragger_activity.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
         gMap = googleMap;

         //add marker
        LatLng latLng = new LatLng(latitude,longitude);
         gMap.addMarker(new MarkerOptions().position(latLng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        float zoomLevel = 15.0f;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));


        //after adding marker to get address..
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size()>0)
        {
            Address address1 = addresses.get(0);
            address = address1.getAddressLine(0);
            Log.i("addressChsanged",address);
            editText.setText(address);
            //get city or pincode...
            String location = address1.getLocality();
            String postal_code = address1.getPostalCode();
            String country = address1.getCountryName();
            Log.i("Other_address  ",location+"  "+postal_code+"  "+country);
        }

        //movable dragger....
        gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                gMap.clear();
                imageView.setVisibility(View.VISIBLE);
                latitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;

                Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));
                Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));

                //after adding marker to get address..
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude,longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size()>0)
                {
                    Address address1 = addresses.get(0);
                    address = address1.getAddressLine(0);
                    Log.i("addressChsanged",address);
                    editText.setText(address);
                    //get city or pincode...
                    String location = address1.getLocality();
                    String postal_code = address1.getPostalCode();
                    String country = address1.getCountryName();
                    Log.i("Other_address  ",location+"  "+postal_code+"  "+country);
                }
            }
        });

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
             @SuppressLint("LongLogTag")
             @Override
             public void onMapClick(LatLng latLng) {
                 MarkerOptions markerOptions = new MarkerOptions();
                 markerOptions.position(latLng);
                 markerOptions.title(latLng.latitude+ " "+latLng.longitude);
                 gMap.clear();//clears previous click position...
                 gMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                 float zoomLevel = 13.0f;
                 gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));
                 gMap.addMarker(markerOptions);
                 Log.i("LatLongVal", String.valueOf(latLng.latitude)+" "+latLng.longitude);


                 //moving location...




                 latitude = latLng.latitude;
                 longitude = latLng.longitude;

                 try {

                     getAddress();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         });
    }

    private void getAddress() throws IOException {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
        if (addresses.size()>0)
        {
            Address address1 = addresses.get(0);
            address = address1.getAddressLine(0);
            Log.i("addressChsanged",address);
            editText.setText(address);

            //get city or pincode...
            String location = address1.getLocality();
            String postal_code = address1.getPostalCode();
            String country = address1.getCountryName();
            Log.i("Other_address  ",location+"  "+postal_code+"  "+country);
        }
    }


    //current location....
    private void getLocation()
    {
        try {


            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 5, (LocationListener) Map_dragger_activity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        Log.i("LatitudeUser", String.valueOf(location.getLatitude()));
        Log.i("LongieUser", String.valueOf(location.getLongitude()));
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //add current location in map....
        if (latitude != null && longitude != null)
        {
            gMap.clear();
            AddMyMarker(gMap);
        }


        try {
            Geocoder geocoder = new Geocoder(Map_dragger_activity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            Log.i("AddressMy", address);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void AddMyMarker(GoogleMap gMap)
    {
        //add marker
        LatLng latLng = new LatLng(latitude,longitude);
        gMap.addMarker(new MarkerOptions().position(latLng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        float zoomLevel = 15.0f;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));

        //after adding marker to get address..
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size()>0)
        {
            Address address1 = addresses.get(0);
            address = address1.getAddressLine(0);
            Log.i("addressChsanged",address);
            editText.setText(address);
            //get city or pincode...
            String location = address1.getLocality();
            String postal_code = address1.getPostalCode();
            String country = address1.getCountryName();
            Log.i("Other_address  ",location+"  "+postal_code+"  "+country);
        }

        progressDialog.dismiss();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Please Turn on the Location", Toast.LENGTH_SHORT).show();
        Log.i("Provid", provider);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(Map_dragger_activity.this, "GPS is On...", Toast.LENGTH_SHORT).show();
                    getLocation();
                } catch (ApiException e) {
                    e.printStackTrace();
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(Map_dragger_activity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;

                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //location part
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(this, "GPS is Turned On...", Toast.LENGTH_SHORT).show();
                    getLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Turn on location Manually ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}