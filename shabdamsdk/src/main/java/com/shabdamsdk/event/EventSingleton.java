package com.shabdamsdk.event;

public class EventSingleton {

    private static EventSingleton eventSingleton;
    private CleverTapEvent cleverTapEvent;


    public static EventSingleton getInstance() {
        return eventSingleton;
    }

    public CleverTapEvent getCleverTapEvents() {
        if (cleverTapEvent == null) {
            return new CleverTapEvent();
        }
        return cleverTapEvent;
    }
}
