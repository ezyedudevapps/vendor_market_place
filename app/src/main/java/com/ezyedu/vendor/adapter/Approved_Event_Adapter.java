package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Approved_events;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class Approved_Event_Adapter extends RecyclerView.Adapter<Approved_Event_Adapter.ApprovedHolder> {
    private Context context;
    private List<Approved_events> approved_eventsList = new ArrayList<>();
    String img_url_base;

    public Approved_Event_Adapter(Context context, List<Approved_events> approved_eventsList) {
        this.context = context;
        this.approved_eventsList = approved_eventsList;
    }

    @NonNull
    @Override
    public ApprovedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_event_adapter,parent,false);
        return new ApprovedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovedHolder holder, int position)
    {
        Approved_events a = approved_eventsList.get(position);
        holder.tittle.setText(a.getTittle());

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return approved_eventsList == null ?0: approved_eventsList.size();
    }

    public class ApprovedHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tittle;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public ApprovedHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.article_img_1);
            tittle = itemView.findViewById(R.id.txt_artiles);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
