package com.example.administrator.ding.module.nail;

import com.example.administrator.ding.base.IBaseNetRequestListener;

public interface OnGetCheckResultListener extends IBaseNetRequestListener {

    /**
     * 敏感词检测未通过
     */
    void onNotPassSensitiveWordsCheck();

}
