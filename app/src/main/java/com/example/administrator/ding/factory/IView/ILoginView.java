package com.example.administrator.ding.factory.IView;

import com.example.administrator.ding.bean.User;

public interface ILoginView {

    /**
     * 显示用户名为空
     */
    void nameIsEmpty();

    /**
     * 显示密码为空
     */
    void passwordEmpty();

    /**
     * 显示正在同步加载等待框
     */
    void loadingDialog();

    /**
     * 网络未连接
     */
    void noNetWork();

    /**
     * 用户不存在
     */
    void userNotExist();

    /**
     * 登录成功，获取服务器返回的用户数据
     * @param user
     */
    void loginSuccess(User user);

    /**
     * 登录失败
     */
    void loginFailed();
}
