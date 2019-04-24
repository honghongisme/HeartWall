package com.example.administrator.ding.widgt;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.ding.R;

public class LoadingProgressDialog extends Dialog {

    private Context context;
    private View layout;

    public LoadingProgressDialog(@NonNull Context context) {
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
        layout = inflater.inflate(R.layout.dialog_loading_progress, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(params);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     *
     * @param desc
     */
    public void setData(String desc) {
        showLoadingAnim();
        setLoadingDesc(desc);
    }

    /**
     *
     */
    private void showLoadingAnim() {
        ImageView imageView = layout.findViewById(R.id.progress_bar_iv);
        final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        imageView.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
    }

    /**
     *
     * @param desc
     */
    private void setLoadingDesc(String desc) {
        if (desc != null) {
            TextView textView = layout.findViewById(R.id.loading_desc_tv);
            textView.setText(desc);
        }
    }
}
