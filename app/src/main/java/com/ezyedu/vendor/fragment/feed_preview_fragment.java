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
import com.ezyedu.vendor.adapter.FeedPreviewAdapter;
import com.ezyedu.vendor.adapter.ProductPreviewAdapter;
import com.ezyedu.vendor.model.Feeds;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.ProductPreview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class feed_preview_fragment extends Fragment {

    RecyclerView recyclerView;
    TextView textView;
    ImageView imageView;
    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String session_id = null;
    List<Feeds> feedsList = new ArrayList<>();
    FeedPreviewAdapter feedPreviewAdapter;

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
        View view  =  inflater.inflate(R.layout.fragment_feed_preview_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyc_product_preview);
        textView = view.findViewById(R.id.t11);
        imageView = view.findViewById(R.id.i11);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue= Volley.newRequestQueue(getContext());
        feedPreviewAdapter = new FeedPreviewAdapter(getContext(),feedsList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(feedPreviewAdapter);
        recyclerView.setHasFixedSize(true);

        getBlogs();
        return view;
    }

    private void getBlogs()
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
                        JSONArray jsonArray = jsonObject.getJSONArray("blogs");
                        if (jsonArray.length() >0)
                        {
                            for (int i = 0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String blog_title = jsonObject1.getString("blog_title");
                                String blog_hash_id = jsonObject1.getString("blog_hash_id");
                                String published_date  =jsonObject1.getString("published_date");
                                String content = jsonObject1.getString("content");
                                String blog_image =jsonObject1.getString("blog_image");
                                Feeds post = new Feeds(blog_title,blog_hash_id,published_date,content,blog_image);
                                feedsList.add(post);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            imageView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
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
}