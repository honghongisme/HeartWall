package com.example.administrator.ding.base;

import android.app.Application;
import android.content.Context;
import com.example.administrator.ding.model.entities.User;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApplication extends Application {

    private User user;
    private RefWatcher refWatcher;

    public User getUser() {
        return user;
    }

    public void saveUser(User user) {
        this.user = user;
    }

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // 判断是否和 LeakCanary 初始化同一进程
            return;
        }
        refWatcher = LeakCanary.install(this);//获取一个 Watcher
    }
}
