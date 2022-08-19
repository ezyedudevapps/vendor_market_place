package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.Seperate_Feeds;
import com.ezyedu.vendor.Seperate_course_activity;
import com.ezyedu.vendor.model.AllFeeds;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class AllFeedsAdapter extends RecyclerView.Adapter<AllFeedsAdapter.AllFeedHolder> {
    private Context context;
    private List<AllFeeds> allFeeds = new ArrayList<>();

    String img_url_base;

    public AllFeedsAdapter(Context context, List<AllFeeds> allFeeds) {
        this.context = context;
        this.allFeeds = allFeeds;
    }

    @NonNull
    @Override
    public AllFeedsAdapter.AllFeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_adapter,parent,false);
        return new AllFeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFeedsAdapter.AllFeedHolder holder, int position)
    {

        AllFeeds a = allFeeds.get(position);
        holder.tittle.setText(a.getTittle());
        holder.date.setText(a.getCategory());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.imageView);

        String hash = a.getHash_id();



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Seperate_Feeds.class);
                intent.putExtra("id",hash);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        int status = a.getStatus();
        if (status == 0)
        {
            holder.status.setText("Status : Pending");
        }
        if (status == 1)
        {
            holder.status.setText("Status : Approved");
        }

    }

    @Override
    public int getItemCount() {
        return allFeeds == null ?0: allFeeds.size();
    }

    public class AllFeedHolder extends RecyclerView.ViewHolder {

        TextView tittle,date,status;
        ImageView imageView;
        RelativeLayout relativeLayout;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public AllFeedHolder(@NonNull View itemView) {
            super(itemView);

            tittle = itemView.findViewById(R.id.tittle_product);
            date = itemView.findViewById(R.id.product_start_date);
            imageView = itemView.findViewById(R.id.img_logo);
            relativeLayout = itemView.findViewById(R.id.relative_course);
            status = itemView.findViewById(R.id.product_status);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_urlfeed_global",img_url_base);


        }
    }
}
