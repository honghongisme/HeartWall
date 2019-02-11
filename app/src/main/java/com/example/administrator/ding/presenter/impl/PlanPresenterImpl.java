package com.example.administrator.ding.presenter.impl;

import android.view.View;
import com.example.administrator.ding.model.entities.PlanPullNail;
import com.example.administrator.ding.presenter.OnGetInitNailListListener;
import com.example.administrator.ding.presenter.OnGetRequestResultListener;
import com.example.administrator.ding.model.entities.Nail;
import com.example.administrator.ding.model.entities.PlanNail;
import com.example.administrator.ding.view.IPlanView;
import com.example.administrator.ding.model.impl.PlanNailDataModel;
import com.example.administrator.ding.view.fragments.PlanNailFragment;

import java.util.List;

public class PlanPresenterImpl {

    private PlanNailDataModel mModel;
    private IPlanView mView;

    public PlanPresenterImpl(IPlanView mView) {
        mModel = new PlanNailDataModel(((PlanNailFragment)mView).getContext());
        this.mView = mView;
    }

    /**
     * 获取界面初始化数据
     */
    public void getInitialData() {
        mModel.getChildViewsLocation(new OnGetInitNailListListener() {
            @Override
            public void onSuccess(List<Nail> nailList) {
                mView.getInitialDataSuccess(nailList);
            }
        });
    }

    /**
     * 检查放置点是否有效
     * @param rawX
     * @param rawY
     * @param d
     */
    public void checkIsPlaceValid(int rawX, int rawY, int[] d) {
        if (mModel.isPlaceValid(rawX, rawY, d[0])) {
            mView.placeValid(rawX, rawY, d);
        } else {
            mView.placeInValid();
        }
    }

    /**
     * 提交初次编辑的信息到服务器
     * @param nail
     */
    public void submitFirstEditInfoToServer(final PlanNail nail) {
        mModel.saveFirstEditInfoToServer(nail, new OnGetRequestResultListener() {
            @Override
            public void onSuccess() {
                mView.submitFirstEditInfoSuccess(nail);
            }

            @Override
            public void onFailed() {
                mView.submitFirstEditInfoFailed();
            }
        });
    }

    /**
     * 提交初次编辑的信息到本地
     * @param nail
     */
    public void submitFirstEditInfoToLocal(PlanNail nail) {
        mModel.saveFirstEditInformationToLocal(nail);
        mModel.updateDateData(2, 1, "yyyy-MM-dd EEE");
        mModel.updateDateData(3, 1, "yyyy-MM");
    }

    /**
     * 提交最后一次编辑的信息到服务器
     * @param planNail
     * @param planPullNail
     * @param v 被点击的view
     */
    public void submitLastEditInfoToServer(final PlanNail planNail, final PlanPullNail planPullNail, final View v) {
        mModel.saveLastEditInfoToServer(planNail, planPullNail, new OnGetRequestResultListener() {
            @Override
            public void onSuccess() {
                mView.submitLastEditInfoSuccess(planNail.getX(), planNail.getY(), planPullNail.getLastDate(), planPullNail.getLastRecord(), v);
            }

            @Override
            public void onFailed() {
                mView.submitLastEditInfoFailed();
            }
        });
    }

    /**
     * 提交最后一次的信息到本地
     * @param x
     * @param y
     * @param date
     * @param text
     */
    public void submitLastEditInfoToLocal(int x, int y, String date, String text) {
        mModel.saveLastEditInfoToLocal(x, y, date, text);
        mModel.updateDateData(2, 0, "yyyy-MM-dd EEE");
        mModel.updateDateData(3, 0, "yyyy-MM");
    }

    /**
     * 获取钉帽的信息，即第一次编辑的信息
     * @param x
     * @param y
     */
    public void getFirstEditInfo(int x, int y) {
        mView.getFirstEditInfoSuccess(mModel.getFirstEditInfo(x, y));
    }
}
