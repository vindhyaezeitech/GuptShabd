
package com.shabdamsdk.model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetWordRequest implements Parcelable
{

    @SerializedName("game_user_id")
    @Expose
    private String userId;
    @SerializedName("word_id")
    @Expose
    private List<String> wordId = null;
    @SerializedName("app_id")
    @Expose
    private String app_id;
    public final static Creator<GetWordRequest> CREATOR = new Creator<GetWordRequest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public GetWordRequest createFromParcel(android.os.Parcel in) {
            return new GetWordRequest(in);
        }

        public GetWordRequest[] newArray(int size) {
            return (new GetWordRequest[size]);
        }

    }
    ;

    protected GetWordRequest(android.os.Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.app_id = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.wordId, (String.class.getClassLoader()));
    }

    public GetWordRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getWordId() {
        return wordId;
    }

    public void setWordId(List<String> wordId) {
        this.wordId = wordId;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeList(wordId);
        dest.writeValue(app_id);


    }

    public int describeContents() {
        return  0;
    }

}
