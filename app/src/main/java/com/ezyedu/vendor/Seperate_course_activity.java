package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.CourseAdapter;
import com.ezyedu.vendor.adapter.SeperateCourseAdapter;
import com.ezyedu.vendor.adapter.SliderAdp;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.SeperateCourse;
import com.ezyedu.vendor.model.SliderImages;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seperate_course_activity extends AppCompatActivity {
    String session_id = null;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    private String Hashid;
    List<SeperateCourse> seperateCourseList = new ArrayList<>();
    SeperateCourseAdapter seperateCourseAdapter;

    //slider
    SliderView sliderView;
    List<SliderImages> sliderImagesList = new ArrayList<>();
    SliderAdp sliderAdp;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seperate_course_activity);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        Hashid = getIntent().getStringExtra("id");
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        recyclerView = findViewById(R.id.course_seperate_recyc);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        seperateCourseAdapter = new SeperateCourseAdapter(Seperate_course_activity.this,seperateCourseList);
        recyclerView.setAdapter(seperateCourseAdapter);
        getData();

        sliderView = findViewById(R.id.slider_view);

    }

    private void getData()
    {
        String url = base_app_url+"api/courses/"+Hashid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    Log.i("ResponseSepCourse",response.toString());
                    JSONObject jsonObject1 = response.getJSONObject("data");
                    String category_label = jsonObject1.getString("category_label");
                    String course_title = jsonObject1.getString("course_title");
                    String course_description = jsonObject1.getString("course_description");
                    String course_duration = jsonObject1.getString("course_duration");
                    Double initial_price = jsonObject1.getDouble("initial_price");
                    Double discount_price = jsonObject1.getDouble("discount_price");
                    String start_date = jsonObject1.getString("start_date");
                    String course_hash_id = jsonObject1.getString("course_hash_id");

                    SeperateCourse post = new SeperateCourse(category_label,course_title,course_description,course_duration,initial_price,discount_price
                            ,start_date,course_hash_id);
                    seperateCourseList.add(post);
                    recyclerView.getAdapter().notifyDataSetChanged();

                    JSONArray jsonArray = jsonObject1.getJSONArray("courses_image");
                    for (int i = 0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String image = jsonObject.getString("image");
                        SliderImages post1 = new SliderImages(image);
                        sliderImagesList.add(post1);
                    }
                    sliderAdp = new SliderAdp(Seperate_course_activity.this,sliderImagesList);
                    sliderView.setSliderAdapter(sliderAdp);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    sliderView.startAutoCycle();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("CatchError",e.toString());
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
}