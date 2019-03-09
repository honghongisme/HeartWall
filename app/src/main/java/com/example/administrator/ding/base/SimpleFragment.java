package com.example.administrator.ding.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.example.administrator.ding.widgt.LoadingProgressDialog;

public class SimpleFragment extends BaseFragment{


    /**
     * toast
     */
    private Toast toast;

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

    @Override
    public void onStop() {
        super.onStop();
        cancelToast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
