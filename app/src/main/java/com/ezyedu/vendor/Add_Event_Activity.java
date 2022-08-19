package com.ezyedu.vendor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.RealPathUtil;

import net.gotev.uploadservice.Logger;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.platform.android.AndroidLog;
import okhttp3.logging.HttpLoggingInterceptor;

import static net.gotev.uploadservice.Logger.setLogLevel;

public class Add_Event_Activity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    TextView start_date,end_date;


    int code;

    EditText tittle,description,address;
    ImageView imageView;
    RelativeLayout relativeLayout;
    Button button;
    String path;

    String ttl;
    String desc;
    String add;
    String sd;
    String ed;
    String session_id = null;
    ProgressDialog progressDialog;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        start_date = findViewById(R.id.st_dt);
        end_date = findViewById(R.id.ed_dt);

        tittle = findViewById(R.id.tittle_event);
        description = findViewById(R.id.description_event);
        address = findViewById(R.id.add_address);
        imageView = findViewById(R.id.add_course_image);
        relativeLayout = findViewById(R.id.add_img);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        ActivityCompat.requestPermissions(Add_Event_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent1= new Intent();
                    intent1.setType("image/*");
                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent1,10);
                }
                else
                {
                    ActivityCompat.requestPermissions(Add_Event_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });


        button = findViewById(R.id.submit_event);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttl = tittle.getText().toString();
                desc = description.getText().toString();
                add = address.getText().toString();
                sd = start_date.getText().toString();
                ed = end_date.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("title",ttl);
                    jsonObject.put("description",desc);
                    jsonObject.put("address",add);
                    jsonObject.put("start_dt",sd);
                    jsonObject.put("end_dt",ed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("JSONEvent",jsonObject.toString());

                if (TextUtils.isEmpty(ttl))
                {
                    Toast.makeText(Add_Event_Activity.this, "Tittle Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(desc))
                {
                    Toast.makeText(Add_Event_Activity.this, "Description Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(add))
                {
                    Toast.makeText(Add_Event_Activity.this, "Address Should not be Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(sd))
                {
                    Toast.makeText(Add_Event_Activity.this, "Please Select the Start Date", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(ed))
                {
                    Toast.makeText(Add_Event_Activity.this, "Please Select the End Date", Toast.LENGTH_SHORT).show();
                }
                else if (path == null)
                {
                    Toast.makeText(Add_Event_Activity.this, "Please Select the Image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AddEvent();
                    progressDialog = new ProgressDialog(Add_Event_Activity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                }


            }
        });

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(start_date);
                code = 1;
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(end_date);
                code = 2;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            Context context = Add_Event_Activity.this;
            path = RealPathUtil.getRealPath(context,uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }
    }


    private void AddEvent()
    {

        try {
            File file = new File(path);
            Log.i("Path",path);
            Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file.getPath());
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("images[0]", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                    .addFormDataPart("title",tittle.getText().toString())
                    .addFormDataPart("description",description.getText().toString())
                    .addFormDataPart("address",address.getText().toString())
                    .addFormDataPart("latitude","1.12764618351")
                    .addFormDataPart("longitude","-1.3762323")
                    .addFormDataPart("start_at",start_date.getText().toString())
                    .addFormDataPart("finish_at",end_date.getText().toString())
                    .build();
            Log.i("Lg",file.getName());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(base_app_url+"api/event")
                    .addHeader("Authorization", session_id)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("ResponseImgFailure", e.toString());
                    progressDialog.dismiss();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    //  Log.d("ResponseAddFeed", response.body().string());
                    String message =  response.peekBody(2048).string();
                    Log.i("MessageAddFeed",message);
                    progressDialog.dismiss();
                    if(response.isSuccessful())
                    {
                        Log.i("Result","Success");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(Add_Event_Activity.this, "Event Added Success", Toast.LENGTH_SHORT);
                                toast.show();
                                  Intent intent1 = new Intent(Add_Event_Activity.this,My_Feeds_Activity.class);
                                startActivity(intent1);
                            }
                        });
                    }
                    else
                    {
                        Log.i("Result","Failure");
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            Log.i("JSONCourseErr",jsonObject.toString());
                            try {
                                String jsonObject1 = jsonObject.getString("message");
                                Log.d("MessageCourse",jsonObject1);
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = Toast.makeText(Add_Event_Activity.this,jsonObject1.toString(), Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });
                            }
                            catch (JSONException e)
                            {

                                e.printStackTrace();
                                Log.i("errorsCourse",e.toString());
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.i("errorsCourse",e.toString());
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.d("ResponseImage", e.toString());
        }



    }


    //get date and time
    private void showDateTimeDialog(TextView start_date)
    {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        if (code == 1)
                        {
                            start_date.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                        else if (code == 2)
                        {
                            end_date.setText(simpleDateFormat.format(calendar.getTime()));
                        }


                        Log.i("DateStartVal",simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(Add_Event_Activity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(Add_Event_Activity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
}