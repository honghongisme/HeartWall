package com.example.administrator.ding.presenter;

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
     * 敏感词检测未通过
     */
    void onNotPassSensitiveWordsCheck();

}
