package com.ezyedu.vendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.vendor.MainActivity;
import com.ezyedu.vendor.R;
import com.ezyedu.vendor.model.CourseVolleySingleton;
import com.ezyedu.vendor.model.Gallery_Images;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.GalleryHolder> {
    private Context context;
    private List<Gallery_Images> galleryImagesList = new ArrayList<>();
    String session_id = null;
    RequestQueue requestQueue;
    String base_app_url;
    String img_url_base;

    public GalleryImageAdapter(Context context, List<Gallery_Images> galleryImagesList) {
        this.context = context;
        this.galleryImagesList = galleryImagesList;
    }

    @NonNull
    @Override
    public GalleryImageAdapter.GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_img_adapter,parent,false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryImageAdapter.GalleryHolder holder, int position)
    {
        Gallery_Images galleryImages = galleryImagesList.get(position);
        int id = galleryImages.getId();
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+galleryImages.getImg()).into(holder.load_img);
        Log.i("Img_get_load",galleryImages.getImg());
        Log.i("imageIDs", String.valueOf(id));

        holder.cncl_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(String.valueOf(id)))
                {
                    try {
                        removeImg(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void removeImg(int id) throws JSONException {
        Log.i("RemoveImgID", String.valueOf(id));
        String url = base_app_url+"api/vendor/image/delete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image_id",id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseDel",response.toString());
                try {
                    String message = response.getString("message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("ErrorResponseDel",error.toString());
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

    @Override
    public int getItemCount() {
        return galleryImagesList == null ?0: galleryImagesList.size();
    }

    public class GalleryHolder extends RecyclerView.ViewHolder {
        ImageView load_img,cncl_img;

        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public GalleryHolder(@NonNull View itemView) {
            super(itemView);
            load_img = itemView.findViewById(R.id.image_glry);
            cncl_img = itemView.findViewById(R.id.img_cancel);

            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
            session_id = sharedPreferences.getString("session_val","");
            Log.i("session_new",session_id);

            requestQueue = CourseVolleySingleton.getInstance(context).getRequestQueue();

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("feeds_domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
