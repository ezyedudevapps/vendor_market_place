package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.ezyedu.vendor.Course_All_Reviews;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.Seperate_course_activity;
import com.ezyedu.vendor.model.CourseReview;
import com.ezyedu.vendor.model.Courses;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class CourseReviewAdapter extends RecyclerView.Adapter<CourseReviewAdapter.CRHolder>
{
    private Context context;
    private List<CourseReview> courseReviews = new ArrayList<>();
    String img_url_base;

    public CourseReviewAdapter(Context context, List<CourseReview> courseReviews) {
        this.context = context;
        this.courseReviews = courseReviews;
    }

    @NonNull
    @Override
    public CourseReviewAdapter.CRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_course_review_adapter,parent,false);
        return new CRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseReviewAdapter.CRHolder holder, int position)
    {
        CourseReview courses = courseReviews.get(position);
        holder.tittle.setText(courses.getTittle());
        holder.date.setText(courses.getDate());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+courses.getLogo()).into(holder.imageView);
        String hash = courses.getHash_id();
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Course_All_Reviews.class);
                intent.putExtra("id",hash);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return courseReviews == null ?0: courseReviews.size();
    }

    public class CRHolder extends RecyclerView.ViewHolder {
        TextView tittle,date;
        ImageView imageView;
        RelativeLayout relativeLayout;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public CRHolder(@NonNull View itemView)
        {
            super(itemView);
            tittle = itemView.findViewById(R.id.tittle_product);
            date = itemView.findViewById(R.id.product_start_date);
            imageView = itemView.findViewById(R.id.img_logo);
            relativeLayout = itemView.findViewById(R.id.relative_course);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
