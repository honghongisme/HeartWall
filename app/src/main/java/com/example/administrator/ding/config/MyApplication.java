package com.example.administrator.ding.config;

import android.app.Application;
import com.example.administrator.ding.model.entities.User;

public class MyApplication extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void saveUser(User user) {
        this.user = user;
    }
}
