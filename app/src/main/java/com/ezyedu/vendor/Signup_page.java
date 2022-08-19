package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup_page extends AppCompatActivity {

    EditText username,email,password,conform_password,referral_code;
    Button RegisterButton;
    RequestQueue requestQueue;
    TextView sign_in_back;

    LoadingDialog loadingDialog;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        username = findViewById(R.id.register_user_get);
        email = findViewById(R.id.register_mail_get);
        password = findViewById(R.id.register_pass_get);
        conform_password = findViewById(R.id.register_conf_pass_get);
        referral_code = findViewById(R.id.ref_code_get);

        RegisterButton = findViewById(R.id.register_btn);

        sign_in_back = findViewById(R.id.sign_up_back);
        SpannableString content1 = new SpannableString("Sign-In");
        content1.setSpan(new UnderlineSpan(),0,content1.length(),0);
        sign_in_back.setText(content1);
        sign_in_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Signup_page.this,Login_page.class);
                startActivity(intent1);
            }
        });


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        loadingDialog = new LoadingDialog(Signup_page.this);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RegisterUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void RegisterUser() throws JSONException {
        String user_name = username.getText().toString();
        String e_mail = email.getText().toString();
        String pass_word = password.getText().toString();
        String conf_password = conform_password.getText().toString();
        String role = "asd123";

        if (TextUtils.isEmpty(user_name))
        {
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(e_mail))
        {
            Toast.makeText(this, "Please Enter Email id", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass_word))
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if (!(pass_word.equals(conf_password)))
        {
            Toast.makeText(this, "Password Does Not match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingDialog.StartLoadingDialog();
            RegosterAccount(user_name,e_mail,pass_word,role);
        }

    }

    private void RegosterAccount(String user_name, String e_mail, String pass_word, String role) throws JSONException {
        String url = base_app_url+"api/auth/register";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", e_mail);
        jsonObject.put("password", pass_word);
        jsonObject.put("username", user_name);
        jsonObject.put("role", role);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("RegisterSuccess", response.toString());
                loadingDialog.DismisDialog();
                Toast.makeText(Signup_page.this, "Otp has been Sent to Your Registered E-mail...", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(Signup_page.this,Activate_Account.class);
                intent1.putExtra("mail_id",e_mail);
                startActivity(intent1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        loadingDialog.DismisDialog();
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Signup_page.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}