package com.example.administrator.ding.database.login;

import android.content.Context;

import com.example.administrator.ding.model.entry.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2018/8/23.
 */

public class UserInfoStore {

    public User getUser(Context context) {
        try {
            ObjectInputStream in =  new ObjectInputStream(new FileInputStream(context.getExternalFilesDir("").getAbsolutePath() + "/user.txt"));
            User user = (User) in.readObject();
            in.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(Context context, User user) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(context.getExternalFilesDir("").getAbsolutePath() + "/user.txt"));
            out.writeObject(user);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
