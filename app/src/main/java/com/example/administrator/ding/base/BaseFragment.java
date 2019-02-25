package com.example.administrator.ding.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.example.administrator.ding.widgt.LoadingProgressDialog;

public class BaseFragment extends Fragment{


    /**
     * toast
     */
    protected Toast toast;
    /**
     * 加载条
     */
    protected LoadingProgressDialog mDialog;
    /**
     * 网络请求的handler
     */
    protected Handler mHandler;


    /**
     * 显示短toast
     * @param text
     */
    protected void showShortToast(String text) {
        if(toast == null) {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
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
    protected void showLongToast(String text) {
        if(toast == null) {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        }else {
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 取消toast
     */
    protected void cancelToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    /**
     * 发送 加载message
     */
    protected void sendLoadingMessage(int msgWhat, Message msg) {
        if (msg == null) {
            msg = new Message();
        }
        msg.what = msgWhat;
        mHandler.sendMessage(msg);
    }

    /**
     * 显示loading进度条
     */
    protected void showProgress(String desc) {
        if (mDialog == null) {
            mDialog = new LoadingProgressDialog(getContext());
            mDialog.show();
        }
        if (!mDialog.isShowing()) mDialog.show();
        mDialog.setData(desc);
    }

    /**
     * 隐藏loading进度条
     */
    protected void hideProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
