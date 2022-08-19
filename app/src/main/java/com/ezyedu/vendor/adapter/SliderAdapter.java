package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.PreviewGalleryImages;
import com.google.gson.internal.$Gson$Preconditions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.Holder>
{
    private Context context;
    List<PreviewGalleryImages> previewGalleryImagesList;
    String img_url_base;

    public SliderAdapter(Context context, List<PreviewGalleryImages> previewGalleryImagesList) {
        this.context = context;
        this.previewGalleryImagesList = previewGalleryImagesList;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        PreviewGalleryImages previewGalleryImages = previewGalleryImagesList.get(position);
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+previewGalleryImages.getImage()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return previewGalleryImagesList == null ?0: previewGalleryImagesList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
