package com.shabdamsdk.model.game_user_update_token;

import com.google.gson.annotations.SerializedName;

public class UpdateUserTokenRequest{

	@SerializedName("game_user_id")
	private String gameUserId;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("device_type")
	private String deviceType;

	public void setGameUserId(String gameUserId){
		this.gameUserId = gameUserId;
	}

	public String getGameUserId(){
		return gameUserId;
	}

	public void setDeviceToken(String deviceToken){
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public void setDeviceType(String deviceType){
		this.deviceType = deviceType;
	}

	public String getDeviceType(){
		return deviceType;
	}
}