package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.GetChatAdapter;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.GetChat;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatAtivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editText;
    ImageButton imageButton;
    GetChatAdapter getChatAdapter;
    private List<GetChat> mlist = new ArrayList<>();
    String Institute = null;
    Integer sender_id,receiver_id;
    TextView textView;
    RequestQueue requestQueue;
    String txt = null;
    SharedPreferences sp,sp1;
    String session_id = null;
    int messageSize;
    int mlist_size;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ativity);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_chat_activity",session_id);



        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        imageButton = findViewById(R.id.send_chat_btn);
        editText = findViewById(R.id.text_get);
        Institute = getIntent().getStringExtra("Institution_name");
        Log.i("ven_inf_name",Institute);
        sender_id = getIntent().getIntExtra("sender_id",0);
        Log.i("sender_chat_id",sender_id.toString());
        receiver_id = getIntent().getIntExtra("receiver_id",0);
        Log.i("receiver_chat_id",receiver_id.toString());

        //storing vendor chat id
        sp = getSharedPreferences("ven_ch_ide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vendor_chat_id", String.valueOf(sender_id));
        editor.commit();

        textView = findViewById(R.id.institution_nm);
        textView.setText("Welcome to "+Institute);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

   /*     recyclerView = findViewById(R.id.chat_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        getChatAdapter = new GetChatAdapter(this,mlist);
        recyclerView.setAdapter(getChatAdapter);
        // recyclerView.scrollToPosition(mlist.size()-1);
        fetchData(sender_id);

    */

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                //  mlist.clear();
                fetchData(sender_id);
                handler.postDelayed(this,5000);
            }
        };handler.postDelayed(runnable,5000);

    /*    Handler handler1 = new Handler();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {

                //  mlist.clear();
                recyclerView.scrollToPosition(mlist.size() - 1);
                handler1.postDelayed(this,1000);
            }
        };handler.postDelayed(runnable1,1000);


     */
      //  recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

     /*   editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(mlist.size() - 1);
            }
        });
      */


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                try {
                    txt = editText.getText().toString();
                    if (TextUtils.isEmpty(txt))
                    {
                        Toast.makeText(ChatAtivity.this, "Message Field Shold not be empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendText(txt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void sendText( String txt) throws JSONException {
        editText.setText(null);
        //333 is a dummy value mentioning instead of user id

        GetChat a = new GetChat(receiver_id,txt);
        mlist.add(a);
          getChatAdapter.notifyDataSetChanged();

        String base = base_app_url+"api/chat/user/";
        String url = base+sender_id;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",txt);
        final String mRequestBody = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("jsonChatsend",response.toString());
                mlist_size = mlist_size+1;
                //   fetchData(vendor_id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonchatFaill",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonError);
                        String message = jsonObject1.getString("message");
                        Toast.makeText(ChatAtivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
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

    private void fetchData(Integer vendor_id) {
        String base = base_app_url+"api/chat/user/";
        String url = base+vendor_id;
        Log.i("url_chat_full",url);
        Log.i("vendor_passed_id",vendor_id.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonObjChat",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("arrJSLen", String.valueOf(jsonArray.length()));
                    if (mlist_size != jsonArray.length()) {
                        mlist.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Integer sender_id = jsonObject.getInt("sender_id");
                            String message = jsonObject.getString("content");
                            Log.i("first_message", message);
                            GetChat post = new GetChat(sender_id, message);
                            mlist.add(post);
                            Log.i("ArSiz", String.valueOf(mlist.size()));
                            mlist_size = mlist.size();
                        }
                        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                        recyclerView.scrollToPosition(mlist.size() - 1);
                    }
                    //getChatAdapter.notifyItemInserted(mlist.size());
                    // getChatAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("catchChatError",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonChatError",error.toString());

            }
        })
        {
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