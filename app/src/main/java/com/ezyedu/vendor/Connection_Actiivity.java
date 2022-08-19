package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.ApprovedConnectionAdapter;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.adapter.My_request_adapter;
import com.ezyedu.vendor.adapter.PendingOrderAdapter;
import com.ezyedu.vendor.adapter.PendingRequestAdapter;
import com.ezyedu.vendor.model.ApprovedConnections;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.MyRequest;
import com.ezyedu.vendor.model.PendingRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connection_Actiivity extends AppCompatActivity
{

    DrawerLayout drawerLayout;
    TextView textView,t1,myrequest;
    ImageView imageView;

    String session_id = null;
    RequestQueue requestQueue;

    RelativeLayout search_relayive;
    RecyclerView recyclerView,myrequestRecycler,pendingRequestRecycler;

    ApprovedConnectionAdapter approvedConnectionAdapter;
    private List<ApprovedConnections> approvedConnectionsList = new ArrayList<>();

    My_request_adapter my_request_adapter;
    private List<MyRequest> myRequestList = new ArrayList<>();

    PendingRequestAdapter pendingRequestAdapter;
    private List<PendingRequests> pendingRequestsList = new ArrayList<>();

    TextView textView1,textView_myconnection;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection__actiivity);

        drawerLayout = findViewById(R.id.drawer_layout);

        textView = findViewById(R.id.tittle_page);

        textView.setText("My Connections");


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        //chatlist
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cl_activity",session_id);
        imageView = findViewById(R.id.i11);
        t1=findViewById(R.id.t11);

        textView_myconnection = findViewById(R.id.my_conn_txt);
        recyclerView = findViewById(R.id.my_connection_recyc);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        approvedConnectionAdapter = new ApprovedConnectionAdapter(Connection_Actiivity.this,approvedConnectionsList);
        recyclerView.setAdapter(approvedConnectionAdapter);
        fetchConnections();

        myrequest = findViewById(R.id.my_request_txt);
        myrequestRecycler = findViewById(R.id.my_request_recyc);
        LinearLayoutManager manager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        myrequestRecycler.setLayoutManager(manager1);
        myrequestRecycler.setHasFixedSize(true);
        my_request_adapter = new My_request_adapter(Connection_Actiivity.this,myRequestList);
        myrequestRecycler.setAdapter(my_request_adapter);
        fetchMyRequests();

        textView1 = findViewById(R.id.pend_req_txt);
        pendingRequestRecycler = findViewById(R.id.pending_recyc);
        LinearLayoutManager manager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        pendingRequestRecycler.setLayoutManager(manager2);
        pendingRequestRecycler.setHasFixedSize(true);
        pendingRequestAdapter = new PendingRequestAdapter(Connection_Actiivity.this,pendingRequestsList);
        pendingRequestRecycler.setAdapter(pendingRequestAdapter);
        fetchPendingRequests();

        EditText editText = findViewById(R.id.search_txt);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Connection_Actiivity.this,Search_Connections.class);
                startActivity(intent1);
            }
        });

        search_relayive = findViewById(R.id.search_connection);
        search_relayive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Connection_Actiivity.this,Search_Connections.class);
                startActivity(intent1);
            }
        });
    }

    private void fetchPendingRequests()
    {
        String url = "https://dev-api.ezy-edu.com//api/user/connection-vendor?status=pending&search=";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("RequestedConnection",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()>0)
                    {
                        textView1.setVisibility(View.VISIBLE);
                        pendingRequestRecycler.setVisibility(View.VISIBLE);
                        for (int i = 0; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String image;
                            if (!jsonObject.isNull("image"))
                            {
                                image = jsonObject.getString("image");
                            }
                            else
                            {
                                image = "null";
                            }
                            String username = jsonObject.getString("username");

                            PendingRequests post = new PendingRequests(name,image,username);
                            pendingRequestsList.add(post);
                            pendingRequestRecycler.getAdapter().notifyDataSetChanged();
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    private void fetchMyRequests()
    {
        String url = "https://dev-api.ezy-edu.com/api/user/connection-vendor?status=sended&search=";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("RequestedConnection",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()>0)
                    {
                        myrequest.setVisibility(View.VISIBLE);
                        myrequestRecycler.setVisibility(View.VISIBLE);
                        for (int i = 0; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String image;
                            if (!jsonObject.isNull("image"))
                            {
                                image = jsonObject.getString("image");
                            }
                            else
                            {
                                image = "null";
                            }
                            String username = jsonObject.getString("username");


                            MyRequest post = new MyRequest(name,image,username);
                            myRequestList.add(post);
                            myrequestRecycler.getAdapter().notifyDataSetChanged();
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
       requestQueue.add(jsonObjectRequest);
    }

    private void fetchConnections()
    {
        String url = "https://dev-api.ezy-edu.com/api/user/connection-vendor?status=approved&search=";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("approvedConnection",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()>0)
                    {
                        textView_myconnection.setVisibility(View.VISIBLE);
                        for (int i = 0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String image = jsonObject.getString("image");
                            String username = jsonObject.getString("username");
                            ApprovedConnections post = new ApprovedConnections(name,image,username);
                            approvedConnectionsList.add(post);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        imageView.setVisibility(View.VISIBLE);
                        t1.setVisibility(View.VISIBLE);
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    //message navigation menu....
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
        //recreate();
        MainActivity.redirectActivity(this,MessageActivity.class);
    }


    public void Clickfeeds(View view)
    {
        MainActivity.redirectActivity(this,Bank_Account_Activity.class);
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
        Toast.makeText(this, "analytics", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clicksales(View view)
    {
        MainActivity.redirectActivity(this,Sales_activity.class);

    }
    public void Clickinstitute(View view)
    {
        MainActivity.redirectActivity(this,Edit_profile_activity.class);
        // redirectActivity(this,);
    }
    public void Clicksettings(View view)
    {
        MainActivity.redirectActivity(this,Settings_Activity.class);
    }
    public void Clickproducts(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
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