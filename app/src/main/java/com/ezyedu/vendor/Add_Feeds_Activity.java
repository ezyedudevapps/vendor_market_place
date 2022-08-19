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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.RealPathUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class
Add_Feeds_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;


    String session_id = null;
    RequestQueue requestQueue;

    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> plist = new ArrayList<String>();

    TextView categories;
    Dialog dialog;
    String hash_id_category;
    Button button;

    EditText tittle,description;
    String path;

    ImageView imageView;

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
        setContentView(R.layout.activity_add__feeds_);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        fetchIdeaCategory();



        categories = findViewById(R.id.txt_categories);

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Add_Feeds_Activity.this);
                dialog.setContentView(R.layout.dialog_all_feed_category_spinner);
                dialog.getWindow().setLayout(650,800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_Feeds_Activity.this,android.R.layout.simple_list_item_1,mylist);
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
                        hash_id_category = plist.get(position);
                        Log.i("Hash_id_val",hash_id_category);


                    }
                });
            }
        });

        ActivityCompat.requestPermissions(Add_Feeds_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        tittle = findViewById(R.id.add_course_tittle);
        description = findViewById(R.id.add_course_description);

        imageView = findViewById(R.id.add_course_image);
        imageView.setOnClickListener(new View.OnClickListener() {
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
                    ActivityCompat.requestPermissions(Add_Feeds_Activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });

        button = findViewById(R.id.add_course_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ttl = tittle.getText().toString();
                String desc = description.getText().toString();
                if (TextUtils.isEmpty(ttl))
                {
                    Toast.makeText(Add_Feeds_Activity.this, "Tittle Should not be Empty", Toast.LENGTH_SHORT).show();
                }
                else  if (TextUtils.isEmpty(desc))
                {
                    Toast.makeText(Add_Feeds_Activity.this, "Description Should not be empty", Toast.LENGTH_SHORT).show();
                }
                else  if (TextUtils.isEmpty(hash_id_category))
                {
                    Toast.makeText(Add_Feeds_Activity.this, "Select the Category", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(path))
                {
                    Toast.makeText(Add_Feeds_Activity.this, "Please Select the Image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addFeeds();
                    progressDialog = new ProgressDialog(Add_Feeds_Activity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            Context context = Add_Feeds_Activity.this;
            path = RealPathUtil.getRealPath(context,uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void addFeeds()
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
                    .addFormDataPart("ideas_category",hash_id_category)
                    .build();
            Log.i("Lg",file.getName());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(base_app_url+"api/ideas")
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
                                Toast toast = Toast.makeText(Add_Feeds_Activity.this, "Feeds Added Success", Toast.LENGTH_SHORT);
                                toast.show();
                              //  Intent intent1 = new Intent(Add_Feeds_Activity.this,My_Feeds_Activity.class);
                                //startActivity(intent1);
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
                                        Toast toast = Toast.makeText(Add_Feeds_Activity.this,jsonObject1.toString(), Toast.LENGTH_LONG);
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

    private void fetchIdeaCategory()
    {
        String url = base_app_url+"api/ideas/category?all=1";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                for (int i = 0; i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String label = jsonObject.getString("label");
                        String hash_id = jsonObject.getString("hash_id");
                        mylist.add(label);
                        plist.add(hash_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
        requestQueue.add(jsonArrayRequest);
    }
}