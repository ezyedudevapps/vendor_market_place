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
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.EditBottomSheetDialog;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.home;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements EditBottomSheetDialog.EditSheetListner {

    DrawerLayout drawerLayout;
    TextView textView, heading,page_preview;
    ImageView logo,settings;
    String session_id = null;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    private List<home> homeList = new ArrayList<>();
    HomeAdapter homeAdapter;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get domain url
        base_app_url = sharedData.getValue();
       // Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        //Log.i("img_url_global",img_url_base);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        recyclerView = findViewById(R.id.recyc_home);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        homeAdapter = new HomeAdapter(MainActivity.this,homeList);
        recyclerView.setAdapter(homeAdapter);
        fetchData(session_id);


        drawerLayout = findViewById(R.id.drawer_layout);
        textView = findViewById(R.id.tittle_page);

        textView.setText("Home");

        heading = findViewById(R.id.tittle_vendor);
        page_preview = findViewById(R.id.preview_vendor);
        logo = findViewById(R.id.img_logo);
        settings = findViewById(R.id.settings_vendor);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBottomSheetDialog bottomSheetDialog = new EditBottomSheetDialog();
                bottomSheetDialog.show(getSupportFragmentManager(),"EditBottomSheet");

            }
        });

        page_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,Preview_Page.class);
                startActivity(intent1);
            }
        });
    }

    private void fetchData(String session_id)
    {
        String url = base_app_url+"api/vendor/home";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseHome",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject = response.getJSONObject("vendor");
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        heading.setText(name);
                        String image = null;
                        if (!jsonObject.isNull("logo"))
                        {
                            image = jsonObject.getString("logo");
                            String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                            Glide.with(MainActivity.this).load(img_url_base+image).into(logo);
                        }
                        else
                        {
                            Glide.with(MainActivity.this)
                                    .load(getResources()
                                            .getIdentifier("gscl", "drawable", MainActivity.this.getPackageName()))
                                            .into(logo);
                        }

                     //   when image null

                        Double rating = jsonObject.getDouble("rating");
                        int rating_count = jsonObject.getInt("rating_count");

                        JSONObject jsonObject1 = response.getJSONObject("orders");
                        int pending = jsonObject1.getInt("pending");
                        int processed = jsonObject1.getInt("processed");
                        int completed = jsonObject1.getInt("completed");

                        int unread_chat = response.getInt("unread_chat");
                        int new_review = response.getInt("new_review");
                        int prending_users = response.getInt("pending_users");
                        int ideas = response.getInt("ideas");
                        int events = response.getInt("events");





                        home post = new home(id,name,image,rating,rating_count,pending,processed,completed,unread_chat,new_review,prending_users,ideas,events);
                        homeList.add(post);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Something Went Wrong, Try Again Later...", Toast.LENGTH_SHORT).show();
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("catchErr",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("ErrorHome",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("MainFailErr", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(MainActivity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
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
        //open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout)
    {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void clicklogo(View view)
    {
        //close drawer
        //later put vendor logo
        closeDrawer(drawerLayout);
    }



    public static void closeDrawer(DrawerLayout drawerLayout)
    {
        //close drawer layout
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void Clickhome(View view)
    {
       // recreate();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void ClickEvents(View view)
    {
        // redirectActivity(this,);
        redirectActivity(this,Events_Activity.class);
    }
    public void Clicmessages(View view)
    {
       // Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
        redirectActivity(this,MessageActivity.class);
    }

public void Clickfeeds(View view)
{
    redirectActivity(this,Bank_Account_Activity.class);
}

    public void Clickpayment(View view)
    {
        redirectActivity(this,Payment_activity.class);
      //  redirectActivity(this,);
    }
    public void Clickusers(View view)
    {
      //  redirectActivity(this,);
        redirectActivity(this,Courses_Activity.class);
      //  Toast.makeText(this, "Users", Toast.LENGTH_SHORT).show();
    }
    public void Clickreviews(View view)
    {
        redirectActivity(this,Reviews_Page.class);
       // redirectActivity(this,);
    }
    public void Clickanalytics(View view)
    {
        redirectActivity(this,Analytics_Activity.class);
    }
    public void Clicksales(View view)
    {
        redirectActivity(this,Sales_activity.class);
      //  Toast.makeText(this, "Sales", Toast.LENGTH_SHORT).show();
       // redirectActivity(this,);
    }
    public void Clickinstitute(View view)
    {
       // Toast.makeText(this, "Institute", Toast.LENGTH_SHORT).show();
        redirectActivity(this,Edit_profile_activity.class);
       // redirectActivity(this,);
    }
    public void Clicksettings(View view)
    {
        redirectActivity(this,Settings_Activity.class);
       // redirectActivity(this,);
    }
    public void ClickFeeds(View view)
    {
        redirectActivity(this,My_Feeds_Activity.class);
    }
    public void Clickproducts(View view)
    {
        redirectActivity(this, Connection_Actiivity.class);
       // redirectActivity(this,);
    }
    public void Clickfaq(View view)
    {
        Toast.makeText(this, "Faq", Toast.LENGTH_SHORT).show();

      //  redirectActivity(this, Create_Institution_map.class);
       // redirectActivity(this,);
    }

    public static void redirectActivity(Activity activity,Class aclass)
    {
        Intent intent1 = new Intent(activity,aclass);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            //super.onBackPressed();
            finishAffinity();
            finish();
        }
    }


    //sheet -> home adapter
    public void sheet()
    {
        EditBottomSheetDialog bottomSheetDialog = new EditBottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(),"MiddleSheet");
    }

    @Override
    public void onButtonClicked(String text) {
        if (text.equals("EditMyProfile"))
        {
            Intent intent1 = new Intent(MainActivity.this,Edit_Users_profile.class);
            startActivity(intent1);
        }
        else if (text.equals("EditInstitutionProfile"))
        {
            Intent intent1 = new Intent(MainActivity.this,Edit_profile_activity.class);
            startActivity(intent1);
        }
    }
}