package com.example.administrator.ding.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.base.OperateNailFragment;
import com.example.administrator.ding.config.MyApplication;
import com.example.administrator.ding.model.entities.*;
import com.example.administrator.ding.presenter.impl.PlanPresenterImpl;
import com.example.administrator.ding.utils.DateUtil;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.utils.ImageSizeUtil;
import com.example.administrator.ding.view.IPlanView;
import com.example.administrator.ding.widgt.*;

import java.util.ArrayList;

public class PlanNailFragment extends OperateNailFragment implements View.OnClickListener, IPlanView {

    /**
     * 工具编号
     */
    private static final int TOOL_NAIL = 3;

    /**
     * 网络请求状态
     */
    private static final int REQUEST_INSERT_SUCCESS = 4;
    private static final int REQUEST_DELETE_SUCCESS = 5;
    private static final int REQUEST_FAILED = 6;
    /**
     * intent里的用户信息
     */
    private User mUser;
    private PlanPresenterImpl mPresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootFrameLayout = (AddViewsFrameLayout) inflater.inflate(R.layout.activity_nail, null);
        mRootFrameLayout.setBackgroundResource(R.drawable.plan_bg);
        mPresenter = new PlanPresenterImpl(this);

        initData();
        initHandler();
        setMenu(R.array.plan_tool_menu_image_res_id, R.array.plan_tool_func_image_res_id);
        setMoveImageViewListener();

        //获取数据
        mPresenter.getInitialData();
        // 刷新视图树
        mRootFrameLayout.requestLayout();

