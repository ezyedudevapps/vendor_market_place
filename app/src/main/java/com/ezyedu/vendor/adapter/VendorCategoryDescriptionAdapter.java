package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.VendorCategoryDescription;

import java.util.ArrayList;
import java.util.List;

public class VendorCategoryDescriptionAdapter extends RecyclerView.Adapter<VendorCategoryDescriptionAdapter.VenCatHolder> {
     Context context;
     List<VendorCategoryDescription> vendorCategoryDescriptionList = new ArrayList<>();
    String img_url_base;

    public VendorCategoryDescriptionAdapter(Context context, List<VendorCategoryDescription> vendorCategoryDescriptionList) {
        this.context = context;
        this.vendorCategoryDescriptionList = vendorCategoryDescriptionList;
    }

    @NonNull
    @Override
    public VendorCategoryDescriptionAdapter.VenCatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_category_description_adapte,parent,false);
        return new VenCatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorCategoryDescriptionAdapter.VenCatHolder holder, int position)
    {
        VendorCategoryDescription vendorCategoryDescription = vendorCategoryDescriptionList.get(position);
        holder.tittle.setText(vendorCategoryDescription.getTittle());
        holder.content.setText(vendorCategoryDescription.getContent());
        Log.i("CatdescTittle",vendorCategoryDescription.getTittle());

    }

    @Override
    public int getItemCount() {
        return vendorCategoryDescriptionList == null ?0: vendorCategoryDescriptionList.size();
    }

    public class VenCatHolder extends RecyclerView.ViewHolder {
        TextView tittle;
        EditText content;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public VenCatHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.tittle_cat);
            content = itemView.findViewById(R.id.content_cat);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
