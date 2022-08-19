package com.ezyedu.vendor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.RealPathUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Add_Course_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    String session_id = null;
    String [] item;
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> plist = new ArrayList<String>();
    RequestQueue requestQueue;
    TextView categories;
    Dialog dialog;
    Button addCourse;
    EditText Tittle,description,price,discount,duration;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int discount_available = 1;
    TextView discTxt;
    ImageView imageView,imageview1,imageview2;
    ProgressDialog progressDialog;

    ImageView i,i1,i2;

    String Course_tittle,Course_description,Course_price,Course_discount,Course_duration,Course_category_hash_id;
    String hash;
    File file;
    String path;
    int img_code;

    String path1;
    String path2;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__course_);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);

        fetchCstegories();

        categories = findViewById(R.id.txt_categories);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Add_Course_Activity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_Course_Activity.this,android.R.layout.simple_list_item_1,mylist);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        categories.setText(adapter.getItem(position));
                        int pos = position;
                        Log.i("catPos", String.valueOf(pos));
                        dialog.dismiss();
                        hash = plist.get(position);
                        Log.i("Hash_id_val",hash);
                    }
                });
            }
        });


        Tittle = findViewById(R.id.add_course_tittle);
        description = findViewById(R.id.add_course_description);
        price = findViewById(R.id.add_course_price);
        discount = findViewById(R.id.add_course_dicount_price);
        duration = findViewById(R.id.add_course_duration);
        discTxt = findViewById(R.id.dtext);


        ActivityCompat.requestPermissions(Add_Course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        imageView = findViewById(R.id.add_course_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_code = 0;
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {

                    Intent intent1= new Intent();
                    intent1.setType("image/*");
                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent1,10);
                }
                else
                {
                    ActivityCompat.requestPermissions(Add_Course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });
        imageview1 = findViewById(R.id.add_course_image1);
        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_code = 1;
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {

                    Intent intent1= new Intent();
                    intent1.setType("image/*");
                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent1,10);
                }
                else
                {
                    ActivityCompat.requestPermissions(Add_Course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });
        imageview2 = findViewById(R.id.add_course_image2);
        imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_code = 2;
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {

                    Intent intent1= new Intent();
                    intent1.setType("image/*");
                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent1,10);
                }
                else
                {
                    ActivityCompat.requestPermissions(Add_Course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });


        radioGroup = findViewById(R.id.radio_price);

        addCourse = findViewById(R.id.add_course_btn);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Course_tittle = Tittle.getText().toString();
                Course_description = description.getText().toString();
                Course_price = price.getText().toString();
                Course_discount = discount.getText().toString();
                Course_duration = duration.getText().toString();
                Course_category_hash_id = hash;
                if (TextUtils.isEmpty(Course_tittle))
                {
                    Toast.makeText(Add_Course_Activity.this, "Tittle Should not be empty", Toast.LENGTH_SHORT).show();
                }
               else if (TextUtils.isEmpty(Course_description))
                {
                    Toast.makeText(Add_Course_Activity.this, "Description Should not be empty", Toast.LENGTH_SHORT).show();
                }
               else if (TextUtils.isEmpty(Course_price))
                {
                    Toast.makeText(Add_Course_Activity.this, "Price Should not be empty", Toast.LENGTH_SHORT).show();
                }
               else if (TextUtils.isEmpty(Course_duration))
                {
                    Toast.makeText(Add_Course_Activity.this, "Duration Should not be empty", Toast.LENGTH_SHORT).show();
                }
               else if (path == null)
                {
                    Toast.makeText(Add_Course_Activity.this, "Select Course image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (path1 != null && path2 == null)
                    {
                        AddCourse1(path,path1);
                        progressDialog = new ProgressDialog(Add_Course_Activity.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        Log.i("TotalAddingImage","two");
                    }
                    else if (path1 != null && path2 != null)
                    {
                        AddCourse2(path,path1,path2);
                        progressDialog = new ProgressDialog(Add_Course_Activity.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        Log.i("TotalAddingImage","three");
                    }
                    else
                    {
                        AddCourse(path);
                        progressDialog = new ProgressDialog(Add_Course_Activity.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        Log.i("TotalAddingImage","one");
                    }

                }

            }
        });
    }

    private void AddCourse2(String path, String path1, String path2)
    {
        try {
            //file 0
            File file = new File(path);
            Log.i("FilePath", String.valueOf(file));
            Log.i("Path",path);
            Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file.getPath());

            //file 1
            File file1 = new File(path1);
            Log.i("firstfilepath", String.valueOf(file1));
            Log.i("firstpath",path1);
            Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file1.getPath());

            //file 2
            File file2 = new File(path2);
            Log.i("firstfilepath", String.valueOf(file2));
            Log.i("firstpath",path2);
            Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file2.getPath());


            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();
            if (file != null) {
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title",Course_tittle)
                        .addFormDataPart("description",Course_description)
                        .addFormDataPart("course_category",hash)
                        .addFormDataPart("is_discount", String.valueOf(discount_available))
                        .addFormDataPart("price",Course_price)
                        .addFormDataPart("discount_price",Course_discount)
                        .addFormDataPart("is_installment_available","0")
                        .addFormDataPart("duration",Course_duration)
                        .addFormDataPart("images[0]", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                        .addFormDataPart("images[1]", file1.getName(), RequestBody.create(file1, MediaType.parse("multipart/form-data")))
                        .addFormDataPart("images[2]", file2.getName(), RequestBody.create(file2, MediaType.parse("multipart/form-data")))
                        .build();
                Log.i("Lg",file.getName());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(base_app_url+"api/courses")
                        .addHeader("Authorization",session_id)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("ResponseCourseFailure", e.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                        Log.d("ResponseAddCourse",response.peekBody(2048).string());
                        String message =  response.peekBody(2048).string();

                        if (response.isSuccessful())
                        {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(Add_Course_Activity.this, "Courses Added Success", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent intent1 = new Intent(Add_Course_Activity.this,Courses_Activity.class);
                                    startActivity(intent1);
                                }
                            });
                        }

                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(message);
                                Log.i("JSONCourseErr",jsonObject.toString());
                                try {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("errors");
                                    Log.d("MessageCourse",jsonObject1.toString());
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast toast = Toast.makeText(Add_Course_Activity.this,jsonObject1.toString(), Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    });
                                }
                                catch (JSONException e)
                                {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                    Log.i("errorsCourse",e.toString());
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                                Log.i("errorsCourse",e.toString());
                            }
                        }


                    }
                });
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.d("ResponseImage", e.toString());
        }
    }

    private void AddCourse1(String path, String path1)
    {
        try {
            //file 0
            File file = new File(path);
            Log.i("FilePath", String.valueOf(file));
            Log.i("Path",path);
            Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file.getPath());

            //file 1
            File file1 = new File(path1);
            Log.i("firstfilepath", String.valueOf(file1));
            Log.i("firstpath",path1);
            Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file1.getPath());

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();
            if (file != null) {
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title",Course_tittle)
                        .addFormDataPart("description",Course_description)
                        .addFormDataPart("course_category",hash)
                        .addFormDataPart("is_discount", String.valueOf(discount_available))
                        .addFormDataPart("price",Course_price)
                        .addFormDataPart("discount_price",Course_discount)
                        .addFormDataPart("is_installment_available","0")
                        .addFormDataPart("duration",Course_duration)
                        .addFormDataPart("images[0]", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                        .addFormDataPart("images[1]", file1.getName(), RequestBody.create(file1, MediaType.parse("multipart/form-data")))
                        .build();
                Log.i("Lg",file.getName());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(base_app_url+"api/courses")
                        .addHeader("Authorization",session_id)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("ResponseCourseFailure", e.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                        Log.d("ResponseAddCourse",response.peekBody(2048).string());
                        String message =  response.peekBody(2048).string();

                        if (response.isSuccessful())
                        {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(Add_Course_Activity.this, "Courses Added Success", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent intent1 = new Intent(Add_Course_Activity.this,Courses_Activity.class);
                                    startActivity(intent1);
                                }
                            });
                        }

                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(message);
                                Log.i("JSONCourseErr",jsonObject.toString());
                                try {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("errors");
                                    Log.d("MessageCourse",jsonObject1.toString());
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast toast = Toast.makeText(Add_Course_Activity.this,jsonObject1.toString(), Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    });
                                }
                                catch (JSONException e)
                                {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                    Log.i("errorsCourse",e.toString());
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                                Log.i("errorsCourse",e.toString());
                            }
                        }


                    }
                });
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.d("ResponseImage", e.toString());
        }
    }


    private void AddCourse(String path)
    {
        try {
            File file = new File(path);
            Log.i("FilePath", String.valueOf(file));
            Log.i("Path",path);
            Log.e(Edit_profile_activity.class.getSimpleName(), "uploadImgage: " + file.getPath());
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();
            if (file != null) {
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title",Course_tittle)
                        .addFormDataPart("description",Course_description)
                        .addFormDataPart("course_category",hash)
                        .addFormDataPart("is_discount", String.valueOf(discount_available))
                        .addFormDataPart("price",Course_price)
                        .addFormDataPart("discount_price",Course_discount)
                        .addFormDataPart("is_installment_available","0")
                        .addFormDataPart("duration",Course_duration)
                        .addFormDataPart("images[0]", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                        .build();
                Log.i("Lg",file.getName());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(base_app_url+"api/courses")
                        .addHeader("Authorization",session_id)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("ResponseCourseFailure", e.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                        Log.d("ResponseAddCourse",response.peekBody(2048).string());
                        String message =  response.peekBody(2048).string();

                        if (response.isSuccessful())
                            {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast toast = Toast.makeText(Add_Course_Activity.this, "Courses Added Success", Toast.LENGTH_SHORT);
                                        toast.show();
                                        Intent intent1 = new Intent(Add_Course_Activity.this,Courses_Activity.class);
                                        startActivity(intent1);
                                    }
                                });
                            }

                            else
                            {
                                try {
                                     JSONObject jsonObject = new JSONObject(message);
                                     Log.i("JSONCourseErr",jsonObject.toString());
                                     try {
                                         JSONObject jsonObject1 = jsonObject.getJSONObject("errors");
                                         Log.d("MessageCourse",jsonObject1.toString());
                                         new Handler(Looper.getMainLooper()).post(new Runnable() {
                                             @Override
                                             public void run() {
                                                 progressDialog.dismiss();
                                                 Toast toast = Toast.makeText(Add_Course_Activity.this,jsonObject1.toString(), Toast.LENGTH_LONG);
                                                 toast.show();
                                             }
                                         });
                                     }
                                  catch (JSONException e)
                                  {
                                      progressDialog.dismiss();
                                      e.printStackTrace();
                                      Log.i("errorsCourse",e.toString());
                                  }
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                    Log.i("errorsCourse",e.toString());
                                }
                            }
                    }
                });
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.d("ResponseImage", e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK)
        {
            if (img_code == 0)
            {
                Uri uri = data.getData();
                Context context = Add_Course_Activity.this;
                path = RealPathUtil.getRealPath(context,uri);
                Log.i("pathImg",path);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bitmap);
                //      Glide.with(this).load(path).into(((ImageView) findViewById(R.id.add_course_image)));
            }
            if (img_code == 1)
            {
                Uri uri = data.getData();
                Context context = Add_Course_Activity.this;
                path1 = RealPathUtil.getRealPath(context,uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path1);
                imageview1.setImageBitmap(bitmap);
                //    Glide.with(this).load(path1).into(((ImageView) findViewById(R.id.add_course_image1)));
            }
            if (img_code == 2)
            {
                Uri uri = data.getData();
                Context context = Add_Course_Activity.this;
                path2 = RealPathUtil.getRealPath(context,uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path2);
                imageview2.setImageBitmap(bitmap);
                //  Glide.with(this).load(path2).into(((ImageView) findViewById(R.id.add_course_image2)));
            }
        }
             //   uploadImgage(getFilePath(this, data.getData()));
          //  file = new File(Objects.requireNonNull(getFilePath(this, data.getData())));


    }

    public void checkButton(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
       // Toast.makeText(this, ""+radioButton.getText(), Toast.LENGTH_SHORT).show();
        String message = (String) radioButton.getText();
        if (message.equals("No"))
        {
            discount_available =0;
            discTxt.setVisibility(View.GONE);
            discount.setVisibility(View.GONE);
        }
        else if (message.equals("Yes"))
        {
            discount_available =1;
        }

    }

    private void fetchCstegories()
    {
        String url = base_app_url+"api/courses/categories";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ResponseCategory",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String hash_id = jsonObject1.getString("hash_id");
                            String label = jsonObject1.getString("label");
                            mylist.add(label);
                            plist.add(hash_id);
                        }
                        Log.i("ArrayLabel", String.valueOf(mylist.size()));
                        Log.i("ArrayLabelVal", String.valueOf(mylist));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}