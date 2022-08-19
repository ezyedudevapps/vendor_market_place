package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Create_vendor_group extends AppCompatActivity {

    EditText editText;
    String group_name;
    Button create_grp;

    //category
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> plist = new ArrayList<String>();
    TextView categories;
    String category_hash_id;
    String category_label;

    //sub category
    TextView sub_categories;
    String sub_category_hash_id;
    String sub_category_label;
    ArrayList<String> ablist = new ArrayList<>();
    ArrayList<String> clist = new ArrayList<>();



    Dialog dialog;

    RequestQueue requestQueue;
    String session_id = null;
    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vendor_group);

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
        editText = findViewById(R.id.get_ven_grp_nm);
        create_grp = findViewById(R.id.create_ven);
        create_grp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_name = editText.getText().toString();
                if (TextUtils.isEmpty(group_name))
                {
                    Toast.makeText(Create_vendor_group.this, "Group Name Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(category_label))
                {
                    Toast.makeText(Create_vendor_group.this, "Please Select vendor category", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(sub_category_label))
                {
                    Toast.makeText(Create_vendor_group.this, "Please Select vendor sub-category", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(Create_vendor_group.this,Create_institution_form.class);
                    intent.putExtra("cat_label",category_label);
                    intent.putExtra("cat_hash",category_hash_id);
                    intent.putExtra("sub_cat_label",sub_category_label);
                    intent.putExtra("sub_cat_hash",sub_category_hash_id);
                    intent.putExtra("id",0);
                    intent.putExtra("group_name",group_name);
                    startActivity(intent);
                }
            }
        });

        fetchCategories();
        fetchSubCategories();
        categories = findViewById(R.id.txt_categories);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Create_vendor_group.this);
                dialog.setContentView(R.layout.daialog_all_category_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Create_vendor_group.this,android.R.layout.simple_list_item_1,mylist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        categories.setText(adapter.getItem(position));
                        category_label = adapter.getItem(position);
                        int pos = position;
                        Log.i("catPos", String.valueOf(pos));
                        dialog.dismiss();
                        category_hash_id = plist.get(position);
                        Log.i("Hash_id_val", category_label+" "+category_hash_id);
                    }
                });
            }
        });

        sub_categories = findViewById(R.id.sub_categories_list);
        sub_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Create_vendor_group.this);
                dialog.setContentView(R.layout.dialog_sub_category_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Create_vendor_group.this,android.R.layout.simple_list_item_1,ablist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        sub_categories.setText(adapter.getItem(position));
                        sub_category_label = adapter.getItem(position);
                        int pos = position;
                        Log.i("catPos", String.valueOf(pos));
                        dialog.dismiss();
                        sub_category_hash_id = clist.get(position);
                        Log.i("sub_Hash_id_val", sub_category_label+" "+sub_category_hash_id);
                    }
                });
            }
        });

    }

    private void fetchSubCategories()
    {
        String url = base_app_url+"api/vendor/subcategory?All=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("responseCategory",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String label = jsonObject.getString("label");
                        String hash_id = jsonObject.getString("hash_id");
                        ablist.add(label);
                        clist.add(hash_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchCategories()
    {
        String url = base_app_url+"api/vendor/category";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("responseCategory",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String label = jsonObject.getString("label");
                        String hash_id = jsonObject.getString("hash_id");
                        mylist.add(label);
                        plist.add(hash_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void createGroup(String group_name) throws JSONException {
        String url = base_app_url+"api/vendor";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vendor_group_name",group_name);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ResponseGroup",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Create_vendor_group.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
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