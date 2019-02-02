package com.example.administrator.ding.factory.model;

import android.content.Context;
import android.database.Cursor;

import com.example.administrator.ding.bean.User;
import com.example.administrator.ding.utils.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/23.
 */

public class UserModel {

    public User getUserData(Context context) {
        DBManager dbManager = new DBManager(context);
        if(!dbManager.isExistTable("user")) {
            return null;
        }
        User user = null;
        String sql = "select * from user";
        Cursor cursor = dbManager.queryAll(sql);
        while(cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setAccountNumber(cursor.getString(cursor.getColumnIndex("accountNumber")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            user.setDepartment(cursor.getString(cursor.getColumnIndex("department")));
            user.setIdentity(cursor.getString(cursor.getColumnIndex("identity")));
        }
        cursor.close();
        dbManager.closeAll();
        return user;
    }

}
