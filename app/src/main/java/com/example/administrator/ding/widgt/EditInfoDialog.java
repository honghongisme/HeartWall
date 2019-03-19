package com.example.administrator.ding.widgt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.*;

import com.example.administrator.ding.R;

/**
 * 可编辑文字的Dialog
 * Created by Administrator on 2018/7/22.
 */

public class EditInfoDialog extends BaseDialog{

    private DialogBtnClickListener dialogBtnClickListener;


    public EditInfoDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.edit_info_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
    }

    private void initListener() {
        ImageView cancel = layout.findViewById(R.id.cancel);
        ImageView confirm = layout.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBtnClickListener.doCancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = layout.findViewById(R.id.mainText);
                dialogBtnClickListener.doConfirm(text.getText().toString());
            }
        });
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
