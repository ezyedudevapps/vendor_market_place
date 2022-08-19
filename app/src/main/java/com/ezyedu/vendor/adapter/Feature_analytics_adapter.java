package com.ezyedu.vendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Feature_Analytics;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class Feature_analytics_adapter extends RecyclerView.Adapter<Feature_analytics_adapter.FeatureHolder> {
    private Context context;
    private List<Feature_Analytics> feature_analytics = new ArrayList<>();
    String img_url_base;

    public Feature_analytics_adapter(Context context, List<Feature_Analytics> feature_analytics) {
        this.context = context;
        this.feature_analytics = feature_analytics;
    }

    @NonNull
    @Override
    public FeatureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_analytics_adapter,parent,false);
        return new FeatureHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FeatureHolder holder, int position) {
        Feature_Analytics a = feature_analytics.get(position);
        holder.ideas.setText(String.valueOf(a.getIdeas()));
        holder.event.setText(String.valueOf(a.getEvent()));
      //  holder.banner.setText(String.valueOf(a.getBanner()));
        holder.messages.setText(String.valueOf(a.getMessage()));
        holder.total_view.setText(String.valueOf(a.getTotal_view()));
        //holder.compare.setText(String.valueOf(a.getCompare()));
        holder.ideas_save.setText(String.valueOf(a.getIdeas_save()));
        holder.vendor_save.setText(String.valueOf(a.getVendor_save()));


    }

    @Override
    public int getItemCount() {
        return feature_analytics == null ?0: feature_analytics.size();
    }

    public class FeatureHolder extends RecyclerView.ViewHolder {
        TextView ideas,event,banner,messages,total_view,compare,ideas_save,vendor_save;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public FeatureHolder(@NonNull View itemView) {
            super(itemView);

            total_view = itemView.findViewById(R.id.total_page_view);
            event = itemView.findViewById(R.id.events_txt);
            ideas = itemView.findViewById(R.id.ideas_txt);
         //   banner = itemView.findViewById(R.id.banner_txt);
            messages = itemView.findViewById(R.id.message_txt);
           // compare = itemView.findViewById(R.id.compare_txt);
            ideas_save = itemView.findViewById(R.id.ideas_save_txt);
            vendor_save = itemView.findViewById(R.id.total_view_txt);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
