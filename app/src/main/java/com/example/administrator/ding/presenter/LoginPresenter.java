package com.example.administrator.ding.presenter;

public interface LoginPresenter {

    /**
     * 检查注册信息是否为空
     * @param account
     * @param password
     * @return
     */
    boolean checkInputDataValid(String account, String password);

    /**
     * 提交账号密码登录
     * @param account
     * @param password
     */
    void submitUserInfoToServer(String account, String password);
}
