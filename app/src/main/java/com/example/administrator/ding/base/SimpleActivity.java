package com.example.administrator.ding.base;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import com.example.administrator.ding.widgt.LoadingProgressDialog;

public abstract class SimpleActivity extends BaseActivity {

    private LoadingProgressDialog dialog;
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 发送 加载message
     */
    protected void sendLoadingMessage(int msgWhat) {
        Message msg = new Message();
        msg.what = msgWhat;
        handler.sendMessage(msg);
    }

    /**
     * 显示loading进度条
     */
    protected void showProgress(String desc) {
        if (dialog == null) {
            dialog = new LoadingProgressDialog(getContext());
            dialog.show();
        }
        if (!dialog.isShowing()) dialog.show();
        dialog.setData(desc);
    }

    /**
     * 隐藏loading进度条
     */
    protected void hideProgress() {
        if(dialog != null) {
            dialog.dismiss();
        }
    }
}
