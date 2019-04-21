package com.example.administrator.ding.module.main;

import android.content.Context;

public class MainPresenterImpl implements IMainContract.Presenter {

    private IMainContract.View mView;
    private MainModel mModel;

    public MainPresenterImpl(IMainContract.View mView) {
        this.mView = mView;
        mModel = new MainModel((Context)mView);
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
