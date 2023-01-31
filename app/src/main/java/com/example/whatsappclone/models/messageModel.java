package com.example.whatsappclone.models;


import java.sql.Date;

public class messageModel {
    private String message;
    private String currantTime;
    private String sender;
    private String reciever;
    Boolean readflag;

    public messageModel(String message, String sender, String reciever,String currantTime) {
        this.message = message;
        this.sender = sender;
        this.reciever = reciever;
        this.currantTime=currantTime;
    }

    public messageModel() {
    }

    public String getMessages() {
        return message;
    }

    public void setMessages(String messages) {
        this.message = messages;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public Boolean getReadflag() {
        return readflag;
    }

    public void setReadflag(Boolean readflag) {
        this.readflag = readflag;
    }

    public String getCurrantTime() {
        return currantTime;
    }

    public void setCurrantTime(String currantTime) {
        this.currantTime = currantTime;
    }
}
