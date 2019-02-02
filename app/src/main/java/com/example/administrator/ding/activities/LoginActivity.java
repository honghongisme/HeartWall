package com.example.administrator.ding.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.example.administrator.ding.R;
import com.example.administrator.ding.base.SimpleActivity;
import com.example.administrator.ding.bean.User;
import com.example.administrator.ding.config.MyApplication;
import com.example.administrator.ding.factory.presenter.LoginPresenter;
import com.example.administrator.ding.factory.IView.ILoginView;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.utils.SystemResHelper;


public class LoginActivity extends SimpleActivity implements ILoginView {

    /**
     * 用户不存在
     */
    private static final int USER_NOT_EXIST = 0;
    /**
     * 登录成功
     */
    private static final int LOGIN_SUCCESS = 1;
    /**
     * 登录失败
     */
    private static final int LOGIN_FAILED = 2;
    /**
     * 没有网络
     */
    private static final int NO_NETWORK = 3;

    private EditText accountEv, passwordEv;
    private ImageView loginBtn;
    private LoginPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentViewData() { }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        accountEv = (EditText)findViewById(R.id.account_ev);
        passwordEv = (EditText)findViewById(R.id.password_ev);
        loginBtn = (ImageView) findViewById(R.id.login_btn);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        mPresenter = new LoginPresenter(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                    case USER_NOT_EXIST:
                        showLongToast("用户不存在");
                        hideProgress();
                        break;
                    case LOGIN_SUCCESS:
                        showLongToast("登录成功");
                        hideProgress();
                        switchActivity();
                        break;
                    case LOGIN_FAILED:
                        showLongToast("登录失败");
                        hideProgress();
                        break;
                    case NO_NETWORK:
                        NetStateCheckHelper.setNetworkMethod(getContext());
                        hideProgress();
                }
            }
        };
    }

    @Override
    protected void initListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemResHelper.hideKeyBoard(v);
                attemptLogin();
            }
        });
        passwordEv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    SystemResHelper.hideKeyBoard(v);
                    attemptLogin();
                }
                return true;
            }
        });
    }

    @Override
    protected Context getContext() {
        return this;
    }
    

    /**
     * 注册信息检查提交
     */
    private void attemptLogin() {
        String account = accountEv.getText().toString().trim();
        String password = passwordEv.getText().toString().trim();
        mPresenter.submitUserInfoToServer(account, password);
    }

    /**
     * 启动主界面
     */
    private void switchActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
        LoginActivity.this.finish();
    }

    @Override
    public void nameIsEmpty() {
        accountEv.setError("用户名不能为空");
        accountEv.requestFocus();
    }

    @Override
    public void passwordEmpty() {
        passwordEv.setError("密码不能为空");
        passwordEv.requestFocus();
    }

    @Override
    public void loadingDialog() {
        showProgress("正在同步数据，请稍后...");
    }

    @Override
    public void noNetWork() {
        sendLoadingMessage(NO_NETWORK);
    }

    @Override
    public void userNotExist() {
        sendLoadingMessage(USER_NOT_EXIST);
    }

    @Override
    public void loginSuccess(User user) {
        ((MyApplication)getApplication()).saveUser(user);
        sendLoadingMessage(LOGIN_SUCCESS);
    }

    @Override
    public void loginFailed() {
        sendLoadingMessage(LOGIN_FAILED);
    }
}