package com.example.administrator.ding.widgt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.ding.R;

public class ToolFuncDescDialog extends BaseDialog {

    public ToolFuncDescDialog(@NonNull Context context) {
        super(context, R.style.ToolFuncDescDialog);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_tool_func_desc;
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
