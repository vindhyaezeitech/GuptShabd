package com.guptshabd;

import android.app.Application;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.guptshabd.notification.CTPushNotificationListener;

import java.util.HashMap;

public class GameApplication extends Application {


    private static GameApplication application;




    public static GameApplication getInstance(){
        return application;
    }

    public int getHelpCount() {
        return helpCount;
    }

    public void setHelpCount(int helpCount) {
        this.helpCount = helpCount;
    }

    private int helpCount;

    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();
        application = (GameApplication) getApplicationContext();
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
    }

}
