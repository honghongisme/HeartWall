package com.example.administrator.ding.module.nail.mood;

import android.view.View;

import com.example.administrator.ding.model.entry.Crack;
import com.example.administrator.ding.model.entry.MoodBadNail;
import com.example.administrator.ding.model.entry.MoodGoodNail;
import com.example.administrator.ding.base.IBaseNetRequestListener;
import com.example.administrator.ding.module.nail.OnGetCheckResultListener;
import com.example.administrator.ding.widgt.EditInfoDialog;

public class MoodPresenterImpl implements IMoodContract.Presenter{

    private MoodNailDataModel mModel;
    private IMoodContract.View mView;

    public MoodPresenterImpl(IMoodContract.View mView) {
        mModel = new MoodNailDataModel(((MoodNailFragment)mView).getContext());
        this.mView = mView;
    }


    @Override
    public void getInitialData() {
        mView.getInitialCracksSuccess(mModel.getAllNailCrackLocation());
        mView.getInitialMoodGoodNailsSuccess(mModel.getAllGoodNailHeadLocation());
        mView.getInitialMoodBadNailsSuccess(mModel.getAllBadNailHeadLocation());
    }

    @Override
    public void checkIsPlaceValid(int x, int y, int[] d) {
        if (mModel.isPlaceValid(x, y, d[0])) {
            mView.placeValid(x, y, d);
        } else {
            mView.placeInValid();
        }
    }

    @Override
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

    @Override
    public void submitGoodNailEditInfoToLocal(MoodGoodNail nail) {
        mModel.saveGoodNailEditInfoToLocal(nail);
        mModel.updateDate(3, 1, 1, "yyyy-MM-dd EEE");
        mModel.updateDate(4, 1, 1, "yyyy-MM");
    }

    @Override
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

    @Override
    public void submitBadNailEditInfoToLocal(MoodBadNail nail, Crack crack) {
        mModel.saveBadNailInfoToLocal(nail);
        mModel.saveCrackInfoToLocal(crack);
        // 更新每日
        mModel.updateDate(3, 0, 1, "yyyy-MM-dd EEE");
        mModel.updateDate(4, 0, 1, "yyyy-MM");
    }

    @Override
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

    @Override
    public void updateGoodNailFromLocal(MoodGoodNail nail) {
        mModel.updateGoodNailFromLocal(nail.getX(), nail.getY(), nail.getLastDate());
        mModel.updateDate(3, 1, 0,"yyyy-MM-dd EEE");
        mModel.updateDate(4, 1, 0,"yyyy-MM");
    }

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

    @Override
    public void updateBadNailFromLocal(MoodBadNail nail) {
        mModel.updateBadNailFromLocal(nail.getX(), nail.getY(), nail.getLastDate());
        mModel.updateDate(3, 0, 0,"yyyy-MM-dd EEE");
        mModel.updateDate(4, 0, 0,"yyyy-MM");
    }

    @Override
    public void getGoodNailHeadDetails(int x, int y) {
        mView.onGetGoodNailHeadDetails(mModel.getGoodNailHeadDetails(x, y));
    }

    @Override
    public void getBadNailHeadDetails(int x, int y) {
        mView.onGetBadNailHeadDetails(mModel.getBadNailHeadDetails(x, y));
    }

    @Override
    public void getCrackDetails(int x, int y) {
        mView.onGetCrackDetails(mModel.getCrackDetails(x, y));
    }
}
