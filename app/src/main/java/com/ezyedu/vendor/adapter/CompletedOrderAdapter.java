package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.vendor.Order_Info_Page;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.completed_orders;
import com.ezyedu.vendor.model.processing_orders;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.CompletedHolder> {
    private Context context;
    private List<completed_orders> completedOrdersList = new ArrayList<>();
    String img_url_base;

    public CompletedOrderAdapter(Context context, List<completed_orders> completedOrdersList) {
        this.context = context;
        this.completedOrdersList = completedOrdersList;
    }

    @NonNull
    @Override
    public CompletedOrderAdapter.CompletedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_order_adapter,parent,false);
        return new CompletedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderAdapter.CompletedHolder holder, int position)
    {
        completed_orders completedOrders = completedOrdersList.get(position);
        holder.date.setText("Ordered at : "+completedOrders.getDate());
        holder.first_course.setText(String.valueOf(completedOrders.getQty())+" * "+completedOrders.getCourse_name());


        holder.o_id.setText("Order ID : "+String.valueOf( completedOrders.getOrder_id()));
        if (completedOrders.getCourses_count() >1) {
            holder.total_course.setText("+" + String.valueOf(completedOrders.getCourses_count() - 1) + " More Items");
        }
        else
        {
            holder.total_course.setVisibility(View.GONE);
        }
        int order_id = completedOrders.getOrder_id();
        holder.see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Order_Info_Page.class);
                intent.putExtra("id",order_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return completedOrdersList == null ?0: completedOrdersList.size();
    }

    public class CompletedHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView date,o_id,first_course,total_course;
        Button see_more;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public CompletedHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.ordr_dt);
            o_id = itemView.findViewById(R.id.Order_id);
            first_course = itemView.findViewById(R.id.fst_course);
            total_course = itemView.findViewById(R.id.ttl_course);
            relativeLayout = itemView.findViewById(R.id.relative_pending);
            see_more = itemView.findViewById(R.id.see_more_btn);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
