package com.ezyedu.vendor.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.Add_Course_Activity;
import com.ezyedu.vendor.Courses_Activity;
import com.ezyedu.vendor.Edit_course_Activity;
import com.ezyedu.vendor.Login_page;
import com.ezyedu.vendor.MainActivity;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.SeperateCourse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SeperateCourseAdapter extends RecyclerView.Adapter<SeperateCourseAdapter.SeperateHolder> {
    public Context context;
    private List<SeperateCourse> seperateCourseList = new ArrayList<>();
    RequestQueue requestQueue;
    String session_id = null;
    String base_app_url;
    ProgressDialog progressDialog;
    String img_url_base;


    public SeperateCourseAdapter(Context context, List<SeperateCourse> seperateCourseList) {
        this.context = context;
        this.seperateCourseList = seperateCourseList;
    }

    @NonNull
    @Override
    public SeperateCourseAdapter.SeperateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperate_course_adapter,parent,false);
        return new SeperateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeperateCourseAdapter.SeperateHolder holder, int position)
    {
        SeperateCourse seperateCourse = seperateCourseList.get(position);
        holder.tittle.setText(seperateCourse.getCourse_title());
       holder.description.setText(seperateCourse.getCourse_description());
        String dpc = NumberFormat.getNumberInstance(Locale.US).format(seperateCourse.getDiscount_price());
        holder.dp.setText("Rp : "+dpc);
        String ipc = NumberFormat.getNumberInstance(Locale.US).format(seperateCourse.getInitial_price());
        holder.ip.setText("Rp : "+ipc);
        holder.ip.setPaintFlags(holder.ip.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (seperateCourse.getCourse_duration() == null || seperateCourse.getCourse_duration() == "null")
        {
            holder.duration.setText("Duration");
        }
        else
        {
            holder.duration.setText(seperateCourse.getCourse_duration()+" Days");
        }
        holder.date.setText(seperateCourse.getStart_date());

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
      //  Glide.with(context).load(img_url+seperateCourse.getCourses_image()).into(holder.imageView);

        String course_hash_id = seperateCourse.getCourse_hash_id();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dig = new AlertDialog.Builder(context).setTitle("Please Select").setMessage("Are you Sure Want to Delete this Course ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        progressDialog = new ProgressDialog(context);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        deleteCourse(course_hash_id);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dig.show();

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Edit_course_Activity.class);
                intent.putExtra("id",course_hash_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void deleteCourse(String course_hash_id)
    {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(base_app_url+"api/courses/"+course_hash_id)
                        .method("DELETE", body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", session_id)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("ResponseCourseFailure", e.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                        Log.d("ResponseAddCourseDelete", response.body().string());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(context, "Course Deleted Successfully...", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent intent1 = new Intent(context, Courses_Activity.class);
                                    context.startActivity(intent1);
                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Try Again Later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
    @Override
    public int getItemCount() {
        return seperateCourseList == null ?0: seperateCourseList.size();
    }

    public class SeperateHolder extends RecyclerView.ViewHolder {
        Button delete,edit;
        TextView tittle,date,duration,ip,dp,description;

        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public SeperateHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.del_course_icon);
            edit = itemView.findViewById(R.id.edit_icon);
            tittle = itemView.findViewById(R.id.tittle_course);
            date = itemView.findViewById(R.id.s_dt);
            duration = itemView.findViewById(R.id.c_dur);
            ip = itemView.findViewById(R.id.initial_price);
            dp = itemView.findViewById(R.id.discount_price);
            description = itemView.findViewById(R.id.c_desc);


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


            requestQueue = CourseVolleySingleton.getInstance(context).getRequestQueue();

            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
            session_id = sharedPreferences.getString("session_val","");
            Log.i("session_new",session_id);

        }
    }
}
