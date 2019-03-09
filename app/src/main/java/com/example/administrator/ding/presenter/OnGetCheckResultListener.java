package com.example.administrator.ding.presenter;

public interface OnGetCheckResultListener extends IBaseNetRequestListener {

    /**
     * 敏感词检测未通过
     */
    void onNotPassSensitiveWordsCheck();

}
