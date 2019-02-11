package com.example.administrator.ding.view;

public interface IMainView {

    /**
     * 获取当日 计划模块 钉拔信息
     * @param nailNumberStr 钉下数量的字符串
     * @param pullNumberStr
     * @param comment 评论的字符串
     */
    void onGetCurrentPlanModuleInfo(String nailNumberStr, String pullNumberStr, String comment);

    /**
     * 获取当日 心情模块 钉拔信息
     * @param badNailNumberStr
     * @param pullNumberStr
     * @param goodNailNumberStr
     * @param comment
     */
    void onGetCurrentMoodModuleInfo(String badNailNumberStr, String pullNumberStr, String goodNailNumberStr, String comment);

}
