package com.ezyedu.vendor.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.ezyedu.vendor.adapter.Approved_Event_Adapter;
import com.ezyedu.vendor.model.Approved_events;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Pending_Event_Fragment extends Fragment {


    RecyclerView recyclerView;
    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String session_id = null;

    private List<Approved_events> approved_eventsList = new ArrayList<>();
    Approved_Event_Adapter approved_event_adapter;

    ImageView imageView;
    TextView t1;

    GridLayoutManager manager;

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
        View view =  inflater.inflate(R.layout.fragment_pending__event_, container, false);

        requestQueue= Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.approved_event_recycler);

        t1 = view.findViewById(R.id.t11);
        imageView = view.findViewById(R.id.i11);


        approved_event_adapter = new Approved_Event_Adapter(getContext(),approved_eventsList);
        manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(approved_event_adapter);
        recyclerView.setHasFixedSize(true);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        fetchData();

        return view;




    }

    private void fetchData()
    {
        String url = base_app_url+"api/event?status=0&expired=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ResponseEventsPending",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length() >0)
                    {

                        for (int i = 0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String tittle = jsonObject.getString("title");
                            String start_at = jsonObject.getString("start_at");
                            String finish_at = jsonObject.getString("finish_at");
                            int status = jsonObject.getInt("status");
                            String hash_id = jsonObject.getString("hash_id");
                            JSONArray jsonArray1 = jsonObject.getJSONArray("event_images");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            String image = jsonObject1.getString("image");
                            Approved_events post = new Approved_events(tittle,image,start_at,finish_at);
                            approved_eventsList.add(post);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }

                    }
                    else
                    {
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