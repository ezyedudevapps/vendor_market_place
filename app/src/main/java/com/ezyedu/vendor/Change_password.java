package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Change_password extends AppCompatActivity {

    EditText currentPassword,newPassword,confirmNewPassword;
    Button changePassword;
    TextView textView;

    String session_id = null;
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
        setContentView(R.layout.activity_change_password);

        textView = findViewById(R.id.chng_pass);

        SpannableString content = new SpannableString("Change Password?");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        textView.setText(content);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);


        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        currentPassword = findViewById(R.id.current_pass);
        newPassword = findViewById(R.id.new_pass);
        confirmNewPassword = findViewById(R.id.confirm_new_pass);

        changePassword = findViewById(R.id.change_password_btn);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cl_activity",session_id);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curretPass = currentPassword.getText().toString();
                String NewPass = newPassword.getText().toString();
                String confPass = confirmNewPassword.getText().toString();

                if (TextUtils.isEmpty(curretPass) || TextUtils.isEmpty(NewPass) || TextUtils.isEmpty(confPass))
                {
                    Toast.makeText(Change_password.this, "Password Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   if (curretPass.length() <6)
                   {
                       Toast.makeText(Change_password.this, "Password Should contain atleast 6 characters", Toast.LENGTH_SHORT).show();
                   }
                   else if (NewPass.length()<6)
                   {
                       Toast.makeText(Change_password.this, "New Password Should contain atleast 6 characters", Toast.LENGTH_SHORT).show();
                   }
                   else if (confPass.length()<6)
                   {
                       Toast.makeText(Change_password.this, "New Password Should contain atleast 6 characters", Toast.LENGTH_SHORT).show();
                   }
                   else if (!NewPass.equals(confPass))
                   {
                       Toast.makeText(Change_password.this, "New Password and Conform Password Should be same...", Toast.LENGTH_SHORT).show();
                   }
                   else if (curretPass.equals(NewPass))
                   {
                       Toast.makeText(Change_password.this, "Current Password and New Password is same...", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       try {
                           progressDialog = new ProgressDialog(Change_password.this);
                           progressDialog.show();
                           progressDialog.setContentView(R.layout.progress_dialog);
                           progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                           ChangePassword(curretPass,NewPass,confPass);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }

                }
            }
        });
    }

    private void ChangePassword(String curretPass, String newPass, String confPass) throws JSONException {

        String url = base_app_url+"api/auth/change-password";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("current_password",curretPass);
        jsonObject.put("new_password",newPass);
        jsonObject.put("new_password_confirmation",confPass);

        Log.i("JSONChangePass",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseChangePass",response.toString());
                if (response.has("id"))
                {
                    progressDialog.dismiss();
                    Toast.makeText(Change_password.this, "Password Changed Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Change_password.this,Login_page.class);
                    startActivity(intent1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorChangePass", String.valueOf(error));
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Change_password.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
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
}