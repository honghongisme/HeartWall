package com.example.administrator.ding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.WelcomeGuideActivity;
import com.example.administrator.ding.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeadFragment extends BaseFragment {

    @BindView(R.id.go_login_view_stub)
    ViewStub mLoginBtnViewStub;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_lead, container, false);
        ButterKnife.bind(this, mView);

        int res = getArguments().getInt("res");
        mView.setBackgroundResource(res);
        if (res == R.drawable.lead4) setClickBtn();

        return mView;
    }

    public void setClickBtn() {
        View view = mLoginBtnViewStub.inflate();
        ImageView loginBtn = view.findViewById(R.id.go_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("加载中，请稍候...");
                ((WelcomeGuideActivity)getActivity()).goLogin();
            }
        });
    }
}
