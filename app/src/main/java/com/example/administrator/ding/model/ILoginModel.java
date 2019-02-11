package com.example.administrator.ding.model;

import android.content.Context;
import com.example.administrator.ding.presenter.OnGetLoginResultListener;

public interface ILoginModel {

    /**
     * 请求服务器保存信息，并同步信息到本地
     * @param context
     * @param account
     * @param password
     * @param onGetLoginResultListener
     */
    void requestServer(final Context context, String account, String password, final OnGetLoginResultListener onGetLoginResultListener);
}
