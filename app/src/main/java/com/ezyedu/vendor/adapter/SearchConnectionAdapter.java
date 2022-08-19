package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.Add_Course_Activity;
import com.ezyedu.vendor.Connection_Actiivity;
import com.ezyedu.vendor.Courses_Activity;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.Seperate_course_activity;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.SearchConnectionList;
import com.google.android.gms.common.api.Api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchConnectionAdapter extends RecyclerView.Adapter<SearchConnectionAdapter.SeachHolder>{
    private Context context;
    private List<SearchConnectionList> searchConnectionLists = new ArrayList<>();
    RequestQueue requestQueue;
    String session_id = null;
    String base_app_url;
    String img_url_base;



    public SearchConnectionAdapter(Context context, List<SearchConnectionList> searchConnectionLists) {
        this.context = context;
        this.searchConnectionLists = searchConnectionLists;

    }

    @NonNull
    @Override
    public SearchConnectionAdapter.SeachHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_list_adapter,parent,false);
        return new SeachHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchConnectionAdapter.SeachHolder holder, int position)
    {
        SearchConnectionList a = searchConnectionLists.get(position);
        holder.name.setText(a.getName());
        holder.username.setText(a.getUsername());
        if (a.getImage().equals("null"))
        {
            Glide.with(context).load(R.drawable.empty_profile_picture).into(holder.imageView);
        }
        else
        {
            String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
            Glide.with(context).load(img_url_base+a.getImage()).into(holder.imageView);
        }
        String hash = a.getHash_id();

        holder.connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectUser(hash);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchConnectionLists == null ?0: searchConnectionLists.size();
    }

    private void connectUser(String hash)
    {
        String url = base_app_url+"api/user/connection-vendor/"+hash;
        Log.i("urlConnection",url);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Authorization", session_id)
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                if (response.isSuccessful())
                {
                    Log.d("responseSent","success");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context, "Request Sent...", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(context, Connection_Actiivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                }
                else
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context, "Failed...", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
      //  Response response = client.newCall(request).execute();
    }

    public void filterList(ArrayList<SearchConnectionList> filteredlist)
    {
        searchConnectionLists= filteredlist;
        notifyDataSetChanged();
    }

    public class SeachHolder extends RecyclerView.ViewHolder {
        TextView name,username;
        ImageView imageView;
        RelativeLayout relativeLayout;
        Button connect_btn;

        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public SeachHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tittle_product);
            username = itemView.findViewById(R.id.product_start_date);
            imageView = itemView.findViewById(R.id.img_logo);
            relativeLayout = itemView.findViewById(R.id.relative_course);
            connect_btn = itemView.findViewById(R.id.connect_user_btn);

            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
            session_id = sharedPreferences.getString("session_val","");
            Log.i("session_new",session_id);


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
