
package com.shabdamsdk.model.getwordresp;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetWordResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    public final static Creator<GetWordResponse> CREATOR = new Creator<GetWordResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public GetWordResponse createFromParcel(android.os.Parcel in) {
            return new GetWordResponse(in);
        }

        public GetWordResponse[] newArray(int size) {
            return (new GetWordResponse[size]);
        }

    }
    ;

    protected GetWordResponse(android.os.Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.statusMessage = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (Datum.class.getClassLoader()));
    }

    public GetWordResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(statusMessage);
        dest.writeList(data);
    }

    public int describeContents() {
        return  0;
    }

}
