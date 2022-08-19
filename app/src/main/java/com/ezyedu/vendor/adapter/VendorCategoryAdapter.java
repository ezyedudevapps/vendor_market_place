package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.Create_institution_form;
import com.ezyedu.vendor.Order_Info_Page;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.Vendor_category;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class VendorCategoryAdapter extends RecyclerView.Adapter<VendorCategoryAdapter.VenCatHolder> {
    Context context;
    List<Vendor_category> vendorCategoryList = new ArrayList<>();
    String img_url_base;

    public VendorCategoryAdapter(Context context, List<Vendor_category> vendorCategoryList) {
        this.context = context;
        this.vendorCategoryList = vendorCategoryList;
    }


    @NonNull
    @Override
    public VendorCategoryAdapter.VenCatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_category_adapter,parent,false);
        return new VenCatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorCategoryAdapter.VenCatHolder holder, int position)
    {
        Vendor_category vendor_category = vendorCategoryList.get(position);
       holder.textView.setText(vendor_category.getLabel());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
     Glide.with(context).load(img_url_base+vendor_category.getImage()).into(holder.imageView);

        String hash = vendor_category.getHash_id();
        String cat_label = vendor_category.getCat_label();
        String cat_hash = vendor_category.getCat_hash();
        String sub_cat_label = vendor_category.getSub_cat_label();
        String sub_cat_hash = vendor_category.getSub_cat_hash();
        int id = vendor_category.getId();




        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Create_institution_form.class);
                intent.putExtra("cat_label",cat_label);
                intent.putExtra("cat_hash",cat_hash);
                intent.putExtra("sub_cat_label",sub_cat_label);
                intent.putExtra("sub_cat_hash",sub_cat_hash);
                intent.putExtra("id",id);
                intent.putExtra("group_name","null");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorCategoryList == null ?0: vendorCategoryList.size();
    }

    public void filterList(ArrayList<Vendor_category> filteredlist)
    {
        vendorCategoryList= filteredlist;
        notifyDataSetChanged();
    }

    public class VenCatHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        RelativeLayout relativeLayout;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public VenCatHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ven_cat_img);
            textView = itemView.findViewById(R.id.ven_cat_label);
            relativeLayout = itemView.findViewById(R.id.ven_cat_relative);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
