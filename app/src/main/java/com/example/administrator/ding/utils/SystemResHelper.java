package com.example.administrator.ding.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.example.administrator.ding.R;

public class SystemResHelper {

    /**
     * 设置通知栏和toolbar颜色一致
     * @param context
     */
    public static void setStateBarColor(Activity context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = context.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(context.getResources().getColor(R.color.toolbarColor));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取手机屏幕尺寸
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return new int[]{metric.widthPixels, metric.heightPixels};
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    public static void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
