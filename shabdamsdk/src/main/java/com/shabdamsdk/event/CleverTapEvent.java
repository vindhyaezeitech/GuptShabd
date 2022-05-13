package com.shabdamsdk.event;

import android.os.Bundle;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.guptshabd.GameApplication;

import java.util.HashMap;
import java.util.Map;

public class CleverTapEvent {
    private CleverTapAPI clevertapDefaultInstance;
    private FirebaseAnalytics firebaseAnalytics;

    public void initializedCleverTap() {
        if (clevertapDefaultInstance == null) {
            clevertapDefaultInstance = CleverTapAPI.getDefaultInstance();
        }
    }

    public void initializedFirebase() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance();
        }
    }

    public CleverTapEvent() {
        initializedFirebase();
        initializedCleverTap();
    }

    public void createOnlyEvent(String event_name) {
        if (clevertapDefaultInstance != null){
            HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
            prodViewedAction.put(CleverTapEventConstants.DEVICE_TYPE, CleverTapEventConstants.ANDROID);
            clevertapDefaultInstance.pushEvent(event_name, prodViewedAction);
            Log.d("CLEVERTAP_EVENTS", event_name );

        }
        if(firebaseAnalytics != null){
            Bundle bundle = new Bundle();
            bundle.putString(CleverTapEventConstants.DEVICE_TYPE, CleverTapEventConstants.ANDROID);
            firebaseAnalytics.logEvent(event_name, bundle);
        }
    }


    public void iterateUsingLambda(String event_name, HashMap<String, Object> map) {
        Bundle bundle = new Bundle();
        // map.forEach((k, v) -> bundle.putString(k, (String) v));

        for (Map.Entry<String,Object> entry : map.entrySet()){
            bundle.putString(entry.getKey(), String.valueOf(entry.getValue()));
        }

        bundle.putString(CleverTapEventConstants.DEVICE_TYPE, CleverTapEventConstants.ANDROID);

        firebaseAnalytics.logEvent(event_name, bundle);
    }
}
