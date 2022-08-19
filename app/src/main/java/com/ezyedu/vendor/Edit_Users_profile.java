package com.ezyedu.vendor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Edit_Users_profile extends AppCompatActivity {



    String path;

    String session_id = null;
    EditText username,name,email,phone;
    Button button;
    ImageView imageView,img_picker;
    RequestQueue requestQueue;
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
        setContentView(R.layout.activity_edit__users_profile);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        ActivityCompat.requestPermissions(Edit_Users_profile.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        username = findViewById(R.id.user_name_update);
        name = findViewById(R.id.name_update);
        email = findViewById(R.id.email_update);
        phone = findViewById(R.id.phone_update);
        button = findViewById(R.id.update_user);
        imageView = findViewById(R.id.image_to_update);
        img_picker = findViewById(R.id.img_pk);
        img_picker.setOnClickListener(new View.OnClickListener() {
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
                    ActivityCompat.requestPermissions(Edit_Users_profile.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });


        getData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressDialog = new ProgressDialog(Edit_Users_profile.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    if(path == null)
                    {
                        updateData();
                    }
                    else
                    {
                        uploadImgage(path);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
            Context context = Edit_Users_profile.this;
            path = RealPathUtil.getRealPath(context,uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
         //   uploadImgage(path);
        }

    }

    private void uploadImgage(String path)
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
            if (file != null) {
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                        .addFormDataPart("username",username.getText().toString())
                        .addFormDataPart("name",name.getText().toString())
                        .addFormDataPart("email",email.getText().toString())
                        .addFormDataPart("phone", phone.getText().toString())
                        .build();
                Log.i("Lg",file.getName());
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(base_app_url+"api/vendor/edit/user")
                        .addHeader("Authorization", session_id)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        progressDialog.dismiss();
                        Log.d("ResponseImgFailure", e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                        Log.d("ResponseImg", response.body().string());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(Edit_Users_profile.this, "Profile Picture Uploaded Successfully", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                });
            }
        } catch (Exception e) {
            Log.d("ResponseImage", e.toString());
        }
    }



    private void updateData() throws JSONException {
        String url =base_app_url+"api/vendor/edit/user";
        JSONObject jsonObject = new JSONObject();
        String u = username.getText().toString();
        if (!TextUtils.isEmpty(u))
        {
            jsonObject.put("username", u);
        }
        String n = name.getText().toString();
        if (!TextUtils.isEmpty(n))
        {
            jsonObject.put("name", n);
        }
        String m = email.getText().toString();
        if (!TextUtils.isEmpty(m))
        {
            jsonObject.put("email",m);
        }
        String p = phone.getText().toString();
        if (!TextUtils.isEmpty(p))
        {
            jsonObject.put("phone",p);
        }

        Log.i("JSonUpdate",jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ResponseUpdate",response.toString());
                progressDialog.dismiss();
                Toast.makeText(Edit_Users_profile.this, "Update Success", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Edit_Users_profile.this,MainActivity.class);
                startActivity(intent1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {

                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        if(jsonObject2.has("username"))
                        {
                            Toast.makeText(Edit_Users_profile.this,jsonObject2.getString("username"), Toast.LENGTH_SHORT).show();
                        }
                        else if(jsonObject2.has("name"))
                        {
                            Toast.makeText(Edit_Users_profile.this,jsonObject2.getString("name"), Toast.LENGTH_SHORT).show();
                        }
                        else if(jsonObject2.has("email"))
                        {
                            Toast.makeText(Edit_Users_profile.this,jsonObject2.getString("email"), Toast.LENGTH_SHORT).show();
                        }
                        else if(jsonObject2.has("phone"))
                        {
                            Toast.makeText(Edit_Users_profile.this,jsonObject2.getString("phone"), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Edit_Users_profile.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                        }

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

    private void getData()
    {
        String url = base_app_url+"api/vendor/info/user";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseEditUser",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONObject jsonObject = response.getJSONObject("data");
                        if (!jsonObject.isNull("image"))
                        {
                            String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                            Glide.with(Edit_Users_profile.this).load(img_url_base+jsonObject.getString("image")).into(imageView);
                        }
                        else
                        {
                            Glide.with(Edit_Users_profile.this).load(R.drawable.empty_profile_picture).into(imageView);
                        }
                        if (!jsonObject.isNull("username"))
                        {
                            username.setText(jsonObject.getString("username"));
                        }
                        if (!jsonObject.isNull("name"))
                        {
                            name.setText(jsonObject.getString("name"));
                        }
                        if (!jsonObject.isNull("email"))
                        {
                            email.setText(jsonObject.getString("email"));
                        }
                        if (!jsonObject.isNull("phone"))
                        {
                            phone.setText(jsonObject.getString("phone"));
                        }
                    }
                    else
                    {
                        Toast.makeText(Edit_Users_profile.this, "Error...Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
             Log.i("ResponseErrorEdit",error.toString());
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