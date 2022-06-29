package com.guptshabd.notification;

import android.os.Bundle;

public class PushMessage {
    private String customMessage;
    private String header;
    private String message;
    private String deepLink;
    private String bigPictureUrl;
    private String largeIconUrl;
    private String streamAlertId;

    public String getCustomMessage() {
        return customMessage;
    }

    public String getStreamAlertId() {
        return streamAlertId;
    }

    public void setStreamAlertId(String streamAlertId) {
        this.streamAlertId = streamAlertId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    private String videoId;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    private Bundle bundle;


    public void setHeader(String header) {
        this.header = header;
    }
    public void setLargeIconUrl(String largeIconUrl) {
        this.largeIconUrl = largeIconUrl;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }


    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public String getCustomData() {
        return customMessage;
    }

    public String getHeader() {
        return header;
    }

    public void setBigPictureUrl(String bigPictureUrl) {
        this.bigPictureUrl = bigPictureUrl;
    }

    public String getBigPictureUrl() {
        return bigPictureUrl;
    }

    public String getLargeIconUrl() {
        return largeIconUrl;
    }
}
