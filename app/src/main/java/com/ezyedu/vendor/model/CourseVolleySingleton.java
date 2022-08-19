package com.ezyedu.vendor.model;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class CourseVolleySingleton {
    private RequestQueue requestQueue;
    private static CourseVolleySingleton mInstance;

    private CourseVolleySingleton(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public static synchronized CourseVolleySingleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new CourseVolleySingleton(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue()
    {
        return requestQueue;
    }
}
