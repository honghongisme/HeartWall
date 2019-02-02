package com.example.administrator.ding.listener;

import com.example.administrator.ding.bean.User;

/**
 * 网络请求结果回调接口
 */
public interface OnGetLoginResultListener {

    void isNoNet();
    void isNotExistUser();
    void isSuccess(User userInfo);
    void isError();

}
