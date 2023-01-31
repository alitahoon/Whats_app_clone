package com.example.whatsappclone.models;

public class txtStoryModel {
    private String text;
    private String time;
    private String publisherPhone;
    private String StringStatusID;

    public txtStoryModel(String text, String time, String publisherPhone, String stringStatusID) {
        this.text = text;
        this.time = time;
        this.publisherPhone = publisherPhone;
        StringStatusID = stringStatusID;
    }

    public txtStoryModel() {
    }

    public String getStringStatusID() {
        return StringStatusID;
    }

    public void setStringStatusID(String stringStatusID) {
        StringStatusID = stringStatusID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPublisherPhone() {
        return publisherPhone;
    }

    public void setPublisherPhone(String publisherPhone) {
        this.publisherPhone = publisherPhone;
    }
}
