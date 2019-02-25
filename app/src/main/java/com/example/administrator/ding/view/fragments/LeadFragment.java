package com.example.administrator.ding.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.view.activities.WelcomeGuideActivity;
import com.example.administrator.ding.widgt.LoadingProgressDialog;

public class LeadFragment extends Fragment {

    private View mView;
    private LoadingProgressDialog mProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_lead, container, false);
        ImageView imageView = mView.findViewById(R.id.im);
        int res = getArguments().getInt("res");
        imageView.setImageResource(res);

        if (res == R.drawable.lead4) {
            System.out.println("click");
            setClickBtn();
        }

        return mView;
    }

    public void setClickBtn() {
        ImageView imageView = mView.findViewById(R.id.btn);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("加载中，请稍候...");
                ((WelcomeGuideActivity)getActivity()).clickCallBack();
            }
        });
    }

    /**
     * 显示loading进度条
     */
    private void showProgress(String desc) {
        mProgress = new LoadingProgressDialog(getContext());
        if (!mProgress.isShowing()) mProgress.show();
        mProgress.setData(desc);
    }

    /**
     * 隐藏loading进度条
     */
    private void hideProgress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgress();
    }
}
