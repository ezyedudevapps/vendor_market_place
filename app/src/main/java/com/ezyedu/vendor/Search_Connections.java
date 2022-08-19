package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.ApprovedConnectionAdapter;
import com.ezyedu.vendor.adapter.SearchConnectionAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.SearchConnectionList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search_Connections extends AppCompatActivity {

    String session_id = null;
    RequestQueue requestQueue;

    RecyclerView recyclerView;
    EditText editText;

    SearchConnectionAdapter searchConnectionAdapter;
    private List<SearchConnectionList> searchConnectionLists = new ArrayList<>();

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__connections);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");

        recyclerView = findViewById(R.id.list_connection_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        searchConnectionAdapter = new SearchConnectionAdapter(Search_Connections.this,searchConnectionLists);
        recyclerView.setAdapter(searchConnectionAdapter);
        fetchUser();

        editText = findViewById(R.id.search_txt);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        ImageView imageView = findViewById(R.id.img_srch);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           imageView.setVisibility(View.GONE);
            }
        });

    }

    private void filter(String text)
    {
        ArrayList<SearchConnectionList> filteredlist = new ArrayList<>();

        for (SearchConnectionList item : searchConnectionLists)
        {
            if (item.getUsername().toLowerCase().contains(text.toLowerCase()))
            {
                filteredlist.add(item);
            }
        }
        searchConnectionAdapter.filterList(filteredlist);
    }
    private void fetchUser()
    {
        String url = "https://dev-api.ezy-edu.com/api/user";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()>0)
                    {
                        for (int i = 0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String hash_id = jsonObject.getString("hash_id");
                            String name = jsonObject.getString("name");
                            String image;
                            if (jsonObject.isNull("image"))
                            {
                                image = "null";
                            }
                            else
                            {
                                image = jsonObject.getString("image");
                            }
                            String username = jsonObject.getString("username");
                            SearchConnectionList post = new SearchConnectionList(hash_id,name,username,image);
                            searchConnectionLists.add(post);
                            recyclerView.getAdapter().notifyDataSetChanged();
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