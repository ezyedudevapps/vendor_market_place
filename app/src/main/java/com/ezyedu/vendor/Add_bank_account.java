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

public class Add_bank_account extends AppCompatActivity {
    String session_id = null;
    RequestQueue requestQueue;

    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<Integer> plist = new ArrayList<Integer>();
    TextView categories;
    Dialog dialog;
    Integer bank_id;
    EditText ac_name,ac_num;
    Button add_account;
    String num;
    String name;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_account);

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

        fetchBankNames();
        categories = findViewById(R.id.txt_categories);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Add_bank_account.this);
                dialog.setContentView(R.layout.dialog_all_bank_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_bank_account.this,android.R.layout.simple_list_item_1,mylist);
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
                        int pos = position;
                        Log.i("catPos", String.valueOf(pos));
                        dialog.dismiss();
                        bank_id = plist.get(position);
                        Log.i("Hash_id_val", String.valueOf(bank_id));
                    }
                });
            }
        });

        ac_name = findViewById(R.id.tittle_get);
        ac_num = findViewById(R.id.acc_num_get);
        add_account = findViewById(R.id.add_bnk_account);
        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = ac_num.getText().toString();
                name = ac_name.getText().toString();
                try {
                    if (!TextUtils.isEmpty(num) && !TextUtils.isEmpty(name) && bank_id != null)
                    {
                        add_Account();
                    }
                   else
                    {
                        Toast.makeText(Add_bank_account.this, "Please Enter All the details", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void add_Account() throws JSONException {
        String url = base_app_url+"api/vendor/bank";

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("bank_id",bank_id);
        jsonObject1.put("acc_no",num);
        jsonObject1.put("holder_name",name);
        Log.i("jsonRequestBank",jsonObject1.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Toast.makeText(Add_bank_account.this, response.toString(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Add_bank_account.this,Bank_Account_Activity.class);
                startActivity(intent1);

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

    private void fetchBankNames()
    {
        String url = base_app_url+"api/bank?search=&sort[created_at]=asc";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ResponseAllBank",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        String name = jsonObject1.getString("name");
                        String bankId = String.valueOf(id);
                        mylist.add(name);
                        plist.add(id);
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