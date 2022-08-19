package com.ezyedu.vendor.App;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.all.*;
import com.facebook.appevents.AppEventsLogger;


public class FaceExercises extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(this);
    }
}
