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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.Feature_analytics_adapter;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Feature_Analytics;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analytics_Activity extends AppCompatActivity {

    String session_id = null;
    RequestQueue requestQueue;

    DrawerLayout drawerLayout;
    TextView textView;
    RecyclerView recyclerView;
    Feature_analytics_adapter feature_analytics_adapter;
    private List<Feature_Analytics> feature_analytics = new ArrayList<>();

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_);

        drawerLayout = findViewById(R.id.drawer_layout);

        textView = findViewById(R.id.tittle_page);

        textView.setText("Analytics");

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);


        recyclerView = findViewById(R.id.feature_analytics_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        feature_analytics_adapter = new Feature_analytics_adapter(Analytics_Activity.this,feature_analytics);
        recyclerView.setAdapter(feature_analytics_adapter);
        fetchData();
    }

    private void fetchData()
    {
        String url = base_app_url+"api/vendor/analytics/feature";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ResponseAnalytics",response.toString());

                try {
                    int ideas = response.getInt("ideas");
                    int event = response.getInt("event");
                    int banner = response.getInt("banner");
                    int message = response.getInt("message");
                    int total_view = response.getInt("total_view");
                    int compare = response.getInt("compare");
                    int ideas_save = response.getInt("ideas_save");
                    int vendor_save = response.getInt("vendor_save");

                    Feature_Analytics post = new Feature_Analytics(ideas,event,banner,message,total_view,compare,ideas_save,vendor_save);
                    feature_analytics.add(post);
                    recyclerView.getAdapter().notifyDataSetChanged();

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
    public void ClickEvents(View view) {
        // redirectActivity(this,);
        MainActivity.redirectActivity(this, Events_Activity.class);
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
        MainActivity.redirectActivity(this,Courses_Activity.class);
    }
    public void Clickreviews(View view)
    {
        MainActivity.redirectActivity(this,Reviews_Page.class);
        // redirectActivity(this,);
    }
    public void Clickanalytics(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        //recreate();
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
        MainActivity.redirectActivity(this,Connection_Actiivity.class);
    }
    public void Clickfaq(View view)
    {
        Toast.makeText(this, "Faq", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void ClickFeeds(View view)
    {
       MainActivity. redirectActivity(this,My_Feeds_Activity.class);
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