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
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.MyRequest;

import java.util.ArrayList;
import java.util.List;

public class My_request_adapter extends RecyclerView.Adapter<My_request_adapter.MyRequestHolder> {
    private Context context;
    private List<MyRequest> myRequestList = new ArrayList<>();
    String img_url_base;

    public My_request_adapter(Context context, List<MyRequest> myRequestList) {
        this.context = context;
        this.myRequestList = myRequestList;
    }

    @NonNull
    @Override
    public My_request_adapter.MyRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_request_adapter,parent,false);
        return new MyRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull My_request_adapter.MyRequestHolder holder, int position)
    {
        MyRequest pendingRequest = myRequestList.get(position);
        holder.name.setText(pendingRequest.getName());
        holder.username.setText(pendingRequest.getUsername());
        if (pendingRequest.getImage().equals("null"))
        {
            Glide.with(context).load(R.drawable.empty_profile_picture).into(holder.imageView);
        }
        else {
            String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
            Glide.with(context).load(img_url_base+pendingRequest.getImage()).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return myRequestList == null ?0: myRequestList.size();
    }

    public class MyRequestHolder extends RecyclerView.ViewHolder {
        TextView name,username;
        ImageView imageView;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public MyRequestHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tittle_product);
            username = itemView.findViewById(R.id.product_start_date);
            imageView = itemView.findViewById(R.id.img_logo);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
