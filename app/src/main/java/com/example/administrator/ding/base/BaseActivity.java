package com.example.administrator.ding.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


/**
 * Created by Administrator on 2018/8/23.
 */

public class BaseActivity extends AppCompatActivity {

    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void showShortToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }else {
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(text);
        }
        mToast.show();
    }

    public void showLongToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
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
}
