package com.example.administrator.ding.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.ding.R;
import com.example.administrator.ding.bean.User;
import com.example.administrator.ding.config.MyApplication;
import com.example.administrator.ding.factory.model.UserModel;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        login();
    }

    /**
     * 页面跳转
     */
    private void login() {
        UserModel userModel = new UserModel();
        User user = userModel.getUserData(this);
        if (user == null) {
            switchActivity(WelcomeGuideActivity.class);
        }else {
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            ((MyApplication)getApplication()).saveUser(user);
            switchActivity(i);
        }
    }

    /**
     * 启动activity
     * @param cls
     */
    private void switchActivity(Class<?> cls) {
        Intent i = new Intent(SplashScreenActivity.this, cls);
        switchActivity(i);
    }

    /**
     * 延时启动
     * @param intent
     */
    private void switchActivity(final Intent intent) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        }, 2000);
    }
}
