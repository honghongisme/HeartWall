package com.example.administrator.ding.presenter;

public interface MainPresenter {

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
