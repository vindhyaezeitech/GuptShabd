package com.guptshabd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.shabdamsdk.ShabdamSplashActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShabdamSplashActivity.startShabdam(TestActivity.this,BuildConfig.APPLICATION_ID, "");

            }
        }, 5000);
    }
}