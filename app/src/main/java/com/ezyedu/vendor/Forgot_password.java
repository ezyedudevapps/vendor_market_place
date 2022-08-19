package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Forgot_password extends AppCompatActivity {
    TextView forgetpass;
    EditText mail;
    Button submit;
    String mail_id;
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
        setContentView(R.layout.activity_forgot_password);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        forgetpass = findViewById(R.id.forget_pass_btn);
        SpannableString content = new SpannableString("Forgot Password?");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        forgetpass.setText(content);

        mail = findViewById(R.id.get_mail);
        submit = findViewById(R.id.sbmit_btn);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mail_id = mail.getText().toString();
              if (TextUtils.isEmpty(mail_id))
              {
                  Toast.makeText(Forgot_password.this, "email should not be null", Toast.LENGTH_SHORT).show();
              }
              else
              {
                  try {
                      progressDialog = new ProgressDialog(Forgot_password.this);
                      progressDialog.show();
                      progressDialog.setContentView(R.layout.progress_dialog);
                      progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                      forgotpassword();
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
            }
        });
    }

    private void forgotpassword() throws JSONException {



        String url = base_app_url+"api/auth/forgot-password";


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email",mail_id)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(base_app_url+"api/auth/forgot-password")
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("ResponseAddCourse", response.body().string());
               ResponseBody a = response.body();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(Forgot_password.this, "OTP Has been sent to Your Registered E-Mail", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent1 = new Intent(Forgot_password.this,Reset_Password.class);
                            intent1.putExtra("mail",mail_id);
                            startActivity(intent1);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Forgot_password.this, "Invalid User ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}