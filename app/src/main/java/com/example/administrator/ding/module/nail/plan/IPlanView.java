package com.example.administrator.ding.module.nail.plan;

import android.view.View;
import com.example.administrator.ding.bean.nail.Nail;
import com.example.administrator.ding.bean.nail.PlanNail;

import java.util.ArrayList;

public interface IPlanView {

    /**
     * 获得页面初始数据
     * @param nailList
     */
    void getInitialDataSuccess(ArrayList<Nail> nailList);

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
    void submitFirstEditInfoSuccess(PlanNail planNail);

    /**
     *  提交第一次编辑的信息失败
     */
    void submitFirstEditInfoFailed();

    /**
     * 提交最后一次编辑的信息成功
     */
    void submitLastEditInfoSuccess(int x, int y, String date, String text, View v);

    /**
     *  提交最后一次编辑的信息失败
     */
    void submitLastEditInfoFailed();

    /**
     * 获取第一次编辑的信息成功
     * @param info
     */
    void getFirstEditInfoSuccess(String[] info);
}
