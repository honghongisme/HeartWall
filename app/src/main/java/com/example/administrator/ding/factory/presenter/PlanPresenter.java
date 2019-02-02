package com.example.administrator.ding.factory.presenter;

import android.content.Context;
import com.example.administrator.ding.factory.IView.IPlanView;
import com.example.administrator.ding.factory.model.PlanNailDataModel;

public class PlanPresenter {

    private PlanNailDataModel mModel;
    private IPlanView mView;

    public PlanPresenter(IPlanView mView) {
        mModel = new PlanNailDataModel((Context)mView);
        this.mView = mView;
    }

}
