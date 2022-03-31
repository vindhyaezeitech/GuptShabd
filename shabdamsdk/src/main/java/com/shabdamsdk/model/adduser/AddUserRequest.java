package com.shabdamsdk.model.adduser;

import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddUserRequest implements Parcelable
{

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name = " ";
    @SerializedName("uname")
    @Expose
    private String uname = " ";
    @SerializedName("email")
    @Expose
    private String email = " ";
    @SerializedName("profileimage")
    @Expose
    private String profileimage = " ";
    public final static Creator<AddUserRequest> CREATOR = new Creator<AddUserRequest>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddUserRequest createFromParcel(android.os.Parcel in) {
            return new AddUserRequest(in);
        }

        public AddUserRequest[] newArray(int size) {
            return (new AddUserRequest[size]);
        }

    }
    ;

    protected AddUserRequest(android.os.Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.uname = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.profileimage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AddUserRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(name);
        dest.writeValue(uname);
        dest.writeValue(email);
        dest.writeValue(profileimage);
    }

    public int describeContents() {
        return  0;
    }

}
