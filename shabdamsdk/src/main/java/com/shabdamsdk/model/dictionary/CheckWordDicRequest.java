
package com.shabdamsdk.model.dictionary;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckWordDicRequest implements Parcelable
{

    @SerializedName("word")
    @Expose
    private String word;

    public String getWord_id() {
        return word_id;
    }

    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }

    @SerializedName("word_id")
    @Expose
    private String word_id;
    @SerializedName("app_id")
    @Expose
    private String app_id;
    public final static Creator<CheckWordDicRequest> CREATOR = new Creator<CheckWordDicRequest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CheckWordDicRequest createFromParcel(android.os.Parcel in) {
            return new CheckWordDicRequest(in);
        }

        public CheckWordDicRequest[] newArray(int size) {
            return (new CheckWordDicRequest[size]);
        }

    }
    ;

    protected CheckWordDicRequest(android.os.Parcel in) {
        this.word = ((String) in.readValue((String.class.getClassLoader())));
        this.word_id = ((String) in.readValue((String.class.getClassLoader())));
        this.app_id = ((String) in.readValue((String.class.getClassLoader())));

    }

    public CheckWordDicRequest() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(word);
        dest.writeValue(word_id);
        dest.writeValue(app_id);


    }

    public int describeContents() {
        return  0;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

}
