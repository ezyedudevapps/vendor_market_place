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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.AllFeedsAdapter;
import com.ezyedu.vendor.adapter.CourseAdapter;
import com.ezyedu.vendor.model.AllFeeds;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_Feeds_Activity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView textView,ttl_course;
    String session_id = null;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    Button add_courseBtn;
    ShimmerFrameLayout shimmerFrameLayout;

    AllFeedsAdapter allFeedsAdapter;
    List<AllFeeds> allFeedsList = new ArrayList<>();

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__feeds_);

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

        ttl_course = findViewById(R.id.course_count);

        shimmerFrameLayout = findViewById(R.id.shimmer_frame_layout);
        shimmerFrameLayout.startShimmerAnimation();

        recyclerView = findViewById(R.id.recyc_course);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        allFeedsAdapter = new AllFeedsAdapter(My_Feeds_Activity.this,allFeedsList);
        recyclerView.setAdapter(allFeedsAdapter);




        add_courseBtn = findViewById(R.id.add_prod);

        add_courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(My_Feeds_Activity.this,Add_Feeds_Activity.class);
                startActivity(intent1);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        textView = findViewById(R.id.tittle_page);
        textView.setText("My Feeds");

        fetchData();

    }
    private void fetchData()
    {
        String url = base_app_url+"api/ideas";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseFeeds",response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length() > 0)
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        for (int i = 0 ; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            int status = jsonObject.getInt("status");
                            String title = jsonObject.getString("title");
                            String description = jsonObject.getString("description");
                            String hash_id = jsonObject.getString("hash_id");
                            JSONArray jsonArray1 = jsonObject.getJSONArray("ideas_images");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            String image = jsonObject1.getString("image");
                            Log.i("image_path_feeds",image);
                            JSONObject jsonObject2 = jsonObject.getJSONObject("ideas_category");
                            String label = jsonObject2.getString("label");


                            AllFeeds post = new AllFeeds(title,image,label,hash_id,status);
                            allFeedsList.add(post);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                    int size = jsonArray.length();
                    ttl_course.setText("Total Feeds : "+ String.valueOf(size));
                } catch (JSONException e) {
                    e.printStackTrace();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
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
        //open drawer
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

    public void ClickFeeds(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void Clickreviews(View view)
    {
        MainActivity.redirectActivity(this,Reviews_Page.class);
    }
    public void Clickanalytics(View view)
    {
        MainActivity.redirectActivity(this,Analytics_Activity.class);
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