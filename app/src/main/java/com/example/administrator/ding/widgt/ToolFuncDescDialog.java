package com.example.administrator.ding.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.utils.SystemResHelper;

public class ToolFuncDescDialog extends Dialog {

    private View layout;
    private Context context;

    public ToolFuncDescDialog(@NonNull Context context) {
        super(context, R.style.ToolFuncDescDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        layout = LayoutInflater.from(context).inflate(R.layout.dialog_tool_func_desc, null);
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

    public void setData(int resId, String title, String toolDesc, String extraTips) {
        setToolImageView(resId);
        setToolTitle(title);
        setToolDesc(toolDesc);
        setToolExtraTips(extraTips);
    }

    private void setToolImageView(int resId) {
        ImageView toolIv = layout.findViewById(R.id.tool_iv);
        toolIv.setImageResource(resId);
    }

    private void setToolTitle(String title) {
        TextView titleTv = layout.findViewById(R.id.tool_title_tv);
        titleTv.setText(title);
    }

    private void setToolDesc(String toolDesc) {
        TextView toolDescTv = layout.findViewById(R.id.tool_desc_tv);
        toolDescTv.setText(Html.fromHtml(toolDesc));
    }

    private void setToolExtraTips(String extraTips) {
        TextView toolExtraTipsTv = layout.findViewById(R.id.tool_extra_tips_tv);
        toolExtraTipsTv.setText(extraTips);
    }

}
