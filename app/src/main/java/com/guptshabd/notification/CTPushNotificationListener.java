package com.guptshabd.notification;

import java.util.HashMap;

public interface CTPushNotificationListener {

    void onNotificationClickedPayloadReceived(HashMap<String, Object> payload);

}
