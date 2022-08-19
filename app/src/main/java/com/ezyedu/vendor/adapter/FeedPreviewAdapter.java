package com.ezyedu.vendor.adapter;

import android.content.Context;
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
import com.ezyedu.vendor.model.Feeds;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class FeedPreviewAdapter extends RecyclerView.Adapter<FeedPreviewAdapter.FeedHolder> {
    private Context context;
    List<Feeds> feedsList = new ArrayList<>();
    String img_url_base;

    public FeedPreviewAdapter(Context context, List<Feeds> feedsList) {
        this.context = context;
        this.feedsList = feedsList;
    }

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_preview_adapter,parent,false);
        return new FeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, int position)
    {
        Feeds feeds = feedsList.get(position);
        holder.tittle.setText(feeds.getBlog_title());
        holder.date.setText(feeds.getPublished_date());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+feeds.getBlog_image()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return feedsList == null ?0: feedsList.size();
    }

    public class FeedHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tittle,date;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);
            tittle = itemView.findViewById(R.id.text_course);
            date = itemView.findViewById(R.id.course_inst_author);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
