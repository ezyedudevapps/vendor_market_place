package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.AllFeedsAdapter;
import com.ezyedu.vendor.adapter.SeperateFeedsAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.SeperateFeeds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seperate_Feeds extends AppCompatActivity {

    String session_id = null;
    RecyclerView recyclerView;
    RequestQueue requestQueue;

    private String Hashid;

    ProgressDialog progressDialog;

    SeperateFeedsAdapter seperateFeedsAdapter;
    List<SeperateFeeds> seperateFeeds = new ArrayList<>();

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seperate__feeds);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        Hashid = getIntent().getStringExtra("id");
        Log.i("FeedsHash",Hashid);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        recyclerView = findViewById(R.id.sep_feed_recyc);

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        seperateFeedsAdapter = new SeperateFeedsAdapter(Seperate_Feeds.this,seperateFeeds);
        recyclerView.setAdapter(seperateFeedsAdapter);
        fetchData();
        progressDialog = new ProgressDialog(Seperate_Feeds.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }
    private void fetchData()
    {
      //  String url = "https://dev-api.ezy-edu.com/api/ideas/inl14jls";
        String url = base_app_url+"api/ideas/"+Hashid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                Log.i("JsonRespFeeds",response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("ideas_images");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String image = jsonObject.getString("image");
                    String tittle = response.getString("title");
                    String description = response.getString("description");
                    JSONObject jsonObject2 = response.getJSONObject("ideas_category");
                    String label =  jsonObject2.getString("label");
                    JSONObject jsonObject1 = response.getJSONObject("vendor");
                    String ven_logo = jsonObject1.getString("logo");
                    String name = jsonObject1.getString("name");
                    String address = jsonObject1.getString("address");

                    SeperateFeeds post = new SeperateFeeds(image,tittle,label,description,ven_logo,address,name);
                    seperateFeeds.add(post);
                    recyclerView.getAdapter().notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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