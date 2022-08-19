package com.ezyedu.vendor.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.VenSliderImages;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class VenSliderAdp extends SliderViewAdapter<VenSliderAdp.Holder> {

    private Context context;
    private List<VenSliderImages> venSliderImagesList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;


    public VenSliderAdp(Context context, List<VenSliderImages> venSliderImagesList) {
        this.context = context;
        this.venSliderImagesList = venSliderImagesList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_image_slider1,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(VenSliderAdp.Holder viewHolder, int position) {
        VenSliderImages a = venSliderImagesList.get(position);
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(viewHolder.imageView);

        Glide.with(context).load(img_url_base+a.getLogo()).into(viewHolder.logo);
        viewHolder.textView.setText(a.getTittle());

        int star = a.getRating();

        if (star == 0)
        {
            viewHolder.e.setVisibility(View.VISIBLE);
            viewHolder.e1.setVisibility(View.VISIBLE);
            viewHolder.e2.setVisibility(View.VISIBLE);
            viewHolder.e3.setVisibility(View.VISIBLE);
            viewHolder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 1)
        {
            viewHolder.rating_img.setVisibility(View.VISIBLE);
            viewHolder.e1.setVisibility(View.VISIBLE);
            viewHolder.e2.setVisibility(View.VISIBLE);
            viewHolder.e3.setVisibility(View.VISIBLE);
            viewHolder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 2 )
        {
            viewHolder.rating_img.setVisibility(View.VISIBLE);
            viewHolder.f1.setVisibility(View.VISIBLE);
            viewHolder.e2.setVisibility(View.VISIBLE);
            viewHolder.e3.setVisibility(View.VISIBLE);
            viewHolder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 3)
        {
            viewHolder.rating_img.setVisibility(View.VISIBLE);
            viewHolder.f1.setVisibility(View.VISIBLE);
            viewHolder.f2.setVisibility(View.VISIBLE);
            viewHolder.e3.setVisibility(View.VISIBLE);
            viewHolder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 4)
        {
            viewHolder.rating_img.setVisibility(View.VISIBLE);
            viewHolder.f1.setVisibility(View.VISIBLE);
            viewHolder.f2.setVisibility(View.VISIBLE);
            viewHolder.f3.setVisibility(View.VISIBLE);
            viewHolder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 5)
        {
            viewHolder.rating_img.setVisibility(View.VISIBLE);
            viewHolder.f1.setVisibility(View.VISIBLE);
            viewHolder.f2.setVisibility(View.VISIBLE);
            viewHolder.f3.setVisibility(View.VISIBLE);
            viewHolder.f4.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.rating_img.setVisibility(View.VISIBLE);
            viewHolder.f1.setVisibility(View.VISIBLE);
            viewHolder.f2.setVisibility(View.VISIBLE);
            viewHolder.f3.setVisibility(View.VISIBLE);
            viewHolder.f4.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getCount() {
        return venSliderImagesList == null ?0: venSliderImagesList.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        ImageView imageView,logo;
        TextView textView;
        ImageView rating_img,f1,f2,f3,f4,e,e1,e2,e3,e4;

        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);
            logo = itemView.findViewById(R.id.ven_logo_new);
            textView = itemView.findViewById(R.id.txt_artiles);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


            rating_img = itemView.findViewById(R.id.rating_img);
            f1 = itemView.findViewById(R.id.rating_img_1);
            f2 = itemView.findViewById(R.id.rating_img_2);
            f3 = itemView.findViewById(R.id.rating_img_3);
            f4 = itemView.findViewById(R.id.rating_img_4);
            e = itemView.findViewById(R.id.rating_img_empty);
            e1 = itemView.findViewById(R.id.rating_img_empty_1);
            e2 = itemView.findViewById(R.id.rating_img_empty_2);
            e3= itemView.findViewById(R.id.rating_img_empty_3);
            e4 = itemView.findViewById(R.id.rating_img_empty_4);
        }
    }
}