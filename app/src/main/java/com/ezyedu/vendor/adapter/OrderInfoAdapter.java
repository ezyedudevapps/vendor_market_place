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
import com.ezyedu.vendor.model.Order_Info;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoAdapter extends RecyclerView.Adapter<OrderInfoAdapter.OrderHolder> {
   private Context context;
   private List<Order_Info> orderInfoList = new ArrayList<>();
    String img_url_base;

    public OrderInfoAdapter(Context context, List<Order_Info> orderInfoList) {
        this.context = context;
        this.orderInfoList = orderInfoList;
    }

    @NonNull
    @Override
    public OrderInfoAdapter.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.order_info_adapter,parent,false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderInfoAdapter.OrderHolder holder, int position)
    {
        Order_Info order_info = orderInfoList.get(position);
        holder.Tittle.setText(order_info.getName());
        holder.quantity.setText("Qty - "+order_info.getQty());
        holder.price.setText("Price : "+order_info.getPrice());
        holder.descp.setText(order_info.getDescription());

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+order_info.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return orderInfoList == null ?0: orderInfoList.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        TextView Tittle,quantity,price,descp;
        ImageView imageView;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.course_img);
            Tittle = itemView.findViewById(R.id.c_t);
            quantity = itemView.findViewById(R.id.c_qty);
            price = itemView.findViewById(R.id.prc);
            descp = itemView.findViewById(R.id.description);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
