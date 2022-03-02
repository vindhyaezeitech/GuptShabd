
package com.shabdamsdk.model.gamesubmit;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("game_user_id")
    @Expose
    private Integer gameUserId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("game_status")
    @Expose
    private String gameStatus;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(android.os.Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;

    protected Data(android.os.Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.gameUserId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        this.gameStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(Integer gameUserId) {
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
        dest.writeValue(id);
        dest.writeValue(gameUserId);
        dest.writeValue(time);
        dest.writeValue(gameStatus);
    }

    public int describeContents() {
        return  0;
    }

}
