package com.example.administrator.ding.factory.presenter;

import android.content.Context;
import com.example.administrator.ding.bean.User;
import com.example.administrator.ding.factory.model.ILoginImpl;
import com.example.administrator.ding.listener.OnGetLoginResultListener;
import com.example.administrator.ding.factory.IView.ILoginView;
import com.example.administrator.ding.utils.LoginFieldCheckUtil;

public class LoginPresenter{

    private ILoginImpl mModel;
    private ILoginView mView;

    public LoginPresenter(ILoginView mView) {
        this.mView = mView;
        mModel = new ILoginImpl((Context) mView);
    }

    /**
     * 验证注册信息是否为空，并提示错误
     * @param account
     * @param password
     * @return
     */
    private boolean checkInputDataValid(String account, String password) {
        boolean result = false;
        if (LoginFieldCheckUtil.isEmpty(account)) {
            mView.nameIsEmpty();
        } else if (LoginFieldCheckUtil.isEmpty(password)) {
            mView.passwordEmpty();
        } else {
            result = true;
        }
        return result;
    }

    /**
     * 提交账号密码登录
     * @param account
     * @param password
     */
    public void submitUserInfoToServer(String account, String password) {
        if (checkInputDataValid(account, password)) {
            // 显示加载进度条
            mView.loadingDialog();
            // 请求服务器验证用户信息
            mModel.requestServer((Context)mView, account, password, new OnGetLoginResultListener() {
                @Override
                public void isNoNet() {
                    mView.noNetWork();
                }

                @Override
                public void isNotExistUser() {
                    mView.userNotExist();
                }

                @Override
                public void isSuccess(User userInfo) {
                    mView.loginSuccess(userInfo);
                }

                @Override
                public void isError() {
                    mView.loginFailed();
                }
            });

        }
    }
}
