package com.example.whatsappclone.models;

import java.io.Serializable;

public class chatMessagesModel implements Serializable {
    private String sentBy;
    private String messageTime;
    private String message;

    public chatMessagesModel(String sentBy, String messageTime, String message) {
        this.sentBy = sentBy;
        this.messageTime = messageTime;
        this.message = message;
    }

    public chatMessagesModel() {
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
