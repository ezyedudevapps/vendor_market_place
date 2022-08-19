package com.ezyedu.vendor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.adapter.GalleryImageAdapter;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.adapter.VendorCategoryDescriptionAdapter;
import com.ezyedu.vendor.fragment.MapMarkerFragment;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Gallery_Images;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.RealPathUtil;
import com.ezyedu.vendor.model.VendorCategoryDescription;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Edit_profile_activity extends AppCompatActivity  implements LocationListener{

    private static final int PICK_IMAGE_REQUEST = 1;
    //location
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    private LocationRequest locationRequest;
    public static final int REQUEST_CHECK_SETTINGS = 1001;

    private List<VendorCategoryDescription> vendorCategoryDescriptionList = new ArrayList<>();
    RecyclerView recyc_ven;
    VendorCategoryDescriptionAdapter vendorCategoryDescriptionAdapter;

    int price_range;
    ProgressDialog progressDialog;

    double lat_int;
    double lon_int;

    int image_code = 0;

  public String latitude;
  public  String longitude;
    String session_id = null;
    RadioGroup radioGroup, price_group;
    RadioButton radioButton, one_p, two_p, three_p;
    int hour, minute;
    RelativeLayout relativeLayout, image_rel;
    RecyclerView recyclerView;
    GalleryImageAdapter galleryImageAdapter;
    List<Gallery_Images> galleryImagesList = new ArrayList<>();

    int m = 0, t = 0, w = 0, th = 0, f = 0, s = 0, su = 0;


    String logo_path = null;




    //selecting Time
    public TextView am1, pm1, monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    RequestQueue requestQueue;
    ImageView logo_get, logo_pick;
    Button update_btn, add_banner, view_banner;
    EditText insti_name, address, lat, longi, website, email, power_point, twitter, youtube, linkedin, instagram, v1, v2, fb;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_profile_activity);

            radioGroup = findViewById(R.id.rd_grp);
            am1 = findViewById(R.id.am_time);
            pm1 = findViewById(R.id.pm_time);


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


            price_group = findViewById(R.id.radio_price);
            one_p = findViewById(R.id.one);
            one_p.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    price_range = 1;
                    Log.i("priceRange",String.valueOf(price_range));
                }
            });
            two_p = findViewById(R.id.two);
            two_p.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    price_range = 2;
                    Log.i("priceRange",String.valueOf(price_range));
                }
            });
            three_p = findViewById(R.id.three);
            three_p.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    price_range = 3;
                    Log.i("priceRange",String.valueOf(price_range));
                }
            });

            ActivityCompat.requestPermissions(Edit_profile_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            //gallery img
            image_rel = findViewById(R.id.add_img);
            image_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_code = 2;
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                    {
                        Intent intent1= new Intent();
                        intent1.setType("image/*");
                        intent1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent1,10);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(Edit_profile_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                }
            });


            //logo
            relativeLayout = findViewById(R.id.img_pic_relative);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_code = 1;
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                    {
                        Intent intent1= new Intent();
                        intent1.setType("image/*");
                        intent1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent1,10);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(Edit_profile_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                }
            });

            requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();


            insti_name = findViewById(R.id.get_inst_name);
            logo_get = findViewById(R.id.logo);
            logo_pick = findViewById(R.id.logo_picker);
            address = findViewById(R.id.get_address);
            lat = findViewById(R.id.get_lat);
            longi = findViewById(R.id.get_long);

            monday = findViewById(R.id.m);
            tuesday = findViewById(R.id.t);
            wednesday = findViewById(R.id.w);
            thursday = findViewById(R.id.th);
            friday = findViewById(R.id.f);
            saturday = findViewById(R.id.s);
            sunday = findViewById(R.id.su);


            recyclerView = findViewById(R.id.gallery_recyc);

            LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager1);
            recyclerView.setHasFixedSize(true);
            galleryImageAdapter = new GalleryImageAdapter(Edit_profile_activity.this, galleryImagesList);
            recyclerView.setAdapter(galleryImageAdapter);
            fetchData();

            monday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log.i("ValueM", String.valueOf(m));
                    if (m == 0) {
                        monday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                        monday.setTextColor(Color.WHITE);
                        m = 1;
                        Log.i("ValueM", String.valueOf(m));
                    } else if (m == 1) {
                        monday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.green_50));
                        monday.setTextColor(Color.BLACK);
                        m = 0;
                        Log.i("ValueM", String.valueOf(m));
                    }
                }
            });
            tuesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (t == 0) {
                        tuesday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                        tuesday.setTextColor(Color.WHITE);
                        t = 1;
                    } else if (t == 1) {
                        tuesday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.green_50));
                        tuesday.setTextColor(Color.BLACK);
                        t = 0;
                    }
                }
            });
            wednesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (w == 0) {
                        wednesday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                        wednesday.setTextColor(Color.WHITE);
                        w = 1;
                    } else if (w == 1) {
                        wednesday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.green_50));
                        wednesday.setTextColor(Color.BLACK);
                        w = 0;
                    }
                }
            });
            thursday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (th == 0) {
                        thursday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                        thursday.setTextColor(Color.WHITE);
                        th = 1;
                    } else if (th == 1) {
                        thursday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.green_50));
                        thursday.setTextColor(Color.BLACK);
                        th = 0;
                    }
                }
            });
            friday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (f == 0) {
                        friday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                        friday.setTextColor(Color.WHITE);
                        f = 1;
                    } else if (f == 1) {
                        friday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.green_50));
                        friday.setTextColor(Color.BLACK);
                        f = 0;
                    }
                }
            });
            saturday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (s == 0) {
                        saturday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                        saturday.setTextColor(Color.WHITE);
                        s = 1;
                    } else if (s == 1) {
                        saturday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.green_50));
                        saturday.setTextColor(Color.BLACK);
                        s = 0;
                    }
                }
            });
            sunday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (su == 0) {
                        sunday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                        sunday.setTextColor(Color.WHITE);
                        su = 1;
                    } else if (su == 1) {
                        sunday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.green_50));
                        sunday.setTextColor(Color.BLACK);
                        su = 0;
                    }
                }
            });

            website = findViewById(R.id.get_website);
            email = findViewById(R.id.get_email);

            power_point = findViewById(R.id.ppt);
            twitter = findViewById(R.id.get_twitter);
            youtube = findViewById(R.id.get_youtube);
            linkedin = findViewById(R.id.get_linkedin);
            instagram = findViewById(R.id.get_instagram);
            v1 = findViewById(R.id.get_vimeo);
            v2 = findViewById(R.id.get_sixdeep);
            fb = findViewById(R.id.get_fb);

            update_btn = findViewById(R.id.update_profile_btn);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
            session_id = sharedPreferences.getString("session_val", "");
            Log.i("Session_main_activity", session_id);


            update_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        progressDialog = new ProgressDialog(Edit_profile_activity.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                        if (logo_path != null)
                        {
                            uploadImgage(logo_path);
                        }
                        else
                        {
                            updateData();
                        }
                     //   updateData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            //location..
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            recyc_ven = findViewById(R.id.ven_cat_recyc);
            LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            recyc_ven.setLayoutManager(manager);
            recyc_ven.setHasFixedSize(true);
            vendorCategoryDescriptionAdapter = new VendorCategoryDescriptionAdapter(Edit_profile_activity.this,vendorCategoryDescriptionList);
            recyc_ven.setAdapter(vendorCategoryDescriptionAdapter);

                    //location map fragment redirect
            MapMarker(new MapMarkerFragment());

        }

    private void MapMarker(MapMarkerFragment mapMarkerFragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,mapMarkerFragment);
        fragmentTransaction.commit();
    }


    //location
        public void checkButton(View view) {
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            // Toast.makeText(this, ""+radioButton.getText(), Toast.LENGTH_SHORT).show();
            String message = (String) radioButton.getText();
            if (message.equals("Pick From My Location")) {
                if (ActivityCompat.checkSelfPermission(Edit_profile_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Edit_profile_activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        }

        private void getLocation() {

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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 5, (LocationListener) Edit_profile_activity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.i("LatitudeUser", String.valueOf(location.getLatitude()));
            Log.i("LongieUser", String.valueOf(location.getLongitude()));
            lat.setText(String.valueOf(location.getLatitude()));
            longi.setText(String.valueOf(location.getLongitude()));
            try {
                Geocoder geocoder = new Geocoder(Edit_profile_activity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);
                Log.i("AddressMy", address);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                        Toast.makeText(Edit_profile_activity.this, "GPS is On...", Toast.LENGTH_SHORT).show();
                        getLocation();

                    } catch (ApiException e) {
                        e.printStackTrace();
                        switch (e.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                    resolvableApiException.startResolutionForResult(Edit_profile_activity.this, REQUEST_CHECK_SETTINGS);
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

        //add gallery image


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

            if (requestCode == 10 && resultCode == Activity.RESULT_OK)
            {
                if (image_code == 1) {
                    Uri uri = data.getData();
                    Context context = Edit_profile_activity.this;
                    String path = RealPathUtil.getRealPath(context,uri);
                    logo_path = path;
                  //  uploadImgage(path);
                }
                else if (image_code == 2)
                {
                    Uri uri = data.getData();
                    Context context = Edit_profile_activity.this;
                    String path = RealPathUtil.getRealPath(context,uri);
                    addImage(path);
                }
            }

            //image part
         /*   if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                if (image_code == 1) {
                    uploadImgage(getFilePath(this, data.getData()));
                } else if (image_code == 2) {
                    addImage(getFilePath(this, data.getData()));
                }
            }

          */
        }

        private void uploadImgage(String path) {
            try {
                File file = new File(path);
                Log.i("Path", path);
                Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file.getPath());
                Glide.with(this).load(path).into(((ImageView) findViewById(R.id.logo)));
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .writeTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(180, TimeUnit.SECONDS)
                        .build();
                if (file != null) {
                    MultipartBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("logo", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                            .build();
                    Log.i("Lg", file.getName());
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(base_app_url+"api/vendor/edit")
                            .addHeader("Authorization", session_id)
                            .addHeader("Content-Type", "application/json")
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("ResponseImgFailure", e.toString());
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                            Log.d("ResponseImg", response.body().string());
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        updateData();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(Edit_profile_activity.this, "Profile Uploaded Successfully", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
                Log.d("ResponseImage", e.toString());
            }
        }

        public void addImage(String path) {
            try {
                progressDialog = new ProgressDialog(Edit_profile_activity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                File file = new File(path);
                Log.i("Path", path);
                Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file.getPath());
                //  Glide.with(this).load(path).into(((ImageView) findViewById(R.id.logo)));
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .writeTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(180, TimeUnit.SECONDS)
                        .build();
                if (file != null) {
                    MultipartBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                            .build();
                    Log.i("Lg", file.getName());
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(base_app_url+"api/vendor/image")
                            .addHeader("Authorization", session_id)
                            .addHeader("Content-Type", "application/json")
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("ResponseImgAddFailure", e.toString());
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                            Log.d("ResponseImgGallery", response.body().string());
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(Edit_profile_activity.this, "Image Added  Successfully", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
                Log.d("ResponseImage", e.toString());
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 101) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "all permission's granted", Toast.LENGTH_LONG).show();
                  //  openFileChooser();
                } else {
                    ActivityCompat.requestPermissions(Edit_profile_activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }
            }
        }

        private void updateData() throws JSONException {
            String institution_name = insti_name.getText().toString();
            Log.i("instinm", institution_name);
            String address_institution = address.getText().toString();
            latitude = lat.getText().toString();
            longitude = longi.getText().toString();
            String web = website.getText().toString();
            String mail = email.getText().toString();
            String insta = instagram.getText().toString();
            String facebook = fb.getText().toString();
            String opn_time = am1.getText().toString();
            Log.i("Opn_time", opn_time);
            String close_time = pm1.getText().toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("inst_name", institution_name);


            jsonObject.put("open", am1.getText().toString());
            jsonObject.put("close", pm1.getText().toString());


            lat_int = Double.parseDouble(latitude);
            lon_int = Double.parseDouble(longitude);



            jsonObject.put("longitude", latitude);
            jsonObject.put("latitude", longitude);
            jsonObject.put("monday", m);
            jsonObject.put("tuesday", t);
            jsonObject.put("wednesday", w);
            jsonObject.put("thursday", th);
            jsonObject.put("friday", f);
            jsonObject.put("saturday", s);
            jsonObject.put("sunday", su);


            jsonObject.put("price_range",price_range);

            jsonObject.put("website", web);
            jsonObject.put("email", mail);

            jsonObject.put("address", address_institution);

            JSONArray jsonArray = new JSONArray();


            String inst =  instagram.getText().toString();
            if (!TextUtils.isEmpty(inst))
            {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("vendor_sosmed_id", 4);
                jsonObject1.put("content",inst);
                jsonArray.put(jsonObject1);
            }
            String facebk =  fb.getText().toString();
            if (!TextUtils.isEmpty(facebk))
            {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("vendor_sosmed_id", 1);
                jsonObject2.put("content",facebk);
                jsonArray.put(jsonObject2);
            }
            String lnkdn =  linkedin.getText().toString();
            if (!TextUtils.isEmpty(lnkdn))
            {
                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.put("vendor_sosmed_id", 3);
                jsonObject3.put("content",lnkdn);
                jsonArray.put(jsonObject3);
            }
            String twtr =  twitter.getText().toString();
            if (!TextUtils.isEmpty(twtr))
            {
                JSONObject jsonObject4 = new JSONObject();
                jsonObject4.put("vendor_sosmed_id", 7);
                jsonObject4.put("content",twtr);
                jsonArray.put(jsonObject4);
            }

            String ytub =  youtube.getText().toString();
            if (!TextUtils.isEmpty(ytub))
            {
                JSONObject jsonObject5 = new JSONObject();
                jsonObject5.put("vendor_sosmed_id", 6);
                jsonObject5.put("content",ytub);
                jsonArray.put(jsonObject5);
            }

            String v11 =  v1.getText().toString();
            if (!TextUtils.isEmpty(v11))
            {
                JSONObject jsonObject6 = new JSONObject();
                jsonObject6.put("vendor_sosmed_id", 9);
                jsonObject6.put("content", v11);
                jsonArray.put(jsonObject6);
            }
            String v22 =  v2.getText().toString();
            if (!TextUtils.isEmpty(v22))
            {
                JSONObject jsonObject7 = new JSONObject();
                jsonObject7.put("vendor_sosmed_id", 7);
                jsonObject7.put("content", v2.getText().toString());
                jsonArray.put(jsonObject7);
            }

            if (jsonArray.length()>0)
            {
                jsonObject.put("sosmed_contents", jsonArray);
            }
            Log.i("JsonRequestEdit", jsonObject.toString());
            Log.i("JSONArraySend", String.valueOf(jsonArray.length()));

            String url = base_app_url+"api/vendor/edit";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("ResponseUpdate", response.toString());
                    try {
                        String message = response.getString("message");
                        if (message.equals("update successfull")) {
                            progressDialog.dismiss();
                            Toast.makeText(Edit_profile_activity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Edit_profile_activity.this,MainActivity.class);
                            startActivity(intent1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("CatchUpdate",e.toString());
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ResonseErrorUpdate", error.toString());

                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        String jsonError = new String(networkResponse.data);
                        Log.i("RegisterFailure", jsonError.toString());
                        try {

                            JSONObject jsonObject1= new JSONObject(jsonError);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                            Log.i("message",jsonObject2.toString());
                            Toast.makeText(Edit_profile_activity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }
            }) {
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

        private void fetchData() {
            String url = base_app_url+"api/vendor/info";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("ResponseEdit", response.toString());
                    try {
                        String message = response.getString("message");
                        if (message.equals("success")) {
                            JSONObject jsonObject = response.getJSONObject("data");

                            JSONArray jsonArrayImg = jsonObject.getJSONArray("vendor_images");
                            Log.i("ArrayImage_Size", String.valueOf(jsonArrayImg.length()));
                            if (jsonArrayImg.length() > 0) {
                                for (int i = 0; i < jsonArrayImg.length(); i++) {
                                    JSONObject jb = jsonArrayImg.getJSONObject(i);
                                    int id = jb.getInt("id");
                                    String img = jb.getString("image");
                                    Gallery_Images post = new Gallery_Images(id, img);
                                    galleryImagesList.add(post);
                                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                                }
                            }
                            String inst_name = jsonObject.getString("inst_name");
                            insti_name.setText(inst_name);

                         /*   String main_inst_name = jsonObject.getString("main_inst_name");
                            String category_name = jsonObject.getString("category_name");
                            String sub_category_name = jsonObject.getString("sub_category_name");
                          */
                            String logo = jsonObject.getString("logo");
                            String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                            Glide.with(Edit_profile_activity.this).load(img_url_base + logo).into(logo_get);

                            String addresss = jsonObject.getString("address");
                            address.setText(addresss);

                            latitude = jsonObject.getString("latitude");
                            longitude = jsonObject.getString("longitude");
                            lat.setText(latitude);
                            longi.setText(longitude);


                            String web = jsonObject.getString("website");
                            website.setText(web);
                            String mail = jsonObject.getString("email");
                            email.setText(mail);

                            price_range = jsonObject.getInt("price_range");
                            if (price_range == 1) {
                                price_group.check(R.id.one);
                            } else if (price_range == 2) {
                                price_group.check(R.id.two);
                            } else if (price_range == 3) {
                                price_group.check(R.id.three);
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray("operatings");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                int id = jsonObject1.getInt("id");
                                int vendor_id = jsonObject1.getInt("vendor_id");
                                String open1 = jsonObject1.getString("open");
                                String[] open_tm = open1.split(":");
                                String o1 = open_tm[0];
                                String o2 = open_tm[1];
                                String open = o1 + ":" + o2;
                                Log.i("OPenTm", open);
                                am1.setText(open);
                                String close1 = jsonObject1.getString("close");
                                String[] close_tm = close1.split(":");
                                String c1 = close_tm[0];
                                String c2 = close_tm[1];
                                String close = c1 + ":" + c2;
                                Log.i("OPenTm", close);
                                pm1.setText(close);

                                int mon = jsonObject1.getInt("monday");
                                if (mon == 1) {
                                    monday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                                    monday.setTextColor(Color.WHITE);
                                    m = 1;
                                }
                                int tues = jsonObject1.getInt("tuesday");
                                if (tues == 1) {
                                    tuesday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                                    tuesday.setTextColor(Color.WHITE);
                                    t = 1;
                                }
                                int wednes = jsonObject1.getInt("wednesday");
                                if (wednes == 1) {
                                    wednesday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                                    wednesday.setTextColor(Color.WHITE);
                                    w = 1;
                                }
                                int thurs = jsonObject1.getInt("thursday");
                                if (thurs == 1) {
                                    thursday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                                    thursday.setTextColor(Color.WHITE);
                                    th = 1;
                                }
                                int fri = jsonObject1.getInt("friday");
                                if (fri == 1) {
                                    friday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                                    friday.setTextColor(Color.WHITE);
                                    f = 1;
                                }
                                int sat = jsonObject1.getInt("saturday");
                                if (sat == 1) {
                                    saturday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                                    saturday.setTextColor(Color.WHITE);
                                    s = 1;
                                }
                                int sun = jsonObject1.getInt("sunday");
                                if (sun == 1) {
                                    sunday.setBackgroundColor(ContextCompat.getColor(Edit_profile_activity.this, R.color.orange_500));
                                    sunday.setTextColor(Color.WHITE);
                                    su = 1;
                                }
                            }
                            JSONArray jsonArray1 = jsonObject.getJSONArray("social_medias");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                int id = jsonObject2.getInt("id");
                                String name = jsonObject2.getString("name");
                                String clogo = jsonObject2.getString("logo");
                                String url = jsonObject2.getString("url");
                                if (id == 4 && url != "null") {
                                    instagram.setText(url);
                                } else if (id == 1 && url != "null") {
                                    fb.setText(url);
                                } else if (id == 3 && url != "null") {
                                    linkedin.setText(url);
                                } else if (id == 7 && url != "null") {
                                    twitter.setText(url);
                                } else if (id == 6 && url != "null") {
                                    youtube.setText(url);
                                } else if (id == 9 && url != "null") {
                                    power_point.setText(url);
                                } else if (id == 9 && url != "null") {
                                    v2.setText(url);
                                }
                            }
                            JSONArray jsonArray2 = jsonObject.getJSONArray("category_description_contents");
                            Log.i("JSONCATDESC",jsonArray2.toString());
                            for (int i = 0; i<jsonArray2.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
                                int cat_desc_content_id = jsonObject1.getInt("cat_desc_id");
                                String title = jsonObject1.getString("title");
                                String content = jsonObject1.getString("content");
                                Log.i("jsonContent",content);
                                VendorCategoryDescription post = new VendorCategoryDescription(cat_desc_content_id,title,content);
                                vendorCategoryDescriptionList.add(post);
                                recyc_ven.getAdapter().notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(Edit_profile_activity.this, "Something Went Wrong, Try Again Later...", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("CatchEditInsti",e.toString());
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ErrorEdit", error.toString());
                }
            }) {
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



        //time (am)
        public void popTimePicker(View view) {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    am1.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();

        }

        public void pm_picker(View view) {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    pm1.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Edit_profile_activity.this,MainActivity.class);
        startActivity(intent1);
    }
}