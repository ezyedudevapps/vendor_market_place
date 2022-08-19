package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.PaymentAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.Payment;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment_activity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView textView,no_payment;
    ImageView imageView;
    RecyclerView recyclerView;
    String session_id = null;
    RequestQueue requestQueue;
    private List<Payment> paymentList = new ArrayList<>();
    PaymentAdapter paymentAdapter;
    ShimmerFrameLayout shimmerFrameLayout;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activity);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        drawerLayout = findViewById(R.id.drawer_layout);
        textView = findViewById(R.id.tittle_page);
        textView.setText("Payments");

        no_payment = findViewById(R.id.No_payment);
        imageView = findViewById(R.id.i11);

        shimmerFrameLayout = findViewById(R.id.shimmer_frame_layout);
        shimmerFrameLayout.startShimmerAnimation();
        recyclerView = findViewById(R.id.payment_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        paymentAdapter = new PaymentAdapter(Payment_activity.this,paymentList);
        recyclerView.setAdapter(paymentAdapter);
        fetchData(session_id);


    }

    private void fetchData(String session_id)
    {
        String url = base_app_url+"api/vendor/payments";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ResponsePayment",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String order_id = jsonObject.getString("order_ref_id");
                            Double amount = jsonObject.getDouble("final_amount");
                            String date = jsonObject.getString("date");

                            Payment post = new Payment(order_id,amount,date);
                            paymentList.add(post);
                        }
                    }
                    else
                    {
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        no_payment.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        no_payment.setText(message);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shimmerFrameLayout.startShimmerAnimation();
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

    public void ClickMenu(View view)
    {
        MainActivity.openDrawer(drawerLayout);
    }
    public void Clickhome(View view)
    {
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public void Clicmessages(View view)
    {
        MainActivity.redirectActivity(this,MessageActivity.class);
    }

    public void ClickEvents(View view)
    {
        // redirectActivity(this,);
        MainActivity.redirectActivity(this,Events_Activity.class);
    }
    public void Clickfeeds(View view)
    {
        MainActivity.redirectActivity(this,Bank_Account_Activity.class);
        //recreate();
    }

    public void Clickpayment(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        //  redirectActivity(this,);
    }
    public void Clickusers(View view)
    {
        MainActivity.redirectActivity(this,Courses_Activity.class);

    }
    public void Clickreviews(View view)
    {
        MainActivity.redirectActivity(this,Reviews_Page.class);
    }
    public void Clickanalytics(View view)
    {
        MainActivity.redirectActivity(this,Analytics_Activity.class);
    }
    public void Clicksales(View view)
    {
        MainActivity.redirectActivity(this,Sales_activity.class);

    }
    public void ClickFeeds(View view)
    {
        MainActivity. redirectActivity(this,My_Feeds_Activity.class);
    }

    public void Clickinstitute(View view)
    {
        MainActivity.redirectActivity(this,Edit_profile_activity.class);
        // redirectActivity(this,);
    }
    public void Clicksettings(View view)
    {
        MainActivity.redirectActivity(this,Settings_Activity.class);
    }
    public void Clickproducts(View view)
    {
        MainActivity.redirectActivity(this,Connection_Actiivity.class);
    }
    public void Clickfaq(View view)
    {
        Toast.makeText(this, "Faq", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Intent intent1 = new Intent(this,MainActivity.class);
            startActivity(intent1);
        }
    }
}