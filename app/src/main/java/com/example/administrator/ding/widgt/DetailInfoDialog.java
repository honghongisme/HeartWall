package com.example.administrator.ding.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.TextView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.utils.SystemResHelper;

public class DetailInfoDialog extends Dialog {

    private Context context;
    private View layout;

    public DetailInfoDialog(@NonNull Context context) {
        super(context, R.style.ToolFuncDescDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        layout = LayoutInflater.from(context).inflate(R.layout.dialog_detail, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);

        // 设置窗口大小
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        int[] screenSize = SystemResHelper.getScreenSize(context);
        params.width = (int)(screenSize[0] * 0.8);
        params.height = (int)(screenSize[1] * 0.7);
        dialogWindow.setAttributes(params);
        dialogWindow.setWindowAnimations(R.style.dialog_animation_style);
    }

    public void setData(String date, String content) {
        setDate(date);
        setContent(content);
    }

    private void setDate(String date) {
        TextView dateTv = layout.findViewById(R.id.firstDate);
        dateTv.setText(date);
    }

    private void setContent(String content) {
        TextView contentTv = layout.findViewById(R.id.content);
        contentTv.setText(content);
    }
}
