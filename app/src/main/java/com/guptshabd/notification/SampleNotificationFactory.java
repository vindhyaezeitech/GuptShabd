package com.guptshabd.notification;

import static com.guptshabd.notification.Constants.NOTIFICATION_CHANNEL_ID;
import static com.guptshabd.notification.Constants.NOTIFICATION_CHANNEL_NAME;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.guptshabd.GameApplication;
import com.guptshabd.R;
import com.shabdamsdk.ShabdamSplashActivity;

/**
 * Check if notification id is comming if yes then update if not then do nothing
 * Check which type of notification is live or video
 */

public class SampleNotificationFactory {
    private static final int notify_id = 1;

    @SuppressLint("NotificationTrampoline")
    public static void onGenerateNotification(@NonNull PushMessage pushMessage) {
// Toast.makeText(NewsTakApplication.getInstance(), "This is notification", Toast.LENGTH_LONG).show();
        Log.d("batman-call", "Notification Received");

     /*   Intent intent = null;
        intent = new Intent(GameApplication.getInstance(), NotificationReceiver.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.setData(Uri.parse(pushMessage.getDeepLink()));
        //intent.putExtras(pushMessage.getBundle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(GameApplication.getInstance(), notify_id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        Intent newIntent = new Intent(GameApplication.getInstance(), NotificationReceiver.class);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(GameApplication.getInstance(), NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(com.shabdamsdk.R.drawable.app_logo);
        mBuilder.setColor(ContextCompat.getColor(GameApplication.getInstance(), android.R.color.white));
        mBuilder.setContentIntent(PendingIntent.getBroadcast(GameApplication.getInstance(), notify_id, newIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        mBuilder.setContentTitle(pushMessage.getHeader())
                .setSmallIcon(R.drawable.app_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText(pushMessage.getMessage())
                .setAutoCancel(true);
        mBuilder.build().flags |= Notification.FLAG_ONGOING_EVENT;
        mBuilder.setWhen(System.currentTimeMillis());

        NotificationManager mNotificationManager = (NotificationManager) GameApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            mBuilder.setGroup(String.valueOf(System.currentTimeMillis())).setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // if (notifyMgr != null)
        //notify_id=notify_id+1;
        mNotificationManager.notify((int) (System.currentTimeMillis() % Integer.MAX_VALUE), mBuilder.build());

    }
}