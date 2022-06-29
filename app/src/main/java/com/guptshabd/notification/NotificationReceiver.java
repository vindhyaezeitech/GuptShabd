package com.guptshabd.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guptshabd.GameApplication;
import com.shabdamsdk.ShabdamSplashActivity;


public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = null;
        intent1 = new Intent(GameApplication.getInstance(), ShabdamSplashActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent1);

       /* if (String.valueOf(intent.getData()).contains(GameApplication.getInstance().getString(R.string.app_name))) {
            intent1 = new Intent(GameApplication.getInstance(), ShabdamSplashActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent1.setData(intent.getData());
            intent1.putExtras(intent.getExtras());
            context.startActivity(intent1);
        }*/
    }
}
