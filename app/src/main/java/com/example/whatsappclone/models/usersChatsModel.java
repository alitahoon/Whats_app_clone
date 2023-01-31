package com.example.whatsappclone.models;

import java.io.Serializable;

public class usersChatsModel implements Serializable {
    private String userID;
    private String chatID;

    public usersChatsModel() {
    }
    public usersChatsModel(String userID, String chatID) {
        this.userID = userID;
        this.chatID = chatID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
}
