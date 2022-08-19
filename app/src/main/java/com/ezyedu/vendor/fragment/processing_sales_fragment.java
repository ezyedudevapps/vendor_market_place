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
import com.ezyedu.vendor.adapter.PendingOrderAdapter;
import com.ezyedu.vendor.adapter.ProcessingOrderAdapter;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.pending_orders;
import com.ezyedu.vendor.model.processing_orders;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class processing_sales_fragment extends Fragment {
    RecyclerView recyclerView;
    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String session_id = null;
    ImageView imageView;
    TextView t1;
    ProcessingOrderAdapter processingOrderAdapter;
    List<processing_orders> processingOrdersList = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_processing_sales_fragment, container, false);

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

        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout);
        shimmerFrameLayout.startShimmerAnimation();

        processingOrderAdapter = new ProcessingOrderAdapter(getContext(),processingOrdersList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(processingOrderAdapter);
        recyclerView.setHasFixedSize(true);
        fetchOrders();
        return view;
    }

    private void fetchOrders()
    {
        String url = base_app_url+"api/vendor/sales";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponsePendingOrders",response.toString());
                try {
                    String message = response.getString("message");
                    if (!message.equals("success"))
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        t1.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int order_id = jsonObject.getInt("order_id");
                        int order_status = jsonObject.getInt("order_status");
                        String date = jsonObject.getString("date");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("courses");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        String course_name = jsonObject1.getString("course_name");
                        int qty = jsonObject1.getInt("qty");
                        int courses_count = jsonObject.getInt("courses_count");

                        if (order_status == 2 || order_status ==3)
                        {
                            processing_orders post = new processing_orders(order_id,order_status,date,course_name,qty,courses_count);
                            processingOrdersList.add(post);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                    if (processingOrdersList.size()==0)
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        t1.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorPendingOrders",error.toString());
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
}