package com.example.administrator.ding.module.main;

import com.example.administrator.ding.base.IBasePresenter;
import com.example.administrator.ding.base.IBaseView;

public interface IMainContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 获取当日 计划模块 钉拔信息
         *
         * @param nailNumberStr 钉下数量的字符串
         * @param pullNumberStr
         * @param comment       评论的字符串
         */
        void onGetCurrentPlanModuleInfo(String nailNumberStr, String pullNumberStr, String comment);

        /**
         * 获取当日 心情模块 钉拔信息
         *
         * @param badNailNumberStr
         * @param pullNumberStr
         * @param goodNailNumberStr
         * @param comment
         */
        void onGetCurrentMoodModuleInfo(String badNailNumberStr, String pullNumberStr, String goodNailNumberStr, String comment);
    }

    interface Presenter extends IBasePresenter {

        /**
         * 获取当日计划模块钉拔信息
         */
        void getTodayPlanModuleInfo();

        /**
         * 获取当日心情模块钉拔信息
         */
        void getTodayMoodModuleInfo();

        /**
         * 清空账号本地信息
         */
        void clearAccountInfo();
    }
}
