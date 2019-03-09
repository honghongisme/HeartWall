package com.example.administrator.ding.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
     * 获得context
     */
    protected abstract Context getContext();


    private Context mContext;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
        initContentViewData();
        setContentView(getContentViewResId());
        initView();
        initData();
    }

    /**
     * 显示短toast
     * @param text
     */
    public void showShortToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }else {
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 显示长toast
     * @param text
     */
    public void showLongToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        }else {
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 取消toast
     */
    private void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
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
