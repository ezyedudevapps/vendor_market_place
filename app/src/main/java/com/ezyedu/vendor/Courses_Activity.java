package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.ezyedu.vendor.adapter.CourseAdapter;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Courses;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.ProductPreview;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Courses_Activity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView textView,ttl_course;
    String session_id = null;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    Button add_courseBtn;
    CourseAdapter courseAdapter;
    List<Courses> coursesList = new ArrayList<>();
    ShimmerFrameLayout shimmerFrameLayout;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_);

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
        courseAdapter = new CourseAdapter(Courses_Activity.this,coursesList);
        recyclerView.setAdapter(courseAdapter);

        add_courseBtn = findViewById(R.id.add_prod);

        add_courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Courses_Activity.this,Add_Course_Activity.class);
                startActivity(intent1);
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        textView = findViewById(R.id.tittle_page);

        textView.setText("My Products");

        fetchData();

    }

    private void fetchData()
    {
        String url = base_app_url+"api/vendor/info";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseCourse",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("courses");
                        if (jsonArray.length() >0)
                        {
                            for (int i = 0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String course_title = jsonObject1.getString("course_title");
                                String course_hash_id = jsonObject1.getString("course_hash_id");
                                String course_duration = jsonObject1.getString("course_duration");
                                String start_date  =jsonObject1.getString("start_date");
                                String institution =jsonObject1.getString("institution");
                                String courses_image =jsonObject1.getString("courses_image");
                                String category_label =jsonObject1.getString("category_label");
                                Courses post = new Courses(course_title,start_date,courses_image,course_hash_id);
                                coursesList.add(post);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                        int size = jsonArray.length();
                        ttl_course.setText("Total Products : "+ String.valueOf(size));
                    }
                    else
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ciurseMissVal",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
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
        //  redirectActivity(this,);
        drawerLayout.closeDrawer(GravityCompat.START);
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

    public void ClickFeeds(View view)
    {
        MainActivity.redirectActivity(this,My_Feeds_Activity.class);
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