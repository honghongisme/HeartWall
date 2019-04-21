package com.example.administrator.ding;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.ding.bean.User;
import com.example.administrator.ding.base.MyApplication;
import com.example.administrator.ding.module.login.UserInfoStore;
import com.example.administrator.ding.module.main.MainActivity;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    private void init() {
        User user = new UserInfoStore().getUser(this);
        if (user == null) {
            Intent i = new Intent(this, WelcomeGuideActivity.class);
            switchActivity(i);
        } else {
            Intent i = new Intent(this, MainActivity.class);
            ((MyApplication)getApplication()).saveUser(user);
            switchActivity(i);
        }
    }

    private void switchActivity(final Intent intent) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}
