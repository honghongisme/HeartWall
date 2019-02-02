package com.example.administrator.ding.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/8/23.
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 初始化布局所需要的数据
     */
    protected abstract void initContentViewData();
    /**
     * 获取布局资源id
     * @return
     */
    protected abstract int getContentViewResId();
    /**
     * 初始化View
     */
    public abstract void initView() ;
    /**
     * 初始化数据
     */
    protected abstract void initData();
    /**
     * 初始化监听器
     */
    protected abstract void initListener();
    /**
     * 获得context
     */
    protected abstract Context getContext();


    private Context context;
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        initContentViewData();
        setContentView(getContentViewResId());
        initView();
        initData();
        initListener();
    }

    /**
     * 显示短toast
     * @param text
     */
    public void showShortToast(String text) {
        if(toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }else {
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 显示长toast
     * @param text
     */
    public void showLongToast(String text) {
        if(toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }else {
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 取消toast
     */
    private void cancelToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelToast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelToast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelToast();
    }
}
