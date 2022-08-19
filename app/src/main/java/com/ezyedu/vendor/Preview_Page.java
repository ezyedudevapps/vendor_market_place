package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.adapter.Sales_fragment_Adapter;
import com.ezyedu.vendor.adapter.SliderAdapter;
import com.ezyedu.vendor.adapter.VenSliderAdp;
import com.ezyedu.vendor.adapter.preview_fragment_adapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.PreviewGalleryImages;
import com.ezyedu.vendor.model.VenSliderImages;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preview_Page extends AppCompatActivity {
    TabLayout tableLayout;
    ViewPager2 pager2;
    String session_id = null;
    RequestQueue requestQueue;



    SliderView sliderView;
    List<VenSliderImages> sliderImagesList = new ArrayList<>();
    VenSliderAdp sliderAdp;

    preview_fragment_adapter pfa;
  /*  List<PreviewGalleryImages> previewGalleryImagesList = new ArrayList<>();
    SliderAdapter sliderAdapter;

   */

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview__page);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

  /*    recyclerView = findViewById(R.id.recyc_gallery);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        sliderAdapter = new SliderAdapter(Preview_Page.this,previewGalleryImagesList);
        recyclerView.setAdapter(sliderAdapter);


   */

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_HPactivity",session_id);

        tableLayout = findViewById(R.id.tab1);
        pager2 = findViewById(R.id.view_p);
        FragmentManager fragmentManager = getSupportFragmentManager();
        pfa = new preview_fragment_adapter(fragmentManager,getLifecycle());
        pager2.setAdapter(pfa);

        sliderView = findViewById(R.id.slider_view);

        tableLayout.addTab(tableLayout.newTab().setText("Profile"));
        tableLayout.addTab(tableLayout.newTab().setText("Products"));
        tableLayout.addTab(tableLayout.newTab().setText("Feeds"));


        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });

        fetchData();
    }

    private void fetchData() {
        String url = base_app_url+"api/vendor/info";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    Log.i("ResponseSS",response.toString());
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        String inst_name = jsonObject.getString("inst_name");
                        String logo = jsonObject.getString("logo");
                        int rating = jsonObject.getInt("rating");
                        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                        JSONArray jsonArray = jsonObject.getJSONArray("vendor_images");
                        if (jsonArray.length() > 0)
                        {
                            for (int i = 0; i <jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String image = jsonObject1.getString("image");
                                VenSliderImages post1 = new VenSliderImages(image,logo,rating,inst_name);
                                sliderImagesList.add(post1);
                              /*  PreviewGalleryImages post = new PreviewGalleryImages(image);
                                previewGalleryImagesList.add(post);
                                recyclerView.getAdapter().notifyDataSetChanged();

                               */
                            }
                            sliderAdp = new VenSliderAdp(Preview_Page.this,sliderImagesList);
                            sliderView.setSliderAdapter(sliderAdp);
                            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                            sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                            sliderView.startAutoCycle();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ResponseSS",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ResponseSS",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}