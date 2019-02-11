package com.example.administrator.ding.presenter;

public interface OnGetRequestResultListener {

    /**
     * 请求成功
     */
    void onSuccess();

    /**
     * 网络异常等错误
     */
    void onFailed();

}