        return mRootFrameLayout;
    }

    public void initData() {
        toolMenuTitleArrays = new String[]{"查看", "锤子", "羊角锤","计划钉子",};
        toolFuncDescArrays = new String[]{"“ 可以<b><tt> 查看 </b></tt>墙面上钉子的信息 ”",
                "“ 可以记录你的<b><tt> 计划 </b></tt>的钉子，可拔下。”",
                "“ <b><tt> 钉 </b></tt>下一颗钉子，记录你的计划 ”",
                "“ <b><tt> 拔 </b></tt>出一颗钉子，记录计划进度或完成情况 ”"};
        toolExtraArrays = new String[]{"“ 这个还用解释吗？ ”",
                "“ 做一个会记录计划的人 ”",
                "“ 这真的是你要做的事情吗？ ”",
                "“ 你真的完成这件事了吗 ”"};
        mUser = ((MyApplication)(getActivity().getApplication())).getUser();
        mCurrentSelectToolId = 3;
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_INSERT_SUCCESS:
                        setNailHeadInfo((PlanNail) msg.obj);
                        hideAndClearMoveImageView();
                        hideProgress();
                        showLongToast("钉下成功！");
                        break;
                    case REQUEST_DELETE_SUCCESS:
                        deleteNodeFromLayout((View) msg.obj);
                        hideProgress();
                        showShortToast("拔出成功！详情请查看背包");
                        break;
                    case REQUEST_FAILED:
                        hideProgress();
                        showLongToast("网络异常，请检查网络设置");
                        break;
                }
            }
        };
    }

    private void setMoveImageViewListener() {
        mMoveIv = mRootFrameLayout.findViewById(R.id.move_iv);
        mMoveIv.setClickNailListener(new MoveImageView.OnClickNailListener() {
            @Override
            public void getClickPointLocation(final int centerPointX, final int centerPointY) {
                if (mCurrentSelectToolId == TOOL_HAMMER) {
                    final int[] imageSize = ImageSizeUtil.getVectorImageSizeByRes(getContext(), R.drawable.ic_plan_nail_head);
                    if (NetStateCheckHelper.isNetWork(getContext())) {
                        mPresenter.checkIsPlaceValid(centerPointX, centerPointY, imageSize);
                    } else {
                        NetStateCheckHelper.setNetworkMethod(getContext());
                    }
                } else {
                    showShortToast("请选用2号锤子");
                }
            }

            @Override
            public void getMoveLocation(int left, int top) {
                mNailLocation.setXY(left, top);
            }
        });
    }

    @Override
    public boolean isSelectedNail(int selectedToolId) {
        return (selectedToolId == TOOL_NAIL);
    }

    /**
     * 设置钉子钉帽并保存信息
     * @param nail
     */
    private void setNailHeadInfo(PlanNail nail) {
        //设置钉帽图片
        setImageViewParams(nail.getX(), nail.getY());
        mRootFrameLayout.requestLayout();
    }

    /**
     * 设置 R.drawable.head_nail 图片的view
     * @param MarginLeft
     * @param marginTop
     */
    private void setImageViewParams(int MarginLeft, int marginTop) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.ic_plan_nail_head);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(MarginLeft, marginTop, 0, 0);
        imageView.setOnClickListener(this);
        imageView.setTag(MarginLeft + "+" + marginTop);
        mRootFrameLayout.myAddViewInLayout(imageView, params);
    }

    @Override
    public void onClick(final View v) {
        if (mCurrentSelectToolId == TOOL_LOOK_DETAILS) {
            if (v.getTag() != null) {
                String[] tag = ((String) v.getTag()).split("\\+");
                int x = Integer.parseInt(tag[0]);
                int y = Integer.parseInt(tag[1]);
                mPresenter.getFirstEditInfo(x, y);
            }
        } else if (mCurrentSelectToolId == TOOL_CLAW_HAMMER) {
            if (v.getTag() != null) {
                if (NetStateCheckHelper.isNetWork(getContext())) {
                    String[] tag = ((String) v.getTag()).split("\\+");
                    final int x = Integer.parseInt(tag[0]);
                    final int y = Integer.parseInt(tag[1]);

                    final String date = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
                    final EditInfoDialog dialog = new EditInfoDialog(getContext());
                    dialog.show();
                    dialog.setData(2, date);
                    dialog.setDialogBtnClickListener(new EditInfoDialog.DialogBtnClickListener() {
                        @Override
                        public void doConfirm(final String text) {
                            dialog.dismiss();
                            showProgress("更新中，请稍后...");
                            PlanNail planNail = new PlanNail(mUser.getId(), x, y, null, null);
                            PlanPullNail planPullNail = new PlanPullNail(mUser.getId(), null, null, date, text);
                            mPresenter.submitLastEditInfoToServer(planNail, planPullNail, v);
                        }

                        @Override
                        public void doCancel() {
                            dialog.dismiss();
                            showShortToast("取消编辑");
                        }

                        @Override
                        public void isVisible(boolean isChecked) {

                        }

                        @Override
                        public void isAnonymous(boolean isChecked) {

                        }
                    });

                } else {
                    NetStateCheckHelper.setNetworkMethod(getContext());
                }
            }
        }
    }

    /**
     * 删除根视图里的某个view
     * @param view
     */
    private void deleteNodeFromLayout(View view) {
        mRootFrameLayout.removeView(view);
    }

    /**
     * 弹出钉帽细节dialog
     * @param date
     * @param content
     */
    private void popDetailsDialog(String date, String content) {
        DetailInfoDialog dialog = new DetailInfoDialog(getContext());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setData(date, content);
    }

    @Override
    public void placeValid(final int centerPointX, final int centerPointY, final int[] imageSize) {
        final String date = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
        final EditInfoDialog dialog = new EditInfoDialog(getContext());
        dialog.show();
        dialog.setData(1, date);
        dialog.setDialogBtnClickListener(new EditInfoDialog.DialogBtnClickListener() {
            @Override
            public void doConfirm(String text) {
                dialog.dismiss();
                showProgress("正在更新，请稍后...");
                int left = centerPointX - imageSize[0] / 2;
                int top = centerPointY - imageSize[1] / 2;
                // 保存信息
                final PlanNail planNail = new PlanNail(mUser.getId(), left, top, date, text);
                mPresenter.submitFirstEditInfoToServer(planNail);
            }
            @Override
            public void doCancel() {
                dialog.dismiss();
                showShortToast("取消编辑");
            }

            @Override
            public void isVisible(boolean isChecked) {

            }

            @Override
            public void isAnonymous(boolean isChecked) {

            }
        });
    }

    @Override
    public void placeInValid() {
        showShortToast("此处不可用！");
    }

    @Override
    public void submitFirstEditInfoSuccess(PlanNail planNail) {
        // 保存信息到本地
        mPresenter.submitFirstEditInfoToLocal(planNail);
        // insert成功，发送消息刷新视图
        Message message = mHandler.obtainMessage(REQUEST_INSERT_SUCCESS);
        message.obj = planNail;
        mHandler.sendMessage(message);
    }

    @Override
    public void submitFirstEditInfoFailed() {
        mHandler.sendEmptyMessage(REQUEST_FAILED);
    }

    @Override
    public void submitLastEditInfoSuccess(int x, int y, String date, String text, View v) {
        mPresenter.submitLastEditInfoToLocal(x, y, date, text);

        Message message = mHandler.obtainMessage(REQUEST_DELETE_SUCCESS);
        message.obj = v;
        mHandler.sendMessage(message);
    }

    @Override
    public void submitLastEditInfoFailed() {
        mHandler.sendEmptyMessage(REQUEST_FAILED);
    }

    @Override
    public void getFirstEditInfoSuccess(String[] info) {
        popDetailsDialog(info[0], info[1]);
    }

    @Override
    public void getInitialDataSuccess(ArrayList<Nail> nailList) {
        for (Nail nail : nailList) {
            setImageViewParams(nail.getPointX(), nail.getPointY());
        }
        mRootFrameLayout.requestLayout();
    }
}
