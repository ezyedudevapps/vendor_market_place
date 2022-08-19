package com.ezyedu.vendor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.fragment.CreateVendorMapFragment;
import com.ezyedu.vendor.fragment.MapMarkerFragment;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Create_institution_form extends AppCompatActivity {


    String session_id = null;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String path;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    private LocationRequest locationRequest;
    public static final int REQUEST_CHECK_SETTINGS = 1001;

    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<Integer> plist = new ArrayList<Integer>();


   // ArrayList<Integer> idList = new ArrayList<>();
   // ArrayList<String> contentList = new ArrayList<>();
    TextView categories;
    Dialog dialog;
    Button create_btn;

    int m = 0, t = 0, w = 0, th = 0, f = 0, s = 0, su = 0;
    int hour, minute;

    EditText insti_name,insti_address,institution_mail,institute_phone;
    RadioGroup radioGroup;
    RadioButton  one_p, two_p, three_p;

    String cat_label,cat_hash,sub_cat_label,sub_cat_hash,group_name;
    TextView cat_lbl,sub_cat_lbl,fclty;
    public TextView am1, pm1, monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    int dist_id;
    ImageView logo_get, logo_pick;
    int price_range;
    RelativeLayout image_rel;

    String name,address,latitude,longitude,logo,vendor_category,email,phone,prc_rng,district,open,close,
            mon,tues,wednes,thurs,fri,satur,sun,holiday;
    int id;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    String latitu;
    String longitu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_institution_form);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        id = getIntent().getIntExtra("id",0);
        cat_label = getIntent().getStringExtra("cat_label");
        cat_hash = getIntent().getStringExtra("cat_hash");
        sub_cat_label = getIntent().getStringExtra("sub_cat_label");
        sub_cat_hash = getIntent().getStringExtra("sub_cat_hash");
        group_name = getIntent().getStringExtra("group_name");



        Log.i("idGrp", String.valueOf(id));
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("LatiLongiVal", Context.MODE_PRIVATE);
        latitu  = sharedPreferences1.getString("shared_latitude","");
        longitu  = sharedPreferences1.getString("shared_longitude","");
        Log.i("SharedValues",latitu+" "+longitu);


        cat_lbl = findViewById(R.id.cate_lbl);
        sub_cat_lbl = findViewById(R.id.sub_cate_lbl);

        cat_lbl.setText(cat_label);
        sub_cat_lbl.setText(sub_cat_label);
        create_btn = findViewById(R.id.update_profile_btn);


        fetchDistrict();
        categories = findViewById(R.id.txt_categories);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Create_institution_form.this);
                dialog.setContentView(R.layout.dialog_all_district_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Create_institution_form.this,android.R.layout.simple_list_item_1,mylist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        categories.setText(adapter.getItem(position));
                        int pos = position;
                        Log.i("catPos", String.valueOf(pos));
                        dialog.dismiss();
                        dist_id = plist.get(position);
                        Log.i("Hash_id_val", String.valueOf(dist_id));
                    }
                });
            }
        });


        am1 = findViewById(R.id.am_time);
        pm1 = findViewById(R.id.pm_time);

        radioGroup = findViewById(R.id.radio_price);
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
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createinstitute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    /*    image_rel = findViewById(R.id.img_pic_relative);
        image_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Create_institution_form.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Create_institution_form.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Create_institution_form.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser();
                } else {
                    ActivityCompat.requestPermissions(Create_institution_form.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }
            }
        });

     */

        monday = findViewById(R.id.m);
        tuesday = findViewById(R.id.t);
        wednesday = findViewById(R.id.w);
        thursday = findViewById(R.id.th);
        friday = findViewById(R.id.f);
        saturday = findViewById(R.id.s);
        sunday = findViewById(R.id.su);

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.i("ValueM", String.valueOf(m));
                if (m == 0) {
                    monday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.orange_500));
                    monday.setTextColor(Color.WHITE);
                    m = 1;
                    Log.i("ValueM", String.valueOf(m));
                } else if (m == 1) {
                    monday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.green_50));
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
                    tuesday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.orange_500));
                    tuesday.setTextColor(Color.WHITE);
                    t = 1;
                } else if (t == 1) {
                    tuesday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.green_50));
                    tuesday.setTextColor(Color.BLACK);
                    t = 0;
                }
            }
        });
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (w == 0) {
                    wednesday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.orange_500));
                    wednesday.setTextColor(Color.WHITE);
                    w = 1;
                } else if (w == 1) {
                    wednesday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.green_50));
                    wednesday.setTextColor(Color.BLACK);
                    w = 0;
                }
            }
        });
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (th == 0) {
                    thursday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.orange_500));
                    thursday.setTextColor(Color.WHITE);
                    th = 1;
                } else if (th == 1) {
                    thursday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.green_50));
                    thursday.setTextColor(Color.BLACK);
                    th = 0;
                }
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (f == 0) {
                    friday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.orange_500));
                    friday.setTextColor(Color.WHITE);
                    f = 1;
                } else if (f == 1) {
                    friday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.green_50));
                    friday.setTextColor(Color.BLACK);
                    f = 0;
                }
            }
        });
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s == 0) {
                    saturday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.orange_500));
                    saturday.setTextColor(Color.WHITE);
                    s = 1;
                } else if (s == 1) {
                    saturday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.green_50));
                    saturday.setTextColor(Color.BLACK);
                    s = 0;
                }
            }
        });
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (su == 0) {
                    sunday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.orange_500));
                    sunday.setTextColor(Color.WHITE);
                    su = 1;
                } else if (su == 1) {
                    sunday.setBackgroundColor(ContextCompat.getColor(Create_institution_form.this, R.color.green_50));
                    sunday.setTextColor(Color.BLACK);
                    su = 0;
                }
            }
        });


        insti_name = findViewById(R.id.get_inst_name);
        logo_get = findViewById(R.id.logo);
        logo_pick = findViewById(R.id.logo_picker);
        insti_address = findViewById(R.id.get_address);

        institution_mail = findViewById(R.id.get_email);
        institute_phone = findViewById(R.id.get_phone);

