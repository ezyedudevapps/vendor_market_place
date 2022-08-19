package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Bank_Account_Activity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    TextView textView,account_num,account_name,bank_name,descp_bank;
    Button editBank,add_bank;
    ImageView imageView;
    String session_id = null;
    RequestQueue requestQueue;
    String ven_id;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank__account_);


        drawerLayout = findViewById(R.id.drawer_layout);

        textView = findViewById(R.id.tittle_page);

        textView.setText("Bank Details");

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);






         imageView = findViewById(R.id.i11);
         descp_bank = findViewById(R.id.t11);
        add_bank = findViewById(R.id.add_bnk);
        editBank = findViewById(R.id.edit_bnk);

        add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Bank_Account_Activity.this,Add_bank_account.class);
                startActivity(intent1);
            }
        });

        account_num = findViewById(R.id.acc_number);
        account_name = findViewById(R.id.acc_name);
        bank_name = findViewById(R.id.acc_bank_name);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        fetch_Vendorid();
    }

    private void fetch_Vendorid()
    {
        String url = base_app_url+"api/vendor/info";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseVendorIdBank",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject1 = response.getJSONObject("data");
                        int v  = jsonObject1.getInt("vendor_id");
                        ven_id = String.valueOf(v);
                        Log.i("ven_id",ven_id);
                        if (ven_id != null)
                        {
                            fetchData(ven_id);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("venBankErr", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Bank_Account_Activity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
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

    private void fetchData(String ven_id)
    {
        String url = base_app_url+"api/vendor/"+ven_id+"/bank";
        Log.i("urlCheck",url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseBank",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))

                    {
                        account_name.setVisibility(View.VISIBLE);
                        account_num.setVisibility(View.VISIBLE);
                        bank_name.setVisibility(View.VISIBLE);
                        editBank.setVisibility(View.VISIBLE);
                        JSONObject jsonObject1 = response.getJSONObject("data");
                        int bank_id = jsonObject1.getInt("id");
                        int acc_no = jsonObject1.getInt("acc_no");
                        String acc_number = String.valueOf(acc_no);
                        account_num.setText("Account Number : "+String.valueOf(acc_no));
                        String holder_name = jsonObject1.getString("holder_name");
                        account_name.setText("Account Holder Name : "+jsonObject1.getString("holder_name"));
                        Log.i("responseAcc",jsonObject1.getString("acc_no"));
                       String Bank_names = jsonObject1.getString("bank_name");
                        bank_name.setText("Bank Name : "+jsonObject1.getString("bank_name"));


                        editBank.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(Bank_Account_Activity.this,Edit_bank_details.class);
                                intent1.putExtra("Bank_id",bank_id);
                                intent1.putExtra("acc_no",acc_number);
                                intent1.putExtra("holder_name",holder_name);
                                intent1.putExtra("bank_name",Bank_names);
                                startActivity(intent1);

                            }
                        });
                    }
                    else if (message.equals("Bank details not found"))
                    {
                        imageView.setVisibility(View.VISIBLE);
                        descp_bank.setVisibility(View.VISIBLE);
                        add_bank.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("errorBank",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("BankDtlErr", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Bank_Account_Activity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
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

    public void ClickMenu(View view)
    {
        MainActivity.openDrawer(drawerLayout);
    }
    public void Clickhome(View view)
    {
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public void Clicmessages(View view)
    {
        MainActivity.redirectActivity(this,MessageActivity.class);
    }

    public void Clickfeeds(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void ClickEvents(View view) {
        // redirectActivity(this,);
        MainActivity.redirectActivity(this, Events_Activity.class);
    }
        public void Clickpayment(View view)
    {
        MainActivity.redirectActivity(this,Payment_activity.class);
        //  redirectActivity(this,);
    }
    public void Clickusers(View view)
    {
        //  redirectActivity(this,);
        MainActivity.redirectActivity(this,Courses_Activity.class);
    }
    public void Clickreviews(View view)
    {
        MainActivity.redirectActivity(this,Reviews_Page.class);
    }
    public void Clickanalytics(View view)
    {
        MainActivity.redirectActivity(this,Analytics_Activity.class);
        // redirectActivity(this,);
    }
    public void Clicksales(View view)
    {
        MainActivity.redirectActivity(this,Sales_activity.class);
    //    Toast.makeText(this, "Sales", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickinstitute(View view)
    {
        MainActivity.redirectActivity(this,Edit_profile_activity.class);
    }
    public void Clicksettings(View view)
    {
        MainActivity.redirectActivity(this,Settings_Activity.class);

    }
    public void Clickproducts(View view)
    {
        MainActivity.redirectActivity(this,Connection_Actiivity.class);

    }
    public void Clickfaq(View view)
    {
        Toast.makeText(this, "Faq", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Intent intent1 = new Intent(this,MainActivity.class);
            startActivity(intent1);
        }
    }








}