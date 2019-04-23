package com.example.administrator.ding.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.example.administrator.ding.R;
import com.example.administrator.ding.base.SimpleActivity;
import com.example.administrator.ding.model.entry.User;
import com.example.administrator.ding.base.MyApplication;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.utils.SystemResHelper;
import com.example.administrator.ding.module.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;


public class LoginActivity extends SimpleActivity implements ILoginContract.View {

    @BindView(R.id.account_ev)
    EditText mAccountEv;
    @BindView(R.id.password_ev)
    EditText mPasswordEv;
    @BindView(R.id.login_btn)
    ImageView mLoginIv;
    private LoginPresenterImpl mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPresenter = new LoginPresenterImpl(this);
    }

    @OnEditorAction(R.id.password_ev)
    public boolean doPasswordEditEnter(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            SystemResHelper.hideKeyBoard(v);
            attemptLogin();
        }
        return true;
    }

    @OnClick(R.id.login_btn)
    public void doLogin(View v) {
        SystemResHelper.hideKeyBoard(v);
        attemptLogin();
    }

    /**
     * 注册信息检查提交
     */
    private void attemptLogin() {
        String account = mAccountEv.getText().toString().trim();
        String password = mPasswordEv.getText().toString().trim();
        mPresenter.submitUserInfoToServer(account, password);
    }

    @Override
    public void nameIsEmpty() {
        mAccountEv.setError("用户名不能为空");
        mAccountEv.requestFocus();
    }

    @Override
    public void passwordIsEmpty() {
        mPasswordEv.setError("密码不能为空");
        mPasswordEv.requestFocus();
    }

    @Override
    public void loadingDialog() {
        showProgress("正在同步数据，请稍后...");
    }

    @Override
    public void noNetWork() {
        hideProgress();
        NetStateCheckHelper.setNetworkMethod(getApplicationContext());
    }

    @Override
    public void userNotExist() {
        hideProgress();
        showLongToast("用户不存在");
    }

    @Override
    public void loginSuccess(User user) {
        ((MyApplication)getApplication()).saveUser(user);
        hideProgress();
        showLongToast("登录成功");
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);finish();
    }

    @Override
    public void loginFailed() {
        hideProgress();
        showLongToast("登录失败");
    }

}