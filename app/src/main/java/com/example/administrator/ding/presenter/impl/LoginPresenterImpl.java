package com.example.administrator.ding.presenter.impl;

import android.content.Context;
import com.example.administrator.ding.contract.LoginContract;
import com.example.administrator.ding.model.entities.User;
import com.example.administrator.ding.model.impl.LoginModelImpl;
import com.example.administrator.ding.presenter.OnGetLoginResultListener;
import com.example.administrator.ding.utils.LoginFieldCheckUtil;

public class LoginPresenterImpl implements LoginContract.LoginPresenter {

    private LoginModelImpl mModel;
    private LoginContract.ILoginView mView;

    public LoginPresenterImpl(LoginContract.ILoginView mView) {
        this.mView = mView;
        mModel = new LoginModelImpl((Context) mView);
    }

    @Override
    public boolean checkInputDataValid(String account, String password) {
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

    @Override
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
                public void onSuccess(User userInfo) {
                    mView.loginSuccess(userInfo);
                }

                @Override
                public void onFailed() {
                    mView.loginFailed();
                }
            });

        }
    }
}
