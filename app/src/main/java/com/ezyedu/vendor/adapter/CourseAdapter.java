package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.Seperate_course_activity;
import com.ezyedu.vendor.model.Courses;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    private Context context;
    private List<Courses> coursesList = new ArrayList<>();
    String img_url_base;

    public CourseAdapter(Context context, List<Courses> coursesList) {
        this.context = context;
        this.coursesList = coursesList;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_adapter,parent,false);
        return new CourseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position)
    {
        Courses courses = coursesList.get(position);
        holder.tittle.setText(courses.getTittle());
        holder.date.setText(courses.getDate());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+courses.getLogo()).into(holder.imageView);

        String hash = courses.getHash_id();



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Seperate_course_activity.class);
                intent.putExtra("id",hash);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList == null ?0: coursesList.size();
    }

    public class CourseHolder extends RecyclerView.ViewHolder {
        TextView tittle,date;
        ImageView imageView;
        RelativeLayout relativeLayout;
        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public CourseHolder(@NonNull View itemView) {
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
