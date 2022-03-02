
package com.shabdamsdk.model.gamesubmit;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitGameRequest implements Parcelable
{

    @SerializedName("game_user_id")
    @Expose
    private String gameUserId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("game_status")
    @Expose
    private String gameStatus;
    public final static Creator<SubmitGameRequest> CREATOR = new Creator<SubmitGameRequest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SubmitGameRequest createFromParcel(android.os.Parcel in) {
            return new SubmitGameRequest(in);
        }

        public SubmitGameRequest[] newArray(int size) {
            return (new SubmitGameRequest[size]);
        }

    }
    ;

    protected SubmitGameRequest(android.os.Parcel in) {
        this.gameUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        this.gameStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SubmitGameRequest() {
    }

    public String getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(String gameUserId) {
        this.gameUserId = gameUserId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(gameUserId);
        dest.writeValue(time);
        dest.writeValue(gameStatus);
    }

    public int describeContents() {
        return  0;
    }

}
