package com.example.administrator.ding.module.login;

import com.example.administrator.ding.base.IBasePresenter;
import com.example.administrator.ding.base.IBaseView;
import com.example.administrator.ding.model.entry.User;

public interface ILoginContract {

    interface Presenter extends IBasePresenter {

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

    interface View extends IBaseView<Presenter> {

        /**
         * 显示用户名为空
         */
        void nameIsEmpty();

        /**
         * 显示密码为空
         */
        void passwordIsEmpty();

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
         * 登录成功，返回的用户数据
         * @param user
         */
        void loginSuccess(User user);

        /**
         * 登录失败
         */
        void loginFailed();
    }

    interface Model {

        /**
         * 请求服务器保存信息，并同步信息到本地
         * @param account
         * @param password
         * @param listener
         */
        void doLogin(String account, String password, final IOnLoginReqListener listener);
    }
}
