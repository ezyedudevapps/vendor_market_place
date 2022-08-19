package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Settings_Activity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView textView;

    String session_id = null;
    RequestQueue requestQueue;

    ProgressDialog progressDialog;

    LinearLayout edit_profile,change_pass,pyment_info,fs,tc,privacy_policy;
    Button logout;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_);

        drawerLayout = findViewById(R.id.drawer_layout);

        textView = findViewById(R.id.tittle_page);

        textView.setText("Settings");



        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        edit_profile = findViewById(R.id.edit_profile_linear);
        change_pass = findViewById(R.id.change_password_linear);
        pyment_info = findViewById(R.id.payment_info_linear);
        fs = findViewById(R.id.feedback_linear);
        tc = findViewById(R.id.tc_linear);
        privacy_policy = findViewById(R.id.privacy_policy_linear);

        logout = findViewById(R.id.logout_linear);

        fs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                String[] recepients = {"ezyeduteam@gmail.com"};
                intent1.putExtra(Intent.EXTRA_EMAIL,recepients);
                intent1.setType("text/plain");
                intent1.setPackage("com.google.android.gm");
                startActivity(intent1.createChooser(intent1,"send mail"));
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Settings_Activity.this,Edit_Users_profile.class);
                startActivity(intent1);
            }
        });

        pyment_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Settings_Activity.this,Payment_activity.class);
                startActivity(intent1);
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Settings_Activity.this,Privacy_policy_activity.class);
                startActivity(intent1);
            }
        });

        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Settings_Activity.this,Terms_Conditions_Activity.class);
                startActivity(intent1);
            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Settings_Activity.this,Change_password.class);
                startActivity(intent1);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Settings_Activity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                Logout();
            }
        });
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cl_activity",session_id);


    }

    private void Logout()
    {
        String url = base_app_url+"api/auth/logout";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(base_app_url+"api/auth/logout")
                .method("POST", body)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("ResponseAddCourse", response.body().string());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Settings_Activity.this, "Logged Out...", Toast.LENGTH_SHORT).show();

                            Intent intent1 = new Intent(Settings_Activity.this,Login_SignUp_page.class);
                            startActivity(intent1);

                            SharedPreferences sharedPreferences = getSharedPreferences("Session_id",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("session_val");
                            editor.apply();
                            finish();
                        }
                    }
                });
            }
        });





    }


    //message navigation menu....
    public void ClickMenu(View view)
    {
        MainActivity.openDrawer(drawerLayout);
    }
    public void Clickhome(View view)
    {
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public void Clicmessages(View view)
    {
        //recreate();
        MainActivity.redirectActivity(this,MessageActivity.class);
    }


    public void Clickfeeds(View view)
    {
        MainActivity.redirectActivity(this,Bank_Account_Activity.class);
    }

    public void Clickpayment(View view)
    {
        MainActivity.redirectActivity(this,Payment_activity.class);
        //  redirectActivity(this,);
    }

    public void ClickEvents(View view) {
        // redirectActivity(this,);
        MainActivity.redirectActivity(this, Events_Activity.class);
    }

    public void Clickusers(View view)
    {
        //  redirectActivity(this,);
        MainActivity.redirectActivity(this,Courses_Activity.class);
    }
    public void Clickreviews(View view)
    {
        MainActivity.redirectActivity(this,Reviews_Page.class);

    }
    public void Clickanalytics(View view)
    {
        Toast.makeText(this, "analytics", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clicksales(View view)
    {
        MainActivity.redirectActivity(this,Sales_activity.class);
    }
    public void Clickinstitute(View view)
    {
        MainActivity.redirectActivity(this,Edit_profile_activity.class);
        // redirectActivity(this,);
    }
    public void Clicksettings(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        // redirectActivity(this,);
    }
    public void Clickproducts(View view)
    {
        MainActivity.redirectActivity(this,Connection_Actiivity.class);

    }
    public void Clickfaq(View view)
    {
        Toast.makeText(this, "Faq", Toast.LENGTH_SHORT).show();
  ;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Intent intent1 = new Intent(this,MainActivity.class);
            startActivity(intent1);
        }
    }
}