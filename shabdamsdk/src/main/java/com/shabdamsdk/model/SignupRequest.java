
package com.shabdamsdk.model;

import javax.annotation.Generated;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SignupRequest implements Parcelable
{

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    public final static Creator<SignupRequest> CREATOR = new Creator<SignupRequest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SignupRequest createFromParcel(android.os.Parcel in) {
            return new SignupRequest(in);
        }

        public SignupRequest[] newArray(int size) {
            return (new SignupRequest[size]);
        }

    }
    ;

    protected SignupRequest(android.os.Parcel in) {
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.uname = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.profileimage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SignupRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(email);
        dest.writeValue(name);
        dest.writeValue(uname);
        dest.writeValue(userId);
        dest.writeValue(profileimage);
    }

    public int describeContents() {
        return  0;
    }

}
