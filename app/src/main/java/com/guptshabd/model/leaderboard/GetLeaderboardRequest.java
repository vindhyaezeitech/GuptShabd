package com.guptshabd.model.leaderboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLeaderboardRequest {
    @SerializedName("game_user_id")
    @Expose
    private String gameUserId;

    public String getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(String gameUserId) {
        this.gameUserId = gameUserId;
    }
}
