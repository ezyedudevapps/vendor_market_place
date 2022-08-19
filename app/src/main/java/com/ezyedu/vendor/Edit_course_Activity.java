package com.ezyedu.vendor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.RealPathUtil;
import com.ezyedu.vendor.model.SeperateCourse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import okhttp3.ResponseBody;

public class Edit_course_Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    String session_id = null;
    private String Hashid;

    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> plist = new ArrayList<String>();
    RequestQueue requestQueue;
    TextView categories;
    Dialog dialog;
    Button addCourse;
    EditText Tittle,description,price,discount,duration;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int discount_available;
    TextView discTxt;
    ImageView imageView,image_picker,image_picker1,image_picker2;
    ProgressDialog progressDialog;

    RadioButton yes,no;


    String Course_tittle,Course_description,Course_price,Course_discount,Course_duration,Course_category_hash_id;
    String hash;

    String path;

    //to store image
    int img_code;

    ImageView cancel,cancel1,cancel2;

    //specific image id
    int image_id,image_id1,image_id2;

    String course_hash_id;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_);


        Hashid = getIntent().getStringExtra("id");

        requestQueue = CourseVolleySingleton.getInstance(Edit_course_Activity.this).getRequestQueue();

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);

        fetchCstegories();
        getCourse();

        categories = findViewById(R.id.txt_categories);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Edit_course_Activity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Edit_course_Activity.this,android.R.layout.simple_list_item_1,mylist);
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
        imageView = findViewById(R.id.add_course_image);



        //pick image from gallery...
        ActivityCompat.requestPermissions(Edit_course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        image_picker = findViewById(R.id.img_pk);
        image_picker.setOnClickListener(new View.OnClickListener() {
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
                    ActivityCompat.requestPermissions(Edit_course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });

        image_picker1 = findViewById(R.id.img_pk1);
        image_picker1.setOnClickListener(new View.OnClickListener() {
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
                    ActivityCompat.requestPermissions(Edit_course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });

        image_picker2 = findViewById(R.id.img_pk2);
        image_picker2.setOnClickListener(new View.OnClickListener() {
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
                    ActivityCompat.requestPermissions(Edit_course_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });



        // delete the course image.....

        cancel = findViewById(R.id.img_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dig = new AlertDialog.Builder(Edit_course_Activity.this).setTitle("Please Select").setMessage("Are you Sure Want to Delete this Image 1 ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressDialog = new ProgressDialog(Edit_course_Activity.this);
                                progressDialog.show();
                                progressDialog.setContentView(R.layout.progress_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                try {
                                    deleteImage();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

        cancel1 = findViewById(R.id.img_cancel1);
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dig = new AlertDialog.Builder(Edit_course_Activity.this).setTitle("Please Select").setMessage("Are you Sure Want to Delete this Image 2 ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressDialog = new ProgressDialog(Edit_course_Activity.this);
                                progressDialog.show();
                                progressDialog.setContentView(R.layout.progress_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                try {
                                    deleteImage1();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

        cancel2 = findViewById(R.id.img_cancel2);
        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dig = new AlertDialog.Builder(Edit_course_Activity.this).setTitle("Please Select").setMessage("Are you Sure Want to Delete this Image 3 ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressDialog = new ProgressDialog(Edit_course_Activity.this);
                                progressDialog.show();
                                progressDialog.setContentView(R.layout.progress_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                try {
                                    deleteImage2();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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


        radioGroup = findViewById(R.id.radio_price);

        addCourse = findViewById(R.id.add_course_btn);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(Edit_course_Activity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    try {
                        updateCourse();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });


        yes = findViewById(R.id.yes_price);
        no = findViewById(R.id.no_price);
    }





    private void deleteImage() throws JSONException {
        String url = base_app_url+"api/courses/image/delete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image_id",image_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {

                Log.i("ImagdelRes",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("Image deleted successfuly"))
                    {
                        recreate();
                        progressDialog.dismiss();
                        Toast.makeText(Edit_course_Activity.this, "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Edit_course_Activity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("ImgDelError",error.toString());
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

    private void deleteImage1() throws JSONException {
        String url = base_app_url+"api/courses/image/delete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image_id",image_id1);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {

                Log.i("ImagdelRes",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("Image deleted successfuly"))
                    {
                        recreate();
                        progressDialog.dismiss();
                        Toast.makeText(Edit_course_Activity.this, "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Edit_course_Activity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    private void deleteImage2() throws JSONException {
        String url = base_app_url+"/api/courses/image/delete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image_id",image_id2);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {

                Log.i("ImagdelRes",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("Image deleted successfuly"))
                    {
                        recreate();
                        progressDialog.dismiss();
                        Toast.makeText(Edit_course_Activity.this, "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Edit_course_Activity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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


    private void uploadImage1(String path)
    {
        try {
            progressDialog = new ProgressDialog(Edit_course_Activity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

            File file1 = new File(path);
            Log.e(MainActivity.class.getSimpleName(), "uploadImgage: " + file1.getPath());
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();
            if (file1 != null) {
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("course_hash_id",course_hash_id)
                        .addFormDataPart("image", file1.getName(), RequestBody.create(file1, MediaType.parse("multipart/form-data")))
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(base_app_url+"api/courses/image")
                        .addHeader("Authorization", session_id)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("Response", e.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                        Log.d("ResponseOnlyImage", response.body().string());
                        if (response.isSuccessful())
                        {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    recreate();
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(Edit_course_Activity.this, "Update  Success", Toast.LENGTH_SHORT);
                                    toast.show();
                                  /*  Intent intent1 = new Intent(Edit_course_Activity.this,Courses_Activity.class);
                                    startActivity(intent1);
                                   */
                                }
                            });
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.d("Response", e.toString());
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
                Context context = Edit_course_Activity.this;
                path = RealPathUtil.getRealPath(context,uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ImageView i1 = findViewById(R.id.add_course_image);
                i1.setImageBitmap(bitmap);

            }
           else if (img_code == 1)
            {
                Uri uri = data.getData();
                Context context = Edit_course_Activity.this;
                path = RealPathUtil.getRealPath(context,uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ImageView i2 = findViewById(R.id.add_course_image1);
                i2.setImageBitmap(bitmap);
            }
           else if (img_code == 2)
            {
                Uri uri = data.getData();
                Context context = Edit_course_Activity.this;
                path = RealPathUtil.getRealPath(context,uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ImageView i3 = findViewById(R.id.add_course_image2);
                i3.setImageBitmap(bitmap);
                uploadImage1(path);
            }
        }
    }



    private void updateCourse() throws JSONException {

        Course_tittle = Tittle.getText().toString();
        Log.i("CourseTTl",Course_tittle);
        Course_description = description.getText().toString();
        Course_price = price.getText().toString();
        Course_discount = discount.getText().toString();
        Course_duration = duration.getText().toString();
        Course_category_hash_id = hash;


        String url = base_app_url+"api/courses/"+ Hashid+"?_method=PUT";
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("title",Course_tittle);
        jsonObject1.put("description",Course_description);
        jsonObject1.put("course_category",Course_category_hash_id);
        jsonObject1.put("is_discount",discount_available);
        Double prc = Double.parseDouble(Course_price);
        jsonObject1.put("price",prc);
        if (discount_available == 1)
        {
            jsonObject1.put("discount_price",Course_discount);
        }
        jsonObject1.put("is_installment_available",0);
        jsonObject1.put("duration",Course_duration);
        jsonObject1.put("status","ACTIVE");

        Log.i("RequestJSOnCP",jsonObject1.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseUpdte",response.toString());
                if (response.has("id"));
                {
                    progressDialog.dismiss();
                    Toast.makeText(Edit_course_Activity.this, "Update Success", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Edit_course_Activity.this,Courses_Activity.class);
                    startActivity(intent1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Log.i("ErrorUpdate",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        //loadingDialog.DismisDialog();
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Edit_course_Activity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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

    private void getCourse()
    {
        String url = base_app_url+"api/courses/"+Hashid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    Log.i("ResponseIndividual",response.toString());
                    JSONObject jsonObject1 = response.getJSONObject("data");
                    course_hash_id = jsonObject1.getString("course_hash_id");
                    hash = jsonObject1.getString("category_hash_id");
                    String category_label = jsonObject1.getString("category_label");
                    categories.setText(category_label);
                    String course_title = jsonObject1.getString("course_title");
                    Tittle.setText(course_title);
                    String course_description = jsonObject1.getString("course_description");
                    description.setText(course_description);

                    JSONArray jsonArray = jsonObject1.getJSONArray("courses_image");
                    Log.i("jsonArrayLen", String.valueOf(jsonArray.length()));
                    if (jsonArray.length()== 1)
                    {

                        cancel.setVisibility(View.VISIBLE);
                        image_picker1.setVisibility(View.VISIBLE);
                        image_picker2.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        image_id = jsonObject.getInt("id");
                        String courses_image = jsonObject.getString("image");
                        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                        Glide.with(Edit_course_Activity.this).load(img_url_base+courses_image).into(((ImageView) findViewById(R.id.add_course_image)));
                    }
                    else if (jsonArray.length() == 2)
                    {
                        cancel.setVisibility(View.VISIBLE);
                        cancel1.setVisibility(View.VISIBLE);
                        image_picker2.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        image_id = jsonObject.getInt("id");
                            String courses_image = jsonObject.getString("image");
                            String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                            Glide.with(Edit_course_Activity.this).load(img_url_base+courses_image).into(((ImageView) findViewById(R.id.add_course_image)));

                        JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                        image_id1 = jsonObject2.getInt("id");
                        String courses_image1 = jsonObject2.getString("image");
                        Glide.with(Edit_course_Activity.this).load(img_url_base+courses_image1).into(((ImageView) findViewById(R.id.add_course_image1)));
                    }
                    else if (jsonArray.length() ==3)
                    {
                        cancel.setVisibility(View.VISIBLE);
                        cancel1.setVisibility(View.VISIBLE);
                        cancel2.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        image_id = jsonObject.getInt("id");
                        String courses_image = jsonObject.getString("image");
                        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                        Glide.with(Edit_course_Activity.this).load(img_url_base+courses_image).into(((ImageView) findViewById(R.id.add_course_image)));

                        JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                        image_id1 = jsonObject2.getInt("id");
                        String courses_image1 = jsonObject2.getString("image");
                        Glide.with(Edit_course_Activity.this).load(img_url_base+courses_image1).into(((ImageView) findViewById(R.id.add_course_image1)));

                        JSONObject jsonObject3 = jsonArray.getJSONObject(2);
                        image_id2 = jsonObject3.getInt("id");
                        String courses_image2 = jsonObject3.getString("image");
                        Glide.with(Edit_course_Activity.this).load(img_url_base+courses_image2).into(((ImageView) findViewById(R.id.add_course_image2)));
                    }
                    /*
                    String courses_image = jsonObject1.getString("courses_image");

                    String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                    Glide.with(Edit_course_Activity.this).load(img_url+courses_image).into(((ImageView) findViewById(R.id.add_course_image)));
                     */
                    String course_duration = jsonObject1.getString("course_duration");
                    duration.setText(course_duration);
                    Double initial_price = jsonObject1.getDouble("initial_price");
                    price.setText(String.valueOf(initial_price));
                    Double discount_price = jsonObject1.getDouble("discount_price");
                    discount.setText(String.valueOf(discount_price));
                    String start_date = jsonObject1.getString("start_date");

                    boolean is_discount = jsonObject1.getBoolean("is_discount");
                    if (is_discount)
                    {
                        yes.setChecked(true);
                        discount_available = 1;
                    }
                    else
                    {
                        no.setChecked(true);
                        discount_available = 0;
                        discTxt.setVisibility(View.GONE);
                        discount.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ErrorCatch",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("responseErrorcr",error.toString());
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


    public void checkButton(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
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
            discTxt.setVisibility(View.VISIBLE);
            discount.setVisibility(View.VISIBLE);
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
                            hash = hash_id;
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