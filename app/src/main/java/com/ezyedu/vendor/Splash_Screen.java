package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash_Screen extends AppCompatActivity {

    String session_id = null;

    Globals sharedData = Globals.getInstance();


    ImageGlobals shareData1 = ImageGlobals.getInstance();


    //domain url
    String dev_url = "https://dev-api.ezy-edu.com/";
    String production_url = "https://prod-api.ezy-edu.com/";

    //image url
    String dev_image = "https://dpzt0fozg75zu.cloudfront.net/";
    String prod_image = "https://d2ozgbltrhzzw8.cloudfront.net/";



    RequestQueue requestQueue;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        //set domain url
        sharedData.setValue(production_url);

        //set img url
        shareData1.SetValue(prod_image);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(session_id)) {
                    Intent intent = new Intent(Splash_Screen.this, Login_SignUp_page.class);
                    startActivity(intent);
                }
                else
                {
                    progressDialog = new ProgressDialog(Splash_Screen.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    checkVendor();
                  //  Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                    //startActivity(intent);
                }

             //  Intent intent = new Intent(Splash_Screen.this, Login_SignUp_page.class);
               // startActivity(intent);
            }
        },500);
    }

    private void checkVendor()
    {
        String url = production_url+"api/vendor/info";;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        if (jsonObject.isNull("vendor_id"))
                        {
                         Intent intent1 = new Intent(Splash_Screen.this,Login_page.class);
                         startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(Splash_Screen.this,MainActivity.class);
                            startActivity(intent1);
                        }
                    }
                    else
                    {
                        Intent intent1 = new Intent(Splash_Screen.this,Login_page.class);
                        startActivity(intent1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("ErrorHome",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("MainFailErr", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Splash_Screen.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}