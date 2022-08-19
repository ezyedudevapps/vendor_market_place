package com.ezyedu.vendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.ProductPreview;

import java.util.ArrayList;
import java.util.List;

public class ProductPreviewAdapter extends RecyclerView.Adapter<ProductPreviewAdapter.ProductHolder> {
    private Context context;
    private List<ProductPreview> productPreviewList = new ArrayList<>();
    String img_url_base;

    public ProductPreviewAdapter(Context context, List<ProductPreview> productPreviewList) {
        this.context = context;
        this.productPreviewList = productPreviewList;
    }

    @NonNull
    @Override
    public ProductPreviewAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_preview_adapter,parent,false);
        return new ProductHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductPreviewAdapter.ProductHolder holder, int position)
    {
        ProductPreview preview = productPreviewList.get(position);

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+preview.getImage()).into(holder.imageView);
        holder.t2.setText("Duration : "+preview.getDuration()+" days");
        String date_time = preview.getDate();
        holder.t1.setText(date_time);
        holder.t3.setText(preview.getTittle());
        holder.t4.setText(preview.getInstitution_name());

    }

    @Override
    public int getItemCount() {
        return productPreviewList == null ?0: productPreviewList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView t1,t2,t3,t4;
        RelativeLayout relativeLayout;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);
            t1 = itemView.findViewById(R.id.course_date);
            t2 = itemView.findViewById(R.id.course_duration);
            t3 = itemView.findViewById(R.id.text_course);
            t4 = itemView.findViewById(R.id.course_inst_author);
            relativeLayout = itemView.findViewById(R.id.course_card);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
