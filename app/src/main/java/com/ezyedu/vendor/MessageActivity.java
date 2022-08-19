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
import com.ezyedu.vendor.adapter.ChatListAdapter;
import com.ezyedu.vendor.model.ChatList;
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

public class MessageActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView textView,t1;

    String session_id = null;
    RequestQueue requestQueue;

    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;
    ImageView imageView;
    private List<ChatList> mlist = new ArrayList<>();


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        drawerLayout = findViewById(R.id.drawer_layout);

        textView = findViewById(R.id.tittle_page);

        textView.setText("Messages");


        //chatlist
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cl_activity",session_id);

        recyclerView = findViewById(R.id.chat_list_recyc);
        imageView = findViewById(R.id.i11);
        t1=findViewById(R.id.t11);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        chatListAdapter = new ChatListAdapter(MessageActivity.this, mlist);
        recyclerView.setAdapter(chatListAdapter);
        fetchChatList();

    }

    private void fetchChatList()
    {
        String url = base_app_url+"api/chat/list";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonchatlist",response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i = 0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String Name = jsonObject.getString("receiver_name");
                        Integer receiver_id = jsonObject.getInt("receiver_id");
                        Integer sender_id = jsonObject.getInt("sender_id");
                        String message = jsonObject.getString("last_message");
                        String image = jsonObject.getString("receiver_image");
                        String Time = jsonObject.getString("time");
                        Integer Unread_Count = jsonObject.getInt("unread_chat");
                        ChatList post = new ChatList(Name,message,image,Time,Unread_Count,receiver_id,sender_id);
                        mlist.add(post);





                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    if (mlist.size() == 0)
                    {
                        Toast.makeText(MessageActivity.this, "No Chat Found", Toast.LENGTH_SHORT).show();
                        imageView.setVisibility(View.VISIBLE);
                        t1.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonchatError",error.toString());
                Toast.makeText(MessageActivity.this, "No Chats Found", Toast.LENGTH_SHORT).show();
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


    //message navigation menu....
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
        //recreate();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void ClickEvents(View view) {
        // redirectActivity(this,);
        MainActivity.redirectActivity(this, Events_Activity.class);
    }

    public void Clickfeeds(View view)
    {
        MainActivity.redirectActivity(this,Bank_Account_Activity.class);
    }

    public void Clickpayment(View view)
    {
        MainActivity.redirectActivity(this,Payment_activity.class);
        //  redirectActivity(this,);
    }
    public void Clickusers(View view)
    {
        //  redirectActivity(this,);
        MainActivity.redirectActivity(this,Courses_Activity.class);
    }
    public void Clickreviews(View view)
    {
        MainActivity.redirectActivity(this,Reviews_Page.class);
       // Toast.makeText(this, "Reviews", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickanalytics(View view)
    {
        MainActivity.redirectActivity(this,Analytics_Activity.class);

    }
    public void Clicksales(View view)
    {
        MainActivity.redirectActivity(this,Sales_activity.class);
      //  Toast.makeText(this, "Sales", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickinstitute(View view)
    {
        MainActivity.redirectActivity(this,Edit_profile_activity.class);

        // redirectActivity(this,);
    }
    public void Clicksettings(View view)
    {
        MainActivity.redirectActivity(this,Settings_Activity.class);
      //  Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickproducts(View view)
    {
        MainActivity.redirectActivity(this,Connection_Actiivity.class);
     //   Toast.makeText(this, "Products", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickfaq(View view)
    {
        Toast.makeText(this, "Faq", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void ClickFeeds(View view)
    {
        MainActivity. redirectActivity(this,My_Feeds_Activity.class);
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