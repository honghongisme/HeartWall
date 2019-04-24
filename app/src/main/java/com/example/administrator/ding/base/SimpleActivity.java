package com.example.administrator.ding.base;

import android.os.Handler;
import android.os.Bundle;
import com.example.administrator.ding.widgt.LoadingProgressDialog;

public abstract class SimpleActivity extends BaseActivity {

    private LoadingProgressDialog mLoadingProgress;
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 显示loading进度条
     */
    protected void showProgress(String desc) {
        if (mLoadingProgress == null) {
            mLoadingProgress = new LoadingProgressDialog(this);
            mLoadingProgress.show();
        }
        if (!mLoadingProgress.isShowing()) mLoadingProgress.show();
        mLoadingProgress.setData(desc);
    }

    /**
     * 隐藏loading进度条
     */
    protected void hideProgress() {
        if(mLoadingProgress != null) {
            mLoadingProgress.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
