package com.example.whatsappclone.models;

import java.io.Serializable;
import java.util.List;

public class chatModel implements Serializable {
    private List<String> members;

    public chatModel() {
    }

    public chatModel(List<String> members) {
        this.members = members;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
