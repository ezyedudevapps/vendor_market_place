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
import com.ezyedu.vendor.model.VendorFacilities;

import java.util.ArrayList;
import java.util.List;

public class VendorFacilityAdapter extends RecyclerView.Adapter<VendorFacilityAdapter.VfacHolder> {
    private Context context;
    private List<VendorFacilities> vendorFacilitiesList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;


    public VendorFacilityAdapter(Context context, List<VendorFacilities> vendorFacilitiesList) {
        this.context = context;
        this.vendorFacilitiesList = vendorFacilitiesList;
    }

    @NonNull
    @Override
    public VendorFacilityAdapter.VfacHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_facility_adapter,parent,false);
        return new VfacHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorFacilityAdapter.VfacHolder holder, int position)
    {
        VendorFacilities a = vendorFacilitiesList.get(position);
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.imageView);

        holder.tittle.setText(a.getTittle());
    }

    @Override
    public int getItemCount() {
        return vendorFacilitiesList == null ?0: vendorFacilitiesList.size();
    }

    public class VfacHolder extends RecyclerView.ViewHolder {
        TextView tittle;
        ImageView imageView;

        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public VfacHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.facility_tittle);
            imageView = itemView.findViewById(R.id.facility_im);



            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}

