package com.comp3717.vu_gilpin.models;

import com.google.firebase.database.Exclude;

public class User {
    @Exclude
    private String key;
    private String userId;

    public User() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
