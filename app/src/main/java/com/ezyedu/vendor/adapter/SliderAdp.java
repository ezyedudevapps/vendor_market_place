package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.SliderImages;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdp extends SliderViewAdapter<SliderAdp.Holder>{
    private Context context;
    List<SliderImages> sliderImagesList = new ArrayList<>();
    String img_url_base;

    public SliderAdp(Context context, List<SliderImages> sliderImagesList) {
        this.context = context;
        this.sliderImagesList = sliderImagesList;
    }

    @Override
    public SliderAdp.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_image_slider,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdp.Holder viewHolder, int position) {
        SliderImages a = sliderImagesList.get(position);
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
         Glide.with(context).load(img_url_base+a.getImage()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return sliderImagesList == null ?0: sliderImagesList.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
