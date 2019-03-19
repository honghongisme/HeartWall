package com.example.administrator.ding.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;
import com.example.administrator.ding.R;
import com.example.administrator.ding.utils.SystemResHelper;

public abstract class BaseDialog extends Dialog {

    private Context context;
    protected View layout;

    public abstract int getLayoutResId();

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        layout = LayoutInflater.from(context).inflate(getLayoutResId(), null);
        setContentView(layout);

        // 设置窗口大小
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        int[] screenSize = SystemResHelper.getScreenSize(context);
        params.width = (int)(screenSize[0] * 0.8);
        params.height = (int)(screenSize[1] * 0.8);
        dialogWindow.setAttributes(params);
        dialogWindow.setWindowAnimations(R.style.dialog_animation_style);
    }
}
