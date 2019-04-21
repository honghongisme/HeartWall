package com.example.administrator.ding.module.nail.mood;

import android.view.View;
import com.example.administrator.ding.bean.Crack;
import com.example.administrator.ding.bean.nail.MoodBadNail;
import com.example.administrator.ding.bean.nail.MoodGoodNail;
import com.example.administrator.ding.widgt.EditInfoDialog;

import java.util.ArrayList;

public interface IMoodView {

    /**
     * 获取初始裂缝数据
     * @param cracks
     */
    void getInitialCracksSuccess(ArrayList<Crack> cracks);

    /**
     * 获取初始好运钉子数据
     * @param moodGoodNails
     */
    void getInitialMoodGoodNailsSuccess(ArrayList<MoodGoodNail> moodGoodNails);

    /**
     * 获取初始厄运钉子数据
     * @param moodBadNails
     */
    void getInitialMoodBadNailsSuccess(ArrayList<MoodBadNail> moodBadNails);

    /**
     * 选择的位置有效，可放置
     * @param centerPointX
     * @param centerPointY
     * @param imageSize
     */
    void placeValid(int centerPointX, int centerPointY, int[] imageSize);

    /**
     * 选择的位置无效，不可放置
     */
    void placeInValid();

    /**
     * 提交第一次编辑的信息成功
     */
    void submitGoodNailFirstEditInfoSuccess(MoodGoodNail moodGoodNail, EditInfoDialog editInfoDialog);

    /**
     *  提交第一次编辑的信息失败
     */
    void submitGoodNailFirstEditInfoFailed(EditInfoDialog editInfoDialog);

    /**
     * 内含敏感词，未通过提交
     */
    void submitGoodNailFirstEditInfoNotPass();



    /**
     * 提交第一次编辑的信息成功
     */
    void submitBadNailFirstEditInfoSuccess(MoodBadNail moodBadNail, Crack crack, int nailHeadMarginLeft, int nailHeadMarginTop);

    /**
     *  提交第一次编辑的信息失败
     */
    void submitBadNailFirstEditInfoFailed();

    /**
     * 内含敏感词，未通过提交
     */
    void submitBadNailFirstEditInfoNotPass();

    /**
     * 拔出好运钉子成功
     */
    void updateGoodNailSuccess(MoodGoodNail nail, View v);

    /**
     * 拔出好运钉子失败
     */
    void updateGoodNailFailed();

    /**
     * 拔出厄运钉子成功
     */
    void updateBadNailSuccess(MoodBadNail nail, View v);

    /**
     * 拔出厄运钉子失败
     */
    void updateBadNailFailed();

    /**
     * 获取钉帽信息
     * @param nail
     */
    void onGetGoodNailHeadDetails(MoodGoodNail nail);
    void onGetBadNailHeadDetails(MoodBadNail nail);

    /**
     * 获取裂缝信息
     * @param info
     */
    void onGetCrackDetails(Crack info);
}
