package com.example.administrator.ding.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding.R;
import com.example.administrator.ding.utils.SystemResHelper;

/**
 * Created by Administrator on 2018/8/3.
 */

public class BrowerInfoDialog extends Dialog {

    private Context context;
    private View layout;


    public BrowerInfoDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        layout = LayoutInflater.from(context).inflate(R.layout.brower_info_dialog, null);
        setContentView(layout);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        //获取屏幕宽高
        int[] screenSize = SystemResHelper.getScreenSize(context);
        //宽度设置为屏幕的0.8
        params.width = (int)(screenSize[0] * 0.8);
        params.height = (int)(screenSize[1] * 0.85);
        dialogWindow.setAttributes(params);
        dialogWindow.setWindowAnimations(R.style.dialog_animation_style);

        ImageView confirm = layout.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowerInfoDialog.super.dismiss();
            }
        });
    }

    /**
     * 设置数据显示
     * @param firstDate
     * @param firstRecord
     * @param lastDate
     * @param lastRecord
     */
    public void setData(String firstDate, String firstRecord, String lastDate, String lastRecord) {
        System.out.println("firstDate = " + firstDate);
        System.out.println("firstRecord = " + firstRecord);
        System.out.println("lastDate = " + lastDate);
        System.out.println("lastRecord = " + lastRecord);

        TextView firstDateTV = layout.findViewById(R.id.fd_tv);
        firstDateTV.setText(firstDate);
        System.out.println("----------kankn = " + firstDateTV.getText().toString());

        TextView firstRecordTV = layout.findViewById(R.id.fr_tv);
        firstRecordTV.setText(firstRecord);

        TextView lastDateTV = layout.findViewById(R.id.ld_tv);
        lastDateTV.setText(lastDate);

        TextView lastRecordTV = layout.findViewById(R.id.lr_tv);
        lastRecordTV.setText(lastRecord);
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
