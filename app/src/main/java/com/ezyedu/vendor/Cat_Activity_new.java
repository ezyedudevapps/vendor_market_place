package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.adapter.GetChatAdapter;
import com.ezyedu.vendor.model.Chat_New;
import com.ezyedu.vendor.model.CourseVolleySingleton;
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

public class Cat_Activity_new extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestQueue requestQueue;
    String session_id = null;
    int sender_id;
    int receiver_id;
    Integer get_Sender_Id,get_Receiver_Id;
    String user_nname;
    List<Chat_New> chat_newList = new ArrayList<>();
    GetChatAdapter getChatAdapter;
    SharedPreferences sp;
    EditText editText;
    ImageButton imageButton;
    int difference = 0;
    int serverListSize;
    int localListsize;
    RelativeLayout sendRelative;
    int send_message_to;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat__new);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_chat_activity",session_id);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        recyclerView = findViewById(R.id.chat_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        getChatAdapter = new GetChatAdapter(this,chat_newList);
        recyclerView.setAdapter(getChatAdapter);

        fetchSenderId();


        imageButton = findViewById(R.id.send_chat_btn);
        editText = findViewById(R.id.text_get);

        sendRelative = findViewById(R.id.rel_tst_snd);
        sendRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(chat_newList.size() -1);
                recyclerView.setItemViewCacheSize(chat_newList.size());
            }
        });


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(chat_newList.size() -1);
                recyclerView.setItemViewCacheSize(chat_newList.size());
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(chat_newList.size() -1);
                recyclerView.setItemViewCacheSize(chat_newList.size());
                String message = editText.getText().toString();
                if(TextUtils.isEmpty(message))
                {
                    Toast.makeText(Cat_Activity_new.this, "Message should not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        user_nname = getIntent().getStringExtra("Institution_name");
        Log.i("ven_inf_name",user_nname);
        get_Sender_Id = getIntent().getIntExtra("sender_id",0);
        Log.i("sender_chat_id",get_Sender_Id.toString());
        get_Receiver_Id = getIntent().getIntExtra("receiver_id",0);
        Log.i("receiver_chat_id",get_Receiver_Id.toString());

        if (sender_id != get_Sender_Id)
        {
            receiver_id = get_Sender_Id;
            send_message_to = get_Sender_Id;
            //first time calling function..
            getChat(receiver_id);

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    //  mlist.clear();
                    getChat(receiver_id);
                    handler.postDelayed(this,5000);
                }
            };handler.postDelayed(runnable,5000);

        }
        else if (sender_id != get_Receiver_Id)
        {
            receiver_id = get_Receiver_Id;
            send_message_to = get_Receiver_Id;
            //first time calling function..
            getChat(receiver_id);


            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    //  mlist.clear();
                    getChat(receiver_id);
                    handler.postDelayed(this,5000);
                }
            };handler.postDelayed(runnable,5000);

        }

        //storing sender id....
        sp = getSharedPreferences("vendor_login", Context.MODE_PRIVATE);

    }

    private void sendMessage(String message) throws JSONException {
        String base = base_app_url+"api/chat/user/";
        String url = base+send_message_to;
        Log.i("UrlSendMessage",url);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",message);
        Chat_New post = new Chat_New(sender_id,send_message_to,message);
        chat_newList.add(post);
     //   localListsize = localListsize+1;
        editText.setText(null);
        recyclerView.scrollToPosition(chat_newList.size() -1);
        recyclerView.setItemViewCacheSize(chat_newList.size());
        Log.i("MessageToSend",message);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {

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
                        Toast.makeText(Cat_Activity_new.this, message, Toast.LENGTH_SHORT).show();
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

    private void getChat(int receiver_id)
    {
        String id = String.valueOf(receiver_id);
        String base = base_app_url+"api/chat/user/"+id;
        Log.i("urlGetChat",base);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, base, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    serverListSize = jsonArray.length();
                    Log.i("serverListSize", String.valueOf(serverListSize));
                    Log.i("ChatListSize",String.valueOf(chat_newList.size()));
                    localListsize = chat_newList.size();
                    if (localListsize != serverListSize)
                    {
                        if (localListsize > 0)
                        {
                            difference = localListsize;
                            Log.i("difference", String.valueOf(difference));
                            for (int i = difference;i<serverListSize;i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int send_id = jsonObject.getInt("sender_id");
                                int receiv_id = jsonObject.getInt("receiver_id");
                                String content = jsonObject.getString("content");
                                if(receiv_id == sender_id)
                                {
                                    Chat_New post = new Chat_New(send_id,receiv_id,content);
                                    chat_newList.add(post);

                                }
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
                            // Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                            recyclerView.scrollToPosition(chat_newList.size() -1);
                            recyclerView.setItemViewCacheSize(chat_newList.size());
                        }
                        else
                        {
                            for (int i = 0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int send_id = jsonObject.getInt("sender_id");
                                int receiv_id = jsonObject.getInt("receiver_id");
                                String content = jsonObject.getString("content");
                                Chat_New post = new Chat_New(send_id,receiv_id,content);
                                chat_newList.add(post);
                            }
                            recyclerView.getAdapter().notifyDataSetChanged();
                            // Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                            recyclerView.scrollToPosition(chat_newList.size() -1);
                            recyclerView.setItemViewCacheSize(chat_newList.size());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("indexError",e.toString());
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

    private void fetchSenderId()
    {
        String url = base_app_url+"api/vendor/info/user";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseEditUser",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        sender_id = jsonObject.getInt("user_id");

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("vendor_user_id", String.valueOf(sender_id));
                        editor.apply();
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