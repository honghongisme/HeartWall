package com.example.administrator.ding.presenter.impl;

import android.content.Context;
import com.example.administrator.ding.presenter.MainPresenter;
import com.example.administrator.ding.view.IMainView;
import com.example.administrator.ding.model.impl.MainModelImpl;

public class MainPresenterImpl implements MainPresenter {

    private IMainView mView;
    private MainModelImpl mModel;

    public MainPresenterImpl(IMainView mView) {
        this.mView = mView;
        mModel = new MainModelImpl((Context)mView);
    }

    public void getTodayModuleInfo() {
        getTodayPlanModuleInfo();
        getTodayMoodModuleInfo();
    }

    @Override
    public void getTodayPlanModuleInfo() {
        int[] planNumber = mModel.getTodayPlanNum();
        String nailNumberStr = null;
        String pullNumberStr = null;
        String planComment = null;
        if(planNumber != null) {
            nailNumberStr = "今日钉下 " + planNumber[0] + " 颗计划钉子";
            pullNumberStr = "今日拔出 " + planNumber[1] + " 颗计划钉子";
            planComment = mModel.getPlanComment(planNumber[0], planNumber[1]);
        } else {
            nailNumberStr = "今日钉下 0 颗计划钉子";
            pullNumberStr = "今日拔出 0 颗计划钉子";
            planComment = "今天是很清闲的一天";
        }
        mView.onGetCurrentPlanModuleInfo(nailNumberStr, pullNumberStr, planComment);
    }

    @Override
    public void getTodayMoodModuleInfo() {
        int[] moodNumber = mModel.getTodayMoodNum();
        String badNailNumberStr = null;
        String pullNumberStr = null;
        String goodNailNumberStr = null;
        String moodComment = null;
        if (moodNumber != null) {
            badNailNumberStr = "今日钉下 " + moodNumber[0] + " 颗厄运钉子";
            pullNumberStr = "今日拔出 " + moodNumber[1] + " 颗厄运钉子";
            goodNailNumberStr = "今日钉下 " + moodNumber[2] + " 颗好运钉子";
            moodComment = mModel.getMoodComment(moodNumber[0], moodNumber[1], moodNumber[2]);
        } else {
            badNailNumberStr = "今日钉下 0 颗厄运钉子";
            pullNumberStr = "今日拔出 0 颗厄运钉子";
            goodNailNumberStr = "今日钉下 0 颗好运钉子";
            moodComment = "今天是个平淡的一天";
        }
        mView.onGetCurrentMoodModuleInfo(badNailNumberStr, pullNumberStr, goodNailNumberStr, moodComment);
    }

    @Override
    public void clearAccountInfo() {
        mModel.clearDB();
    }
}
