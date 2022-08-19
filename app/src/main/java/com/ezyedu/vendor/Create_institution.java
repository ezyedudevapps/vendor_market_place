package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.HomeAdapter;
import com.ezyedu.vendor.adapter.VendorCategoryAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.Vendor_category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Create_institution extends AppCompatActivity {
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    String session_id = null;
    List<Vendor_category> vendorCategoryList = new ArrayList<>();
    VendorCategoryAdapter vendorCategoryAdapter;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    EditText editText;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_institution);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        recyclerView = findViewById(R.id.ven_cat_recyc);
        recyclerView.setLayoutManager(new GridLayoutManager(Create_institution.this,2));
        recyclerView.setHasFixedSize(true);
        vendorCategoryAdapter = new VendorCategoryAdapter(Create_institution.this,vendorCategoryList);
        recyclerView.setAdapter(vendorCategoryAdapter);
        fetchData();

        button = findViewById(R.id.add_ven_cat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Create_institution.this,Create_vendor_group.class);
                startActivity(intent1);
            }
        });

        editText = findViewById(R.id.search_course_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter(s.toString());
            }
        });

    }

    private void filter(String toString)
    {
        ArrayList<Vendor_category> filteredlist = new ArrayList<>();

        for (Vendor_category item : vendorCategoryList)
        {
            if (item.getLabel().toLowerCase().contains(toString.toLowerCase()))
            {
                filteredlist.add(item);
            }
        }
        vendorCategoryAdapter.filterList(filteredlist);
    }

    private void fetchData()
    {

        String url = base_app_url+"api/vendor/group?search=&sort[created_at]=desc&category_id=";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseVendorCat",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("JsonArraySz", String.valueOf(jsonArray.length()));
                    for (int i = 0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        String label = jsonObject1.getString("group_name");
                        String hash_id = jsonObject1.getString("hash_id");
                        JSONObject jsonObject11 = jsonObject1.getJSONObject("vendor_category");
                        String image = jsonObject11.getString("icon");
                        String cat_hash = jsonObject11.getString("hash_id");
                        String cat_label = jsonObject11.getString("label");
                        if (!jsonObject1.isNull("vendor_subcategory"))
                        {
                            JSONObject jsonObject12 = jsonObject1.getJSONObject("vendor_subcategory");
                            String sub_cat_label = jsonObject12.getString("label");
                            String sub_cat_hash = jsonObject12.getString("hash_id");
                            Vendor_category post = new Vendor_category(label,image,hash_id,cat_hash,cat_label,sub_cat_label,sub_cat_hash,id);
                            vendorCategoryList.add(post);
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("CatchErr",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}