//fetchVenCatDescription();

 //       MapMarker(new CreateVendorMapFragment());
    }

   /* private void MapMarker(CreateVendorMapFragment mapMarkerFragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,mapMarkerFragment);
        fragmentTransaction.commit();
    }

    */

    private void createinstitute() throws JSONException {
        name = insti_name.getText().toString();
        address = insti_address.getText().toString();
        vendor_category = cat_hash;
        email = institution_mail.getText().toString();
        phone = institute_phone.getText().toString();
        prc_rng = String.valueOf(price_range);
        district = String.valueOf(dist_id);
        open = am1.getText().toString();
        close = pm1.getText().toString();
        mon = String.valueOf(m);
        tues = String.valueOf(t);
        wednes = String.valueOf(w);
        thurs = String.valueOf(th);
        fri = String.valueOf(f);
        satur = String.valueOf(s);
        sun = String.valueOf(su);
        holiday = String.valueOf(0);


        Intent intent1 = new Intent(Create_institution_form.this,Create_Institution_map.class);
        intent1.putExtra("id",id);
        intent1.putExtra("group_name",group_name);
        intent1.putExtra("name",name);
        intent1.putExtra("address",address);
        intent1.putExtra("vendor_category",vendor_category);
        intent1.putExtra("phone",phone);
        intent1.putExtra("email",email);
        intent1.putExtra("price_range",prc_rng);
        intent1.putExtra("district",district);
        intent1.putExtra("open",open);
        intent1.putExtra("close",close);
        intent1.putExtra("monday",mon);
        intent1.putExtra("tuesday",tues);
        intent1.putExtra("wednesday",wednes);
        intent1.putExtra("thursday",thurs);
        intent1.putExtra("friday",fri);
        intent1.putExtra("saturday",satur);
        intent1.putExtra("sunday",sun);
        intent1.putExtra("holiday",holiday);


        startActivity(intent1);

    }

    private void fetchDistrict()
    {
        String url = base_app_url+"api/geograph/district?city_id=7 ";
      JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
          @Override
          public void onResponse(JSONArray response)
          {
              Log.i("JsonArrayResponse",response.toString());
              for (int i = 0;i<response.length();i++)
              {
                  try {
                      JSONObject jsonObject1  = response.getJSONObject(i);
                      int id = jsonObject1.getInt("id");
                      String name = jsonObject1.getString("name");
                      mylist.add(name);
                      plist.add(id);

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
      });
        requestQueue.add(jsonObjectRequest);
    }

    //time picker
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

}