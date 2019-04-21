package com.example.administrator.ding.module.login;

import com.example.administrator.ding.bean.User;

/**
 * 网络请求结果回调接口
 */
public interface IOnLoginReqListener {

    // 无网络
    void isNoNet();
    // 用户不存在
    void isNotExistUser();
    // 成功
    void onSuccess(User userInfo);
    // 失败
    void onFailed();

}
