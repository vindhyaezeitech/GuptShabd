package com.shabdamsdk.model.leaderboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLeaderboardRequest {
    @SerializedName("game_user_id")
    @Expose
    private String gameUserId;
    @SerializedName("app_id")
    @Expose
    private String app_id;

    public String getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(String gameUserId) {
        this.gameUserId = gameUserId;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }
}
