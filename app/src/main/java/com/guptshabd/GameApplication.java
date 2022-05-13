package com.guptshabd;

import android.app.Application;

import com.shabdamsdk.event.EventSingleton;

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
        super.onCreate();
        application = (GameApplication) getApplicationContext();

        initSingleton();
    }

    private void initSingleton() {
        EventSingleton.getInstance();
    }
}
