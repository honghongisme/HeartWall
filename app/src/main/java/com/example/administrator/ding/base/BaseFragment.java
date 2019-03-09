package com.example.administrator.ding.base;

import android.support.v4.app.Fragment;
import com.example.administrator.ding.widgt.LoadingProgressDialog;

public class BaseFragment extends Fragment {

    /**
     * 加载条
     */
    private LoadingProgressDialog mDialog;

    /**
     * 显示loading进度条
     */
    protected void showProgress(String desc) {
        if (mDialog == null) {
            mDialog = new LoadingProgressDialog(getContext());
            mDialog.show();
        }
        if (!mDialog.isShowing()) mDialog.show();
        mDialog.setData(desc);
    }

    /**
     * 隐藏loading进度条
     */
    protected void hideProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgress();
    }
}
