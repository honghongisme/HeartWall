package com.example.administrator.ding.widgt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.example.administrator.ding.R;

public class DetailInfoDialog extends BaseDialog {

    public DetailInfoDialog(@NonNull Context context) {
        super(context, R.style.ToolFuncDescDialog);
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

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_detail;
    }
}
