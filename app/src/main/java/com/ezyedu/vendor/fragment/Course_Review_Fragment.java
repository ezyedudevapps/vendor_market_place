package com.ezyedu.vendor.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.adapter.CourseReviewAdapter;
import com.ezyedu.vendor.adapter.PendingOrderAdapter;
import com.ezyedu.vendor.model.CourseReview;
import com.ezyedu.vendor.model.Courses;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.pending_orders;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Course_Review_Fragment extends Fragment
{
    RecyclerView recyclerView;
    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    CourseReviewAdapter courseReviewAdapter;
    List<CourseReview> courseReviews = new ArrayList<>();
    String session_id = null;
    ImageView imageView;
    TextView t1,head;
    ShimmerFrameLayout shimmerFrameLayout;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_Histry_activity",session_id);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_course__review_, container, false);
        requestQueue= Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.recyc_pending);
        t1 = view.findViewById(R.id.t11);
        imageView = view.findViewById(R.id.i11);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        head = view.findViewById(R.id.crse_ttl);
     //   shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout);
       // shimmerFrameLayout.startShimmerAnimation();

        courseReviewAdapter = new CourseReviewAdapter(getContext(),courseReviews);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(courseReviewAdapter);
        recyclerView.setHasFixedSize(true);
        fetchData();
        return view;
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
                                CourseReview post = new CourseReview(course_title,start_date,courses_image,course_hash_id);
                                courseReviews.add(post);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            head.setVisibility(View.GONE);
                            t1.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

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