package com.example.administrator.ding.module.nail.mood;

import com.example.administrator.ding.base.IBasePresenter;
import com.example.administrator.ding.base.IBaseView;
import com.example.administrator.ding.model.entry.Crack;
import com.example.administrator.ding.model.entry.MoodBadNail;
import com.example.administrator.ding.model.entry.MoodGoodNail;
import com.example.administrator.ding.widgt.EditInfoDialog;

import java.util.ArrayList;

public interface IMoodContract {

    interface View extends IBaseView<Presenter> {
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
        void updateGoodNailSuccess(MoodGoodNail nail, android.view.View v);

        /**
         * 拔出好运钉子失败
         */
        void updateGoodNailFailed();

        /**
         * 拔出厄运钉子成功
         */
        void updateBadNailSuccess(MoodBadNail nail, android.view.View v);

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

    interface Presenter extends IBasePresenter {
        /**
         * 获取初始数据
         */
        void getInitialData();

        /**
         * 检查放置点是否有效
         * @param x
         * @param y
         * @param d
         */
        void checkIsPlaceValid(int x, int y, int[] d);

        /**
         * 提交信息至服务器
         * @param nail
         * @param editInfoDialog
         */
        void submitGoodNailEditInfoToServer(final MoodGoodNail nail, final EditInfoDialog editInfoDialog);

        /**
         * 提交信息到本地
         * @param nail
         */
        void submitGoodNailEditInfoToLocal(MoodGoodNail nail);

        /**
         * 提交信息到服务器
         * @param badNail
         * @param crack
         */
        void submitBadNailEditInfoToServer(final MoodBadNail badNail, final Crack crack, final int nailHeadMarginLeft, final int nailHeadMarginTop);

        /**
         * 提交信息到本地
         * @param nail
         */
        void submitBadNailEditInfoToLocal(MoodBadNail nail, Crack crack);

        /**
         * 拔出好运钉子，更新服务器数据
         * @param nail
         */
        void updateGoodNailFromServer(final MoodGoodNail nail, final android.view.View v);

        /**
         * 拔出好运钉子，更新本地数据
         * @param nail
         */
        void updateGoodNailFromLocal(MoodGoodNail nail);

        /**
         * 拔出厄运钉子，更新服务器数据
         * @param nail
         */
        void updateBadNailFromServer(final MoodBadNail nail, final android.view.View v);

        /**
         * 拔出厄运钉子，更新本地数据
         * @param nail
         */
        void updateBadNailFromLocal(MoodBadNail nail);

        void getGoodNailHeadDetails(int x, int y);
        void getBadNailHeadDetails(int x, int y);
        void getCrackDetails(int x, int y);
    }
}
