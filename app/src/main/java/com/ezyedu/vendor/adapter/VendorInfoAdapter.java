package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.vendor.ChatAtivity;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.VendorInfo;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

public class VendorInfoAdapter extends RecyclerView.Adapter<VendorInfoAdapter.VendorHolder> {



    private Context context;
    private List<VendorInfo> vendorInfoList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public VendorInfoAdapter(Context context, List<VendorInfo> vendorInfoList) {
        this.context = context;
        this.vendorInfoList = vendorInfoList;
    }

    @NonNull
    @Override
    public VendorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_insti1_adapter,parent,false);
        return new VendorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorHolder holder, int position)
    {
        VendorInfo vendorInfo = vendorInfoList.get(position);
        String name = vendorInfo.getVendor_name();
        Log.i("ven_name_updated",name);

        Integer isChat_allow = vendorInfo.getIs_chatting_allowed();
        String ischatAllow = String.valueOf(isChat_allow);
        if (ischatAllow.equals("0"))
        {
            holder.button.setVisibility(View.GONE);
        }
        else
        {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatAtivity.class);
                    intent.putExtra("Institution_name",vendorInfo.getVendor_name());
                    intent.putExtra("Vendor_id",vendorInfo.getVendor_id());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        String path = vendorInfo.getVendor_logo();


        if (!vendorInfo.getInstagram().equals("null"))
        {
            holder.r5.setVisibility(View.VISIBLE);
            holder.instagram.setText(vendorInfo.getInstagram());
            Linkify.addLinks(holder.instagram, Linkify.ALL);
        }
        if (!vendorInfo.getFacebook().equals("null"))
        {
            holder.r8.setVisibility(View.VISIBLE);
            holder.facebook.setText(vendorInfo.getFacebook());
            Linkify.addLinks(holder.facebook, Linkify.ALL);
        }
        if (!vendorInfo.getTwitter().equals("null"))
        {
            holder.r2.setVisibility(View.VISIBLE);
            holder.twitter.setText(vendorInfo.getTwitter());
            Linkify.addLinks(holder.twitter, Linkify.ALL);
        }
        if (!vendorInfo.getYoutube().equals("null"))
        {
            holder.r3.setVisibility(View.VISIBLE);
            holder.youtube.setText(vendorInfo.getYoutube());
            Linkify.addLinks(holder.youtube, Linkify.ALL);
        }
        if (!vendorInfo.getTiktok().equals("null"))
        {
            holder.r1.setVisibility(View.VISIBLE);
            holder.test.setText(vendorInfo.getTiktok());
            Linkify.addLinks(holder.test, Linkify.ALL);
        }






        holder.website.setText(vendorInfo.getWebsite());
        holder.mail.setText(vendorInfo.getVendor_email());
        holder.address.setText(vendorInfo.getVendor_address());
        holder.time.setText(vendorInfo.getTime());


        String abc = holder.website.getText().toString();
        if (!TextUtils.isEmpty(abc))
        {
            holder.website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = holder.website.getText().toString();
                    Log.i("urlToload",url);
                    Linkify.addLinks(holder.website, Linkify.ALL);
                }
            });
        }

        String xyz = holder.mail.getText().toString();
        if (!TextUtils.isEmpty(xyz))
        {
            holder.mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Intent.ACTION_SEND);
                    String[] recepients = {holder.mail.getText().toString()};
                    intent1.putExtra(Intent.EXTRA_EMAIL,recepients);
                    intent1.setType("text/plain");
                    intent1.setPackage("com.google.android.gm");
                    context.startActivity(intent1.createChooser(intent1,"send mail"));
                }
            });
        }

        int mon = vendorInfo.getMon();
        if (mon == 1) {
            holder.monday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.monday.setTextColor(Color.WHITE);
        }
        int tues = vendorInfo.getTues();
        if (tues == 1) {
            holder.tuesday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.tuesday.setTextColor(Color.WHITE);
        }
        int wed = vendorInfo.getWednes();
        if (wed == 1) {
            holder.wednesday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.wednesday.setTextColor(Color.WHITE);
        }
        int thurs = vendorInfo.getThurs();
        if (thurs == 1) {
            holder.thursday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.thursday.setTextColor(Color.WHITE);
        }
        int fri = vendorInfo.getFri();
        if (fri == 1) {
            holder.friday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.friday.setTextColor(Color.WHITE);
        }
        int sat = vendorInfo.getSat();
        if (sat == 1) {
            holder.saturday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.saturday.setTextColor(Color.WHITE);
        }
        int sun = vendorInfo.getSun();
        if (sun == 1) {
            holder.sunday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.sunday.setTextColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return vendorInfoList == null ?0: vendorInfoList.size();
    }

    public class VendorHolder extends RecyclerView.ViewHolder {
        TextView address,website,mail,instotution_name, button,time;
        ImageView logo;

        TextView instagram,facebook,twitter,youtube,test;

        public TextView  monday, tuesday, wednesday, thursday, friday, saturday, sunday;

        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        RelativeLayout r1,r2,r3,r4,r5,r6,r7,r8;

        private GoogleMap mMap;
        public VendorHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.chat_now_btn);


            monday = itemView.findViewById(R.id.m);
            tuesday = itemView.findViewById(R.id.t);
            wednesday =  itemView.findViewById(R.id.w);
            thursday =  itemView.findViewById(R.id.th);
            friday =  itemView.findViewById(R.id.f);
            saturday =  itemView.findViewById(R.id.s);
            sunday =  itemView.findViewById(R.id.su);


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

            instagram = itemView.findViewById(R.id.get_instagram);
            facebook = itemView.findViewById(R.id.get_fb);
            twitter = itemView.findViewById(R.id.get_twitter);
            youtube = itemView.findViewById(R.id.get_youtube);
            test = itemView.findViewById(R.id.ppt);


            website = itemView.findViewById(R.id.web_pg_ven);
            mail = itemView.findViewById(R.id.mail_pg_ven);
            time = itemView.findViewById(R.id.working_hrs);
            address = itemView.findViewById(R.id.address_pg_ven);

            r1 = itemView.findViewById(R.id.sos_1);
            r2 = itemView.findViewById(R.id.sos_2);
            r3 = itemView.findViewById(R.id.sos_3);
            r4 = itemView.findViewById(R.id.sos_4);
            r5 = itemView.findViewById(R.id.sos_5);
            r6 = itemView.findViewById(R.id.sos_6);
            r7 = itemView.findViewById(R.id.sos_7);
            r8 = itemView.findViewById(R.id.sos_8);


        }

    }
}