package com.example.administrator.ding.listener;

public interface OnGetCheckResultListener {

    /**
     * 请求成功
     */
    void onSuccess();

    /**
     * 网络异常等错误
     */
    void onFailed();

    /**
     * 敏感词检测失败
     */
    void onCheckFailed();

}
