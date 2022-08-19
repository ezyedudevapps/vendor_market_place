package com.ezyedu.vendor.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.VendorInfo1;

import java.util.ArrayList;
import java.util.List;

public class VendorInfo1Adapter extends RecyclerView.Adapter<VendorInfo1Adapter.Viholder> {
    private Context context;
    private List<VendorInfo1> vendorInfo1List = new ArrayList<>();

    public VendorInfo1Adapter(Context context, List<VendorInfo1> vendorInfo1List) {
        this.context = context;
        this.vendorInfo1List = vendorInfo1List;
    }

    @NonNull
    @Override
    public VendorInfo1Adapter.Viholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vndor_info1_adapter,parent,false);
        return new Viholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorInfo1Adapter.Viholder holder, int position)
    {
        VendorInfo1 a = vendorInfo1List.get(position);
        holder.tittle.setText(a.getTittle());
        holder.description.setText(a.getDescription());
    }

    @Override
    public int getItemCount() {
        return vendorInfo1List == null ?0: vendorInfo1List.size();
    }

    public class Viholder extends RecyclerView.ViewHolder {
        TextView tittle,description;
        public Viholder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.w1);
            description = itemView.findViewById(R.id.web_pg_ven);
        }
    }
}