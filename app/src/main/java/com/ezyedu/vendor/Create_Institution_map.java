package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.fragment.NearMeMapFragment;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Create_Institution_map extends AppCompatActivity {

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    double lat;
    double lon;

    String Latitude;
    String Longitude;

    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> contentList = new ArrayList<>();

   String name,group_name,address,vendor_category,email,phone,prc_rng,district,open,close,mon,tues,wednes,thurs,fri,satur,sun,holiday;
   int id;
   RequestQueue requestQueue;

   String session_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__institution_map);

        //get domain url
        base_app_url = sharedData.getValue();
        //  Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        //Log.i("img_url_global",img_url_base);

        id = getIntent().getIntExtra("id",0);
        group_name = getIntent().getStringExtra("group_name");
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        vendor_category = getIntent().getStringExtra("vendor_category");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        prc_rng = getIntent().getStringExtra("price_range");
        district = getIntent().getStringExtra("district");
        open = getIntent().getStringExtra("open");
        close = getIntent().getStringExtra("close");
        mon = getIntent().getStringExtra("monday");
        tues = getIntent().getStringExtra("tuesday");
        wednes = getIntent().getStringExtra("wednesday");
        thurs = getIntent().getStringExtra("thursday");
        fri = getIntent().getStringExtra("friday");
        satur = getIntent().getStringExtra("saturday");
        sun = getIntent().getStringExtra("sunday");
        holiday = getIntent().getStringExtra("holiday");

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        if (id != 0)
        {
            fetchVenCatDescription();
        }

        Log.i("create_ins_values",name+address+vendor_category+email+phone+prc_rng+district+open+close+mon+tues+wednes+thurs+fri+satur+sun+holiday);
        //open marker fragment
        MapMarker(new NearMeMapFragment());
    }


    private void fetchVenCatDescription()
    {
        //get custom description
        String url = base_app_url+"api/vendor/group?search=&sort[created_at]=desc&category_id=";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    Log.i("ResponseVenDesc",response.toString());
                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int idgrp = jsonObject1.getInt("id");
                        if (id == idgrp) {
                            Log.i("idGrp", String.valueOf(idgrp));
                            JSONObject jsonObject11 = jsonObject1.getJSONObject("vendor_category");
                            JSONArray jsonArray1 = jsonObject11.getJSONArray("vendor_category_description");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject obj = jsonArray1.getJSONObject(j);
                                int id = obj.getInt("id");
                                String title = obj.getString("title");
                                String description = obj.getString("description");

                                idList.add(id);
                                contentList.add(description);
                                //add custom description to json array to create institution
                             /*   JSONObject object = new JSONObject();
                                object.put("id", id);
                                object.put("content", description);
                                Array.put(object);
                              */
                            }
                            Log.i("CustDescSize", String.valueOf(idList.size()));
                            Log.i("CustDescSize", String.valueOf(contentList.size()));

                            // adding the json array to main json object to create institution....
                            //   Log.i("JsonArrayCustom", String.valueOf(Array));
                            // jsonObject.put("custom_descriptions", Array);
                            //Log.i("JSONOBJectCreate", jsonObject.toString());
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
        });

        requestQueue.add(jsonObjectRequest);

    }
    public void f1(double lat, double lon) throws JSONException {
        Log.i("CurrentValll",String.valueOf(lat)+" "+String.valueOf(lon));
        Latitude = String.valueOf(lat);
        Longitude = String.valueOf(lon);
        
        createInstitution();
    }

    private void createInstitution() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (!group_name.equals("null"))
        {
            jsonObject.put("vendor_group_name",group_name);
        }
        jsonObject.put("name",name);
        jsonObject.put("address",address);
        jsonObject.put("latitude",Latitude);
        jsonObject.put("longitude",Longitude);
        jsonObject.put("vendor_category",vendor_category);
        jsonObject.put("phone",phone);
        jsonObject.put("email",email);
        jsonObject.put("price_range",prc_rng);
        jsonObject.put("district",district);
        jsonObject.put("open",open);
        jsonObject.put("close",close);
        jsonObject.put("monday",mon);
        jsonObject.put("tuesday",tues);
        jsonObject.put("wednesday",wednes);
        jsonObject.put("thursday",thurs);
        jsonObject.put("friday",fri);
        jsonObject.put("saturday",satur);
        jsonObject.put("sunday",sun);
        jsonObject.put("holiday",holiday);

        if (idList.size() != 0)
        {
            JSONArray Array = new JSONArray();
            for (int i = 0;i<idList.size();i++)
            {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("vendor_category_description_id",idList.get(i));
                jsonObject1.put("content",contentList.get(i));
                Array.put(jsonObject1);
            }
            jsonObject.put("custom_descriptions",Array);
            Log.i("JsonArrayCustom", String.valueOf(Array));
            Log.i("JSONOBJectCreate", jsonObject.toString());
        }

        Log.i("addressVendor",address);

        String url1 = base_app_url+"api/vendor";

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, url1, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    Log.i("ResponseCreateVendor",response.toString());
                   // progressDialog.dismiss();
                    if (!response.isNull("id"))
                    {
                        Toast.makeText(Create_Institution_map.this, "Vendor Account created Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(Create_Institution_map.this,MainActivity.class);
                        startActivity(intent1);
                    }
                    else
                    {
                        Toast.makeText(Create_Institution_map.this, "Failed Try Again", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Log.i("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
              //  progressDialog.dismiss();
                Log.i("ResponseError",error.toString());
                //     Toast.makeText(Create_institution_form.this, "Something went Wrong, Try Again", Toast.LENGTH_SHORT).show();

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Create_Institution_map.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
        requestQueue.add(jsonObjectRequest1);

    }

    private void MapMarker(NearMeMapFragment nearMeMapFragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,nearMeMapFragment);
        fragmentTransaction.commit();
    }


}