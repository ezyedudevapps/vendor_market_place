package com.ezyedu.vendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.Edit_profile_activity;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.ProfilePreview;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PagePreviewAdapter extends RecyclerView.Adapter<PagePreviewAdapter.PagePreviewHolder> {
    private Context context;
    private List<ProfilePreview> profilePreviews = new ArrayList<>();
    String img_url_base;

    public PagePreviewAdapter(Context context, List<ProfilePreview> profilePreviews) {
        this.context = context;
        this.profilePreviews = profilePreviews;
    }

    @NonNull
    @Override
    public PagePreviewAdapter.PagePreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_preview_adapter,parent,false);
        return new PagePreviewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PagePreviewAdapter.PagePreviewHolder holder, int position)
    {
        ProfilePreview profilePreview = profilePreviews.get(position);
        holder.tittle.setText(profilePreview.getTittle());
        Log.i("tittleInstitution",profilePreview.getTittle());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+profilePreview.getLogo()).into(holder.logo);

        Double review = profilePreview.getRating();

        if (review == 0.0)
        {
            holder.b1.setVisibility(View.VISIBLE);
            holder.b2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 1.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.b2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 2.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 3.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 4.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.a4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 5.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.a4.setVisibility(View.VISIBLE);
            holder.a5.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }

        String open = profilePreview.getOpen();
        String close = profilePreview.getClose();

        String [] op = open.split(":");
        String OPEN = op[0] +":" +op[1];
        Log.i("openingTime",OPEN);


        String [] cl = close.split(":");
        String CLOSE = cl[0] +":" +cl[1];
        Log.i("openingTime",CLOSE);

        holder.time.setText("Open :"+OPEN+"  -  "+"Close :"+CLOSE);


        int mon = profilePreview.getMon();
        if (mon == 1) {
            holder.monday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.monday.setTextColor(Color.WHITE);
        }
        int tues = profilePreview.getTues();
        if (tues == 1) {
            holder.tuesday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.tuesday.setTextColor(Color.WHITE);
        }
        int wed = profilePreview.getWednes();
        if (wed == 1) {
            holder.wednesday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.wednesday.setTextColor(Color.WHITE);
        }
        int thurs = profilePreview.getThurs();
        if (thurs == 1) {
            holder.thursday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.thursday.setTextColor(Color.WHITE);
        }
        int fri = profilePreview.getFri();
        if (fri == 1) {
            holder.friday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.friday.setTextColor(Color.WHITE);
        }
        int sat = profilePreview.getSat();
        if (sat == 1) {
            holder.saturday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.saturday.setTextColor(Color.WHITE);
        }
        int sun = profilePreview.getSun();
        if (sun == 1) {
            holder.sunday.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_500));
            holder.sunday.setTextColor(Color.WHITE);
        }


        holder.instagram.setText(profilePreview.getInstagram());
        Linkify.addLinks(holder.instagram, Linkify.ALL);
        holder.facebook.setText(profilePreview.getFacebook());
        Linkify.addLinks(holder.facebook, Linkify.ALL);
        holder.twitter.setText(profilePreview.getTwitter());
        Linkify.addLinks(holder.twitter, Linkify.ALL);
        holder.youtube.setText(profilePreview.getYoutube());
        Linkify.addLinks(holder.youtube, Linkify.ALL);
        holder.test.setText(profilePreview.getTittok());
        Linkify.addLinks(holder.test, Linkify.ALL);



        holder.webpage.setText(profilePreview.getWeb());
        holder.email.setText(profilePreview.getMail());
        holder.address.setText(profilePreview.getAddress());

        String abc = holder.webpage.getText().toString();
        if (!TextUtils.isEmpty(abc))
        {
            holder.webpage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = holder.webpage.getText().toString();
                    Log.i("urlToload",url);
                    Linkify.addLinks(holder.webpage, Linkify.ALL);
                }
            });
        }

String xyz = holder.email.getText().toString();
        if (!TextUtils.isEmpty(xyz))
        {
            holder.email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Intent.ACTION_SEND);
                    String[] recepients = {holder.email.getText().toString()};
                    intent1.putExtra(Intent.EXTRA_EMAIL,recepients);
                    intent1.setType("text/plain");
                    intent1.setPackage("com.google.android.gm");
                    context.startActivity(intent1.createChooser(intent1,"send mail"));
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return profilePreviews == null ?0: profilePreviews.size();
    }

    public class PagePreviewHolder extends RecyclerView.ViewHolder {
        TextView tittle,instagram,facebook,twitter,youtube,test;
        ImageView logo,a1,a2,a3,a4,a5,b1,b2,b3,b4,b5;
        TextView webpage,email,address,time;

        public TextView  monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public PagePreviewHolder(@NonNull View itemView) {
            super(itemView);

            webpage = itemView.findViewById(R.id.web_pg_ven);
            email = itemView.findViewById(R.id.mail_pg_ven);
            time = itemView.findViewById(R.id.working_hrs);
            address = itemView.findViewById(R.id.address_pg_ven);

            tittle = itemView.findViewById(R.id.txt_artiles);
            logo = itemView.findViewById(R.id.image_to_update);

            instagram = itemView.findViewById(R.id.get_instagram);
            facebook = itemView.findViewById(R.id.get_fb);
            twitter = itemView.findViewById(R.id.get_twitter);
            youtube = itemView.findViewById(R.id.get_youtube);
            test = itemView.findViewById(R.id.ppt);


            monday = itemView.findViewById(R.id.m);
            tuesday = itemView.findViewById(R.id.t);
            wednesday =  itemView.findViewById(R.id.w);
            thursday =  itemView.findViewById(R.id.th);
            friday =  itemView.findViewById(R.id.f);
            saturday =  itemView.findViewById(R.id.s);
            sunday =  itemView.findViewById(R.id.su);



            a1 = itemView.findViewById(R.id.rating_img);
            a2 = itemView.findViewById(R.id.rating_img_1);
            a3 = itemView.findViewById(R.id.rating_img_2);
            a4 = itemView.findViewById(R.id.rating_img_3);
            a5 = itemView.findViewById(R.id.rating_img_4);
            b1 = itemView.findViewById(R.id.rating_img_empty);
            b2 = itemView.findViewById(R.id.rating_img_empty_1);
            b3 = itemView.findViewById(R.id.rating_img_empty_2);
            b4= itemView.findViewById(R.id.rating_img_empty_3);
            b5 = itemView.findViewById(R.id.rating_img_empty_4);


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
