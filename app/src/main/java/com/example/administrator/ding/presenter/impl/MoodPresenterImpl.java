package com.example.administrator.ding.presenter.impl;

import android.view.View;
import com.example.administrator.ding.model.entities.Crack;
import com.example.administrator.ding.model.entities.MoodBadNail;
import com.example.administrator.ding.model.entities.MoodGoodNail;
import com.example.administrator.ding.model.impl.MoodNailDataModel;
import com.example.administrator.ding.presenter.IBaseNetRequestListener;
import com.example.administrator.ding.presenter.OnGetCheckResultListener;
import com.example.administrator.ding.view.IMoodView;
import com.example.administrator.ding.view.fragments.MoodNailFragment;
import com.example.administrator.ding.widgt.EditInfoDialog;

public class MoodPresenterImpl {

    private MoodNailDataModel mModel;
    private IMoodView mView;

    public MoodPresenterImpl(IMoodView mView) {
        mModel = new MoodNailDataModel(((MoodNailFragment)mView).getContext());
        this.mView = mView;
    }

    /**
     * 获取初始数据
     */
    public void getInitialData() {
        mView.getInitialCracksSuccess(mModel.getAllNailCrackLocation());
        mView.getInitialMoodGoodNailsSuccess(mModel.getAllGoodNailHeadLocation());
        mView.getInitialMoodBadNailsSuccess(mModel.getAllBadNailHeadLocation());
    }

    /**
     * 检查放置点是否有效
     * @param x
     * @param y
     * @param d
     */
    public void checkIsPlaceValid(int x, int y, int[] d) {
        if (mModel.isPlaceValid(x, y, d[0])) {
            mView.placeValid(x, y, d);
        } else {
            mView.placeInValid();
        }
    }

    /**
     * 提交信息至服务器
     * @param nail
     */
    public void submitGoodNailEditInfoToServer(final MoodGoodNail nail, final EditInfoDialog editInfoDialog) {
        mModel.saveGoodNailEditInfoToServer(nail, new OnGetCheckResultListener() {
            @Override
            public void onSuccess() {
                mView.submitGoodNailFirstEditInfoSuccess(nail, editInfoDialog);
            }

            @Override
            public void onFailed() {
                mView.submitGoodNailFirstEditInfoFailed(editInfoDialog);
            }

            @Override
            public void onNotPassSensitiveWordsCheck() {
                mView.submitGoodNailFirstEditInfoNotPass();
            }
        });
    }

    /**
     * 提交信息到本地
     * @param nail
     */
    public void submitGoodNailEditInfoToLocal(MoodGoodNail nail) {
        mModel.saveGoodNailEditInfoToLocal(nail);
        mModel.updateDate(3, 1, 1, "yyyy-MM-dd EEE");
        mModel.updateDate(4, 1, 1, "yyyy-MM");
    }

    /**
     * 提交信息到服务器
     * @param badNail
     * @param crack
     */
    public void submitBadNailEditInfoToServer(final MoodBadNail badNail, final Crack crack, final int nailHeadMarginLeft, final int nailHeadMarginTop) {
        mModel.insertBadNailInfoToServer(badNail, crack, new OnGetCheckResultListener() {
            @Override
            public void onSuccess() {
                mView.submitBadNailFirstEditInfoSuccess(badNail, crack, nailHeadMarginLeft, nailHeadMarginTop);
            }

            @Override
            public void onFailed() {
                mView.submitBadNailFirstEditInfoFailed();
            }

            @Override
            public void onNotPassSensitiveWordsCheck() {
                mView.submitBadNailFirstEditInfoNotPass();
            }
        });
    }

    /**
     * 提交信息到本地
     * @param nail
     */
    public void submitBadNailEditInfoToLocal(MoodBadNail nail, Crack crack) {
        mModel.saveBadNailInfoToLocal(nail);
        mModel.saveCrackInfoToLocal(crack);
        // 更新每日
        mModel.updateDate(3, 0, 1, "yyyy-MM-dd EEE");
        mModel.updateDate(4, 0, 1, "yyyy-MM");
    }

    /**
     * 拔出好运钉子，更新服务器数据
     * @param nail
     */
    public void updateGoodNailFromServer(final MoodGoodNail nail, final View v) {
        mModel.updateGoodNailFromServer(nail, new IBaseNetRequestListener() {
            @Override
            public void onSuccess() {
                mView.updateGoodNailSuccess(nail, v);
            }

            @Override
            public void onFailed() {
                mView.updateGoodNailFailed();
            }
        });
    }

    /**
     * 拔出好运钉子，更新本地数据
     * @param nail
     */
    public void updateGoodNailFromLocal(MoodGoodNail nail) {
        mModel.updateGoodNailFromLocal(nail.getX(), nail.getY(), nail.getLastDate());
        mModel.updateDate(3, 1, 0,"yyyy-MM-dd EEE");
        mModel.updateDate(4, 1, 0,"yyyy-MM");
    }

    /**
     * 拔出厄运钉子，更新服务器数据
     * @param nail
     */
    public void updateBadNailFromServer(final MoodBadNail nail, final View v) {
        mModel.updateBadNailFromServer(nail, new IBaseNetRequestListener() {
            @Override
            public void onSuccess() {
                mView.updateBadNailSuccess(nail, v);
            }

            @Override
            public void onFailed() {
                mView.updateBadNailFailed();
            }
        });
    }

    /**
     * 拔出厄运钉子，更新本地数据
     * @param nail
     */
    public void updateBadNailFromLocal(MoodBadNail nail) {
        mModel.updateBadNailFromLocal(nail.getX(), nail.getY(), nail.getLastDate());
        mModel.updateDate(3, 0, 0,"yyyy-MM-dd EEE");
        mModel.updateDate(4, 0, 0,"yyyy-MM");
    }

    public void getGoodNailHeadDetails(int x, int y) {
        mView.onGetGoodNailHeadDetails(mModel.getGoodNailHeadDetails(x, y));
    }

    public void getBadNailHeadDetails(int x, int y) {
        mView.onGetBadNailHeadDetails(mModel.getBadNailHeadDetails(x, y));
    }

    public void getCrackDetails(int x, int y) {
        mView.onGetCrackDetails(mModel.getCrackDetails(x, y));
    }
}
