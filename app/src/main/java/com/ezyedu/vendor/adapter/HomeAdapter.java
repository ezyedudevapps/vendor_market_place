package com.ezyedu.vendor.adapter;

import android.annotation.SuppressLint;
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
import com.ezyedu.vendor.Connection_Actiivity;
import com.ezyedu.vendor.Edit_Users_profile;
import com.ezyedu.vendor.Edit_profile_activity;
import com.ezyedu.vendor.Events_Activity;
import com.ezyedu.vendor.MainActivity;
import com.ezyedu.vendor.MessageActivity;
import com.ezyedu.vendor.My_Feeds_Activity;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.Reviews_Page;
import com.ezyedu.vendor.Sales_activity;
import com.ezyedu.vendor.Seperate_course_activity;
import com.ezyedu.vendor.model.EditBottomSheetDialog;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private Context context;
    private List<home> homeList = new ArrayList<>();
    String img_url_base;

    public HomeAdapter(Context context, List<home> homeList) {
        this.context = context;
        this.homeList = homeList;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_adapter,parent,false);
        return new HomeHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeHolder holder, int position)
    {
     home h = homeList.get(position);
     int id = h.getId();
     double val = h.getRating();
     holder.rating.setText(String.format(Locale.US, "%.1f", val));
     holder.total_review.setText(h.getRating_count() +" Ratings");


     holder.idea_v.setText(String.valueOf(h.getIdea_views()));
     holder.event_v.setText(String.valueOf(h.getEvent_views()));
     holder.t_sales.setText(String.valueOf(h.getCompleted()));
     holder.u_message.setText(String.valueOf(h.getUnread_chat()));

   /*  holder.pending.setText(String.valueOf(h.getPending()));
    holder.processing.setText(String.valueOf(h.getProcessed()));
   holder.completed.setText(String.valueOf(h.getCompleted()));

     holder.unread_chat.setText(String.valueOf(h.getUnread_chat()));
     holder.review_count.setText(String.valueOf(h.getNew_review()));
     holder.pending_users.setText(String.valueOf(h.getPrending_users()));

    */

     Double review = h.getRating();
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

        holder.completedRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Sales_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.message_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
       holder.idea_views_relative.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, My_Feeds_Activity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
           }
       });
        holder.event_views_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Events_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeList == null ?0: homeList.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder {
        TextView rating,total_review,pending,processing,completed,unread_chat,review_count,pending_users;
        TextView idea_v,event_v,t_sales,u_message;
        ImageView a1,a2,a3,a4,a5,b1,b2,b3,b4,b5;
        RelativeLayout idea_views_relative,event_views_relative,completedRelative,message_rel,newReviewRelative,pendingUsersRelative;

        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public HomeHolder(@NonNull View itemView) {
            super(itemView);


            idea_v = itemView.findViewById(R.id.idea_text);
            event_v =  itemView.findViewById(R.id.evnt_text);
            t_sales = itemView.findViewById(R.id.sales_text);
            u_message = itemView.findViewById(R.id.ch_txt);


            idea_views_relative = itemView.findViewById(R.id.pending_relative);
            event_views_relative = itemView.findViewById(R.id.unread_relative);
            completedRelative = itemView.findViewById(R.id.new_rel);
            message_rel = itemView.findViewById(R.id.chat_rel);



            rating = itemView.findViewById(R.id.avg_rating);
            total_review = itemView.findViewById(R.id.total_ratings);


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
