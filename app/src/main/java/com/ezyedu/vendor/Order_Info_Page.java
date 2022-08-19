package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.OrderInfoAdapter;
import com.ezyedu.vendor.model.BottomSheetDialog;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.Order_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order_Info_Page extends AppCompatActivity implements com.ezyedu.vendor.model.BottomSheetDialog.BottomSheetListner {
    RequestQueue requestQueue;
    String session_id = null;
    TextView od_txt,pend_status,order_id,ven_name,date,actual_price,discount,final_price,total_items;
    ImageView imageButton;
    RecyclerView recyclerView;
    Button button;
    RelativeLayout relativeLayout;
    List<Order_Info> orderInfoList = new ArrayList<>();
    OrderInfoAdapter orderInfoAdapter;
    int id;
    int status;
    int status_code;
    Integer sender_id;
    Integer receiver_id;
    String user_name;

    //save order status code...
    SharedPreferences sp;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__info__page);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_order_activity",session_id);

        id = getIntent().getIntExtra("id",0);
        Log.i("Ordet_idsr",String.valueOf(id));


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        //storing status code
        sp = getSharedPreferences("order_status", Context.MODE_PRIVATE);

        pend_status = findViewById(R.id.pending_status);
        order_id = findViewById(R.id.ord_ide);
        relativeLayout = findViewById(R.id.rel_status);
        od_txt = findViewById(R.id.Order_txt);
        ven_name = findViewById(R.id.od_ven);
        date = findViewById(R.id.od_dt);
        actual_price = findViewById(R.id.od_price);
        discount = findViewById(R.id.od_discount);
        final_price = findViewById(R.id.od_final);
        total_items = findViewById(R.id.itms_count);
        imageButton = findViewById(R.id.others_btn);
        button = findViewById(R.id.chat_ven);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Order_Info_Page.this,ChatAtivity.class);
                intent.putExtra("Institution_name",user_name);
                intent.putExtra("sender_id",receiver_id);
                intent.putExtra("receiver_id",sender_id);
               startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.od_recyc);
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        orderInfoAdapter = new OrderInfoAdapter(Order_Info_Page.this,orderInfoList);
        recyclerView.setAdapter(orderInfoAdapter);
        getOrderDetails();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(getSupportFragmentManager(),"exampleBottomSheet");
            }
        });
    }

    private void getOrderDetails()
    {
        String url = base_app_url+"api/vendor/sales/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("responseInfo",response.toString());
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        sender_id = jsonObject.getInt("vendor_user_id");
                        receiver_id = jsonObject.getInt("user_id");
                        String order_ref_id = jsonObject.getString("order_ref_id");
                        order_id.setText(order_ref_id);

                        status = jsonObject.getInt("status");

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("status_code", String.valueOf(status));
                        editor.commit();



                        Log.i("StatusCode",String.valueOf(status));
                        if (status == 1)
                        {
                            pend_status.setText("Waiting For Your Approval");
                            relativeLayout.setBackgroundColor(R.color.orange_500);
                        }
                        if (status == 2)
                        {
                            pend_status.setText("Waiting For User's Approval");
                            relativeLayout.setBackgroundColor(R.color.ezy);
                        }
                        if (status == 3)
                        {
                            pend_status.setText("Proceed to Confirm");
                            relativeLayout.setBackgroundColor(R.color.ezy);
                        }
                        if (status == 4)
                        {
                            pend_status.setText("Completed");
                            relativeLayout.setBackgroundColor(R.color.green_300);
                            imageButton.setVisibility(View.GONE);
                        }
                        if (status == 5)
                        {
                            pend_status.setText("Cancelled");
                            relativeLayout.setBackgroundColor(Color.RED);
                            imageButton.setVisibility(View.GONE);
                        }
                        if (status == 6)
                        {
                            pend_status.setText("User Cancelled the Order");
                            relativeLayout.setBackgroundColor(Color.RED);
                            imageButton.setVisibility(View.GONE);
                        }
                        if (status == 7)
                        {
                            pend_status.setText("User Requested to cancel");
                            relativeLayout.setBackgroundColor(Color.RED);
                            imageButton.setVisibility(View.GONE);
                        }

                        user_name = jsonObject.getString("user_name");
                        ven_name.setText(user_name);
                        String order_date = jsonObject.getString("order_date");
                        date.setText(order_date);
                        Double tp = jsonObject.getDouble("total_price");
                        actual_price.setText(String.valueOf(tp));
                        Double dis = jsonObject.getDouble("discount");
                        discount.setText(String.valueOf(dis));
                        Double total_payment = jsonObject.getDouble("total_payment");
                        final_price.setText(String.valueOf(total_payment));
                        JSONArray jsonArray = jsonObject.getJSONArray("courses");
                        for (int i = 0; i<jsonArray.length();i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            String image = jsonObject1.getString("image");
                            Double price = jsonObject1.getDouble("price");
                            String description = jsonObject1.getString("description");
                            int qty = jsonObject1.getInt("qty");
                            Order_Info post = new Order_Info(name,image,price,description,qty);
                            orderInfoList.add(post);
                            total_items.setText(String.valueOf(orderInfoList.size())+" Items");
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ErrorInfo",e.toString());
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

    @Override
    public void onButtonClicked(String text) throws JSONException
    {
        if (text.equals("cancel"))
        {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();

            String url = base_app_url+"api/order/"+id;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status",5);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    try {
                        Log.i("ResponseOrderCancel",response.toString());
                        String message = response.getString("message");
                        if (message.equals("Success"))
                        {
                            Toast.makeText(Order_Info_Page.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Order_Info_Page.this, Sales_activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast.makeText(Order_Info_Page.this, message, Toast.LENGTH_SHORT).show();
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
        //redirect to chat
        else if(text.equals("proceed"))
        {
            Toast.makeText(this, "proceed", Toast.LENGTH_SHORT).show();


            if (status == 1)
            {
                status_code = 2;
            }
            if (status == 3)
            {
                status_code = 4;
            }
            String url = base_app_url+"api/order/"+id;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status",status_code);
            Log.i("JsonProceed",jsonObject.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    try {
                        String message = response.getString("message");
                        if (message.equals("Success"))
                        {
                            Toast.makeText(Order_Info_Page.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Order_Info_Page.this, Sales_activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            Toast.makeText(Order_Info_Page.this, message, Toast.LENGTH_SHORT).show();
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

}