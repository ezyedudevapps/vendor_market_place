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
import com.ezyedu.vendor.model.CourseAllReview;
import com.ezyedu.vendor.model.CourseReview;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Course_all_review_adapter extends RecyclerView.Adapter<Course_all_review_adapter.CourseAllHolder>
{
    private Context context;
    private List<CourseAllReview> courseAllReviewList = new ArrayList<>();
    String img_url_base;

    public Course_all_review_adapter(Context context, List<CourseAllReview> courseAllReviewList) {
        this.context = context;
        this.courseAllReviewList = courseAllReviewList;
    }

    @NonNull
    @Override
    public CourseAllHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_course_adapter,parent,false);
        return new CourseAllHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAllHolder holder, int position)
    {
        CourseAllReview courses = courseAllReviewList.get(position);
        holder.username.setText(courses.getUsername());
        holder.description.setText(courses.getUser_description());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+courses.getUser_image()).into(holder.imageView);
        int star = courses.getUser_rating();
        if (star == 0)
        {
            holder.e.setVisibility(View.VISIBLE);
            holder.e1.setVisibility(View.VISIBLE);
            holder.e2.setVisibility(View.VISIBLE);
            holder.e3.setVisibility(View.VISIBLE);
            holder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 1)
        {
            holder.rating_img.setVisibility(View.VISIBLE);
            holder.e1.setVisibility(View.VISIBLE);
            holder.e2.setVisibility(View.VISIBLE);
            holder.e3.setVisibility(View.VISIBLE);
            holder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 2)
        {
            holder.rating_img.setVisibility(View.VISIBLE);
            holder.f1.setVisibility(View.VISIBLE);
            holder.e2.setVisibility(View.VISIBLE);
            holder.e3.setVisibility(View.VISIBLE);
            holder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 3)
        {
            holder.rating_img.setVisibility(View.VISIBLE);
            holder.f1.setVisibility(View.VISIBLE);
            holder.f2.setVisibility(View.VISIBLE);
            holder.e3.setVisibility(View.VISIBLE);
            holder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 4)
        {
            holder.rating_img.setVisibility(View.VISIBLE);
            holder.f1.setVisibility(View.VISIBLE);
            holder.f2.setVisibility(View.VISIBLE);
            holder.f3.setVisibility(View.VISIBLE);
            holder.e4.setVisibility(View.VISIBLE);
        }
        else if (star == 5)
        {
            holder.rating_img.setVisibility(View.VISIBLE);
            holder.f1.setVisibility(View.VISIBLE);
            holder.f2.setVisibility(View.VISIBLE);
            holder.f3.setVisibility(View.VISIBLE);
            holder.f4.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.rating_img.setVisibility(View.VISIBLE);
            holder.f1.setVisibility(View.VISIBLE);
            holder.f2.setVisibility(View.VISIBLE);
            holder.f3.setVisibility(View.VISIBLE);
            holder.f4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return courseAllReviewList == null ?0: courseAllReviewList.size();
    }

    public class CourseAllHolder extends RecyclerView.ViewHolder {
        ImageView imageView,rating_img,f1,f2,f3,f4,e,e1,e2,e3,e4;
        TextView username,description;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public CourseAllHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_logo);
            username = itemView.findViewById(R.id.user_review);
            description = itemView.findViewById(R.id.description_review);


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


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);


            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);
        }
    }
}
