package com.example.administrator.ding.factory.presenter;

import android.content.Context;
import com.example.administrator.ding.factory.IView.IMainView;
import com.example.administrator.ding.factory.model.ModuleDateShowModel;

public class MainPresenter {

    private IMainView mView;
    private ModuleDateShowModel mModel;

    public MainPresenter(IMainView mView) {
        this.mView = mView;
        mModel = new ModuleDateShowModel((Context)mView);
    }

    public void getTodayModuleInfo() {
        getTodayPlanModuleInfo();
        getTodayMoodModuleInfo();
    }

    /**
     * 获取当日计划模块钉拔信息
     */
    private void getTodayPlanModuleInfo() {
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

    /**
     * 获取当日心情模块钉拔信息
     */
    private void getTodayMoodModuleInfo() {
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

    /**
     * 清空账号本地信息
     */
    public void clearAccountInfo() {
        mModel.clearDB();
    }
}
