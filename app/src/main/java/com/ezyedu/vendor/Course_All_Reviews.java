package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.Course_all_review_adapter;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.model.CourseAllReview;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course_All_Reviews extends AppCompatActivity {
    String hash_id;
    String session_id = null;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    TextView textView,t1;
    ImageView imageView;
    Course_all_review_adapter course_all_review_adapter;
    private List<CourseAllReview> courseAllReviewList = new ArrayList<>();

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__all__reviews);

        hash_id = getIntent().getStringExtra("id");
        Log.i("Hash_course",hash_id);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        recyclerView = findViewById(R.id.recyc_course_all_review);
        textView = findViewById(R.id.ttl_reviews_txt);

        t1 = findViewById(R.id.t11);
        imageView = findViewById(R.id.i11);

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        course_all_review_adapter = new Course_all_review_adapter(Course_All_Reviews.this,courseAllReviewList);
        recyclerView.setAdapter(course_all_review_adapter);
        fetchData();
    }

    private void fetchData() {
        String url = base_app_url+"api/user/course-review/" + hash_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseAllReview", String.valueOf(response));
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("user_reviews");
                        if (jsonArray.length()>0)
                        {
                            int a = jsonArray.length();
                            textView.setText("Total Reviews : "+ a);
                            for (int i = 0; i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String username = jsonObject.getString("username");
                                String user_image = jsonObject.getString("user_image");
                                int user_rating = jsonObject.getInt("user_rating");
                                String user_description = jsonObject.getString("user_description");
                                CourseAllReview post = new CourseAllReview(username,user_image,user_rating,user_description);
                                courseAllReviewList.add(post);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            t1.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                          //  Toast.makeText(Course_All_Reviews.this, "No Reviews Yet....", Toast.LENGTH_SHORT).show();
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

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}