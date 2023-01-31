package com.example.whatsappclone.models;

import androidx.fragment.app.Fragment;

public class tabLayoutModel {
    private Fragment fragment;
    private String name;

    public tabLayoutModel(Fragment fragment, String name) {
        this.fragment = fragment;
        this.name = name;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
