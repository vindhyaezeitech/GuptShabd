package com.guptshabd.model.statistics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("game_user_id")
    @Expose
    private Integer gameUserId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("win")
    @Expose
    private String win;
    @SerializedName("loss")
    @Expose
    private String loss;
    @SerializedName("played")
    @Expose
    private String played;
    @SerializedName("current_streak")
    @Expose
    private String currentStreak;
    @SerializedName("max_streak")
    @Expose
    private String maxStreak;
    @SerializedName("last_game_status")
    @Expose
    private String lastGameStatus;

    public Integer getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(Integer gameUserId) {
        this.gameUserId = gameUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public String getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(String currentStreak) {
        this.currentStreak = currentStreak;
    }

    public String getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(String maxStreak) {
        this.maxStreak = maxStreak;
    }

    public String getLastGameStatus() {
        return lastGameStatus;
    }

    public void setLastGameStatus(String lastGameStatus) {
        this.lastGameStatus = lastGameStatus;
    }
}
