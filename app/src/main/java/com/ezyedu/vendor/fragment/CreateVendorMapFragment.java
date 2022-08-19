package com.ezyedu.vendor.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class CreateVendorMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double latitude;
    Double longitude;
    SharedPreferences sharedPreferences,sp;
    String session_id = null;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    FusedLocationProviderClient client;
    SupportMapFragment mMapFragment;
    double lat;
    double lon;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val", "");
        Log.i("Session_Histry_activity", session_id);

        sp = getContext().getSharedPreferences("LatiLongiVal", Context.MODE_PRIVATE);


        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.google_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);



        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_vendor_map, container, false);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url", base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global", img_url_base);


        client = LocationServices.getFusedLocationProviderClient(getActivity());
        return view;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        Log.i("FastLatLon", String.valueOf(lat + " " + lon));
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                googleMap.addMarker(new MarkerOptions().position(latLng).title("You are Here")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            }
                        });


                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("shared_latitude", String.valueOf(lat));
                        editor.putString("shared_longitude", String.valueOf(lon));
                        editor.commit();
                        //     markMyLocation(latLng);
                        //   onMapReady(mMap);
                    /*    mMap.addMarker(new MarkerOptions().position(latLng).title("You are Here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                     */

                        //    progressDialog = new ProgressDialog(getContext());
                        //  progressDialog.show();
                        // progressDialog.setContentView(R.layout.progress_dialog);
                        // progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                        //    nearMe(lat,lon);


                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                lat = location1.getLatitude();
                                lon = location1.getLongitude();
                                Log.i("FastLatLon", String.valueOf(lat + " " + lon));
                                LatLng latLng = new LatLng(location1.getLatitude(), location1.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng).title("You are Here")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("shared_latitude", String.valueOf(lat));
                                editor.putString("shared_longitude", String.valueOf(lon));
                                editor.commit();
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    }
                }
            });

        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}