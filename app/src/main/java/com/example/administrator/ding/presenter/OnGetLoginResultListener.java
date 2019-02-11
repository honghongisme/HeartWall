package com.example.administrator.ding.presenter;

import com.example.administrator.ding.model.entities.User;

/**
 * 网络请求结果回调接口
 */
public interface OnGetLoginResultListener {

    void isNoNet();
    void isNotExistUser();
    void isSuccess(User userInfo);
    void isError();

}
