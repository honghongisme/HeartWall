package com.example.administrator.ding.widgt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding.R;

/**
 * Created by Administrator on 2018/8/3.
 */

public class BrowerInfoDialog extends BaseDialog {


    public BrowerInfoDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.brower_info_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
    }

    /**
     * 设置数据显示
     * @param firstDate
     * @param firstRecord
     * @param lastDate
     * @param lastRecord
     */
    public void setData(String firstDate, String firstRecord, String lastDate, String lastRecord) {

        TextView firstDateTV = layout.findViewById(R.id.fd_tv);
        firstDateTV.setText(firstDate);

        TextView firstRecordTV = layout.findViewById(R.id.fr_tv);
        firstRecordTV.setText(firstRecord);

        TextView lastDateTV = layout.findViewById(R.id.ld_tv);
        lastDateTV.setText(lastDate);

        TextView lastRecordTV = layout.findViewById(R.id.lr_tv);
        lastRecordTV.setText(lastRecord);
    }

    public void initListener() {
        ImageView confirm = layout.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowerInfoDialog.super.dismiss();
            }
        });
    }

    /**
     * 设置拔出钉子按钮监听器
     * @param onGoNailClickListener
     */
    public void setOnGoNailClickListener(final OnGoNailClickListener onGoNailClickListener) {
        TextView textView = layout.findViewById(R.id.go_nail);
        textView.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowerInfoDialog.super.dismiss();
                onGoNailClickListener.onClick();
            }
        });
    }

    /**
     * 点击拔钉子的回调接口
     */
    public interface OnGoNailClickListener{
        void onClick();
    }

}
