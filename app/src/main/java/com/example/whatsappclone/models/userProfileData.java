package com.example.whatsappclone.models;



import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

import java.io.Serializable;
import java.sql.Time;

public class userProfileData implements Serializable{
    private String mName;
    private String mTime;
    private String mExention;
    private String mPhone;
    private Timestamp mTimestamp;

    public userProfileData(String name, String time, String exention, String phone) {
        this.mName = name;
        this.mTime = time;
        this.mExention = exention;
        this.mPhone = phone;
    }

    public userProfileData() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getExention() {
        return mExention;
    }

    public void setExention(String exention) {
        this.mExention = exention;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone= phone;
    }
    @ServerTimestamp
    public Timestamp getTimestamp() { return mTimestamp; }

    public void setTimestamp(Timestamp timestamp) { mTimestamp = timestamp; }
}
