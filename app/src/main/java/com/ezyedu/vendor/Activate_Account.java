package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.ezyedu.vendor.model.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class Activate_Account extends AppCompatActivity {

    TextView textView,t2;
    EditText editText;
    Button button;
    RequestQueue requestQueue;
    String mail;
    LoadingDialog loadingDialog;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate__account);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        textView = findViewById(R.id.mail_acc);
        editText=findViewById(R.id.Code_get);
        button=findViewById(R.id.ValidateBtn);
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        t2 = findViewById(R.id.acc_activate);
        SpannableString content = new SpannableString("Activate Account");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        t2.setText(content);


        loadingDialog = new LoadingDialog(Activate_Account.this);
        Intent intent = getIntent();
        mail = intent.getStringExtra("mail_id");
        Log.i("mailpassed",mail);
        textView.setText("Please enter the code that you have received in "+mail+" to activate your account");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String temp_mail = "shabari.abimanu1@gmail.com";
                String code = editText.getText().toString();
                if (!TextUtils.isEmpty(code))
                {
                    try {
                        loadingDialog.StartLoadingDialog();
                        ActivateData(mail,
                                code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(Activate_Account.this, "Please enter the Code", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void ActivateData(String mail, String code) throws JSONException {
        String url = base_app_url+"api/auth/active";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email",mail);
        jsonObject.put("code",code);
        Log.i("jsontopass",jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                loadingDialog.DismisDialog();
                Log.i("ActivateSucccess",response.toString());
                Toast.makeText(Activate_Account.this, "Account Activated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activate_Account.this,Login_page.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loadingDialog.DismisDialog();
                Log.i("ActivateFailure",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Activate_Account.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}