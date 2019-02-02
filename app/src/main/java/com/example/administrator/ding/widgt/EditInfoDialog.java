package com.example.administrator.ding.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import com.example.administrator.ding.R;

/**
 * 可编辑文字的Dialog
 * Created by Administrator on 2018/7/22.
 */

public class EditInfoDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private View layout;
    private ImageView confirm,cancel;
    private DialogBtnClickListener dialogBtnClickListener;


    public EditInfoDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = inflater.inflate(R.layout.edit_info_dialog, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);

        cancel = layout.findViewById(R.id.cancel);
        confirm = layout.findViewById(R.id.confirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        //获取屏幕宽高
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //宽度设置为屏幕的0.8
        params.width = (int)(displayMetrics.widthPixels * 0.8);
        params.height = (int)(displayMetrics.heightPixels * 0.9);
        dialogWindow.setAttributes(params);
        dialogWindow.setWindowAnimations(R.style.dialog_animation_style);
    }

    public void setData(int tag, String date) {
        if (tag == 1) {
            setPageText("< 1 / 2 >");
        }else if (tag == 2) {
            setPageText("< 2 / 2 >");
        }
        setDate(date);
    }

    private void setDate(String date) {
        TextView dateTv = layout.findViewById(R.id.currentDate);
        dateTv.setText(date);
    }

    private void setPageText(String text) {
        TextView pageTV = layout.findViewById(R.id.page);
        pageTV.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dialogBtnClickListener.doCancel();
                break;
            case R.id.confirm:
                EditText text = layout.findViewById(R.id.mainText);
                dialogBtnClickListener.doConfirm(text.getText().toString());
                break;
            default:
                break;
        }
    }

    public void setVisibleCheckBox() {
        ConstraintLayout constraintLayout = layout.findViewById(R.id.checkbox_container_cl);
        constraintLayout.setVisibility(View.VISIBLE);
        CheckBox visible_cb = layout.findViewById(R.id.visible_btn);
        CheckBox anonymous_cb = layout.findViewById(R.id.anonymous_btn);
        visible_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dialogBtnClickListener.isVisible(isChecked);
            }
        });
        anonymous_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dialogBtnClickListener.isAnonymous(isChecked);
            }
        });
    }

    /**
     * 完成取消按钮回调接口
     * @param dialogBtnClickListener
     */

    public void setDialogBtnClickListener(DialogBtnClickListener dialogBtnClickListener) {
        this.dialogBtnClickListener = dialogBtnClickListener;
    }

    public interface DialogBtnClickListener {
        void doConfirm(String text);
        void doCancel();
        void isVisible(boolean isChecked);
        void isAnonymous(boolean isChecked);
    }

}
