package com.example.administrator.ding.view.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.base.OperateNailFragment;
import com.example.administrator.ding.utils.*;
import com.example.administrator.ding.view.activities.LookCommentDetailActivity;
import com.example.administrator.ding.config.MyApplication;
import com.example.administrator.ding.model.entities.*;
import com.example.administrator.ding.presenter.impl.MoodPresenterImpl;
import com.example.administrator.ding.view.IMoodView;
import com.example.administrator.ding.widgt.*;

import java.util.ArrayList;

public class MoodNailFragment extends OperateNailFragment implements View.OnClickListener, IMoodView {

    /**
     * 工具编号
     */
    private static final int TOOL_GOOD_NAIL = 3;
    private static final int TOOL_BAD_NAIL = 4;

    /**
     * 网络请求状态
     */
    private static final int REQUEST_FAILED = 5;
    private static final int REQUEST_INSERT_GOOD = 6;
    private static final int REQUEST_INSERT_BAD = 7;
    private static final int REQUEST_UPDATE_GOOD = 8;
    private static final int REQUEST_UPDATE_BAD = 9;
    private static final int REQUEST_INPUT_SENSITIVE = 10;

    /**
     * 当前选中的钉子编号(-1为无)
     */
    private int mCurrentSelectNailId = -1;

    /**
     * intent里的用户信息
     */
    private User mUser;
    /**
     * 记录摇晃次数的dialog
     */
    private Dialog mCountShakeNumDialog;
    /**
     * 记录摇晃次数的dialog的view
     */
    private View mCountShakeNumDialogLayout;
    /**
     * 传感器
     */
    private ShakeListenerHelper mShakeListenerHelper;
    /**
     * 晃动速度数组
     */
    private ArrayList<Double> mShakeSpeedList;
    /**
     * 是否公开
     */
    private boolean mIsVisible;
    /**
     * 是否匿名
     */
    private boolean mIsAnonymous;
    private MoodPresenterImpl mPresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootFrameLayout = (AddViewsFrameLayout) inflater.inflate(R.layout.activity_nail, null);
        mRootFrameLayout.setBackgroundResource(R.drawable.mood_bg);
        mPresenter = new MoodPresenterImpl(this);

        initData();
        initHandler();
        setMenu(R.array.mood_tool_menu_image_res_id, R.array.mood_tool_func_image_res_id);
        setMoveImageViewListener();

        mPresenter.getInitialData();
        // 刷新视图树
        mRootFrameLayout.requestLayout();

        return mRootFrameLayout;
    }

    public void initData() {
        toolMenuTitleArrays = new String[]{ "查看", "锤子", "羊角锤", "好运钉子", "厄运钉子",};
        toolFuncDescArrays = new String[]{"“ 可以<b><tt> 查看 </b></tt>墙面上钉子的某些信息 ”",
                "“ <b><tt> 钉 </b></tt>下一颗钉子 ”",
                "“ <b><tt> 拔 </b></tt>出一颗钉子 ”",
                "“ 钉下钉子，记录<b><tt> 愉快 </b></tt>的事，它将会一直留在墙面上，经常点击看看，让你拥有一天的好心情 ”",
                "“ 当你遇到<b><tt> 无法忍受的坏事 </b></tt>时，钉下一颗钉子，未来你可以拔出它。 ”"};
        toolExtraArrays = new String[]{"“ 这个还用解释吗？ ”",
                "“ 当你钉下坏事钉子时，不妨试着多挥动几次 ”",
                "“ 拔出厄运钉子时，墙面会留下难看的洞 ”",
                "“ 遇到让你心情愉悦的事时，不妨钉下这颗钉子 ”",
                "“ 钉下钉子时试试多挥动几次 ”",};
        mUser = ((MyApplication)(getActivity().getApplication())).getUser();
        mCurrentSelectToolId = 4;
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_INSERT_GOOD:
                        setGoodNailHeadImage((MoodGoodNail) msg.obj);
                        hideAndClearMoveImageView();
                        hideProgress();
                        showLongToast("数据更新成功！");
                        break;
                    case REQUEST_INSERT_BAD:
                        if (msg.obj != null) {
                            Crack crack = (Crack) msg.obj;
                            TypedArray typed = getContext().getResources().obtainTypedArray(R.array.mood_crack_image_res_id);
                            setImageViewParams(crack.getX(), crack.getY(), typed.getResourceId(crack.getResId(), 0), "crack");
                            typed.recycle();
                        }
                        setImageViewParams(msg.arg1, msg.arg2, R.drawable.ic_mood_bad_head_nail, "bad");
                        hideAndClearMoveImageView();
                        hideProgress();
                        showShortToast("数据更新成功！");
                        showAnimation();
                        break;
                    case REQUEST_UPDATE_BAD:
                        hideProgress();
                        deleteNodeFromLayout((View) msg.obj);
                        showShortToast("拔出成功！");
                        break;
                    case REQUEST_UPDATE_GOOD:
                        hideProgress();
                        deleteNodeFromLayout((View) msg.obj);
                        showShortToast("拔出成功！");
                        break;
                    case REQUEST_FAILED:
                        hideProgress();
                        showLongToast("网络异常，请检查网络设置");
                        break;
                    case REQUEST_INPUT_SENSITIVE:
                        hideProgress();
                        showLongToast("输入文本存在敏感词汇，请重新输入");
                        break;
                }
            }
        };
    }

    /**
     * 设置拖动钉子的监听
     */
    private void setMoveImageViewListener() {
        mMoveIv = mRootFrameLayout.findViewById(R.id.move_iv);
        mMoveIv.setClickNailListener(new MoveImageView.OnClickNailListener() {
            @Override
            public void getClickPointLocation(final int centerPointX, final int centerPointY) {
                if (mCurrentSelectToolId == TOOL_HAMMER) {
                    final int[] imageSize = ImageSizeUtil.getVectorImageSizeByRes(getContext(), R.drawable.ic_mood_good_head_nail);
                    mPresenter.checkIsPlaceValid(centerPointX, centerPointY, imageSize);
                } else {
                    showShortToast("请选用3号锤子");
                }
            }

            @Override
            public void getMoveLocation(int left, int top) {
                mNailLocation.setXY(left, top);
            }
        });
    }

    /**
     * 设置好运钉子钉帽并保存信息
     * @param nail
     */
    private void setGoodNailHeadImage(MoodGoodNail nail) {
        //设置钉帽图片
        setImageViewParams(nail.getX(), nail.getY(), R.drawable.ic_mood_good_head_nail, "good");
        mRootFrameLayout.requestLayout();
    }

    /**
     * 开启动画
     */
    private void showAnimation() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.nail_nail_anim);
        imageView.setLayoutParams(params);
        mRootFrameLayout.addView(imageView, mRootFrameLayout.getChildCount(), params);
        final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        int duration = 0;
        imageView.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            duration += animationDrawable.getDuration(i);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRootFrameLayout.removeView(imageView);
            }
        }, duration);
    }

    /**
     * 弹出摇晃计数框
     */
    private void popCountShakeNumDialog(final int x, final int y, final String date, final String record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        mCountShakeNumDialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_count_shake_num, null);
        mCountShakeNumDialog = builder.create();
        mCountShakeNumDialog.show();
        mCountShakeNumDialog.getWindow().setContentView(mCountShakeNumDialogLayout);
        // 开启传感器
        startSensor();
        TextView finishBtn = mCountShakeNumDialogLayout.findViewById(R.id.finish);
        TextView cancelBtn = mCountShakeNumDialogLayout.findViewById(R.id.cancel);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountShakeNumDialog.dismiss();
                closeSensor();
                showProgress("更新数据中，请稍等...");

                int[] nailHeadImageSize = ImageSizeUtil.getVectorImageSizeByRes(getContext(), R.drawable.ic_mood_bad_head_nail);
                final int nailHeadMarginLeft = x - nailHeadImageSize[0] / 2;
                final int nailHeadMarginTop = y - nailHeadImageSize[1] / 2;
                final MoodBadNail nail = new MoodBadNail(mUser.getId(), nailHeadMarginLeft, nailHeadMarginTop, date, null, record, 0, 1, 1, 0);
                if (!mIsVisible) nail.setVisibility(0);
                if (!mIsAnonymous) nail.setAnonymous(0);

                CrackCountHelper helper = new CrackCountHelper(mShakeSpeedList);
                int crackImageResId = helper.getCrackImageResId();
                Crack crack = null;
                if (crackImageResId != -1) {
                    int[] crackImageSize = ImageSizeUtil.getNormalImageSizeByRes(getContext(), crackImageResId);
                    int crackMarginLeft = x - crackImageSize[0] / 2;
                    int crackMarginTop = y - crackImageSize[1] / 2;
                    crack = new Crack(mUser.getId(), crackMarginLeft, crackMarginTop, date, mShakeSpeedList.size(), helper.getPower(), helper.getCrackImageResIdFromTypedArray());
                }

                // 保存钉帽和裂缝的信息
                mPresenter.submitBadNailEditInfoToServer(nail, crack, nailHeadMarginLeft, nailHeadMarginTop);

                hideAndClearMoveImageView();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountShakeNumDialog.dismiss();
                closeSensor();
            }
        });
    }

    /**
     * 更新计数dialog的数字
     */
    private void updateDialogShakeNum() {
        TextView num = mCountShakeNumDialogLayout.findViewById(R.id.num);
        num.setText((Integer.parseInt(num.getText().toString()) + 1) + "");
    }

    /**
     * 开启传感器
     */
    private void startSensor() {
        mShakeListenerHelper = new ShakeListenerHelper(getContext());
        mShakeSpeedList = new ArrayList<>();
        mShakeListenerHelper.setOnShakeListener(new ShakeListenerHelper.OnShakeListener() {
            @Override
            public void onShake(double speed) {
                mShakeSpeedList.add(speed);
                updateDialogShakeNum();
            }
        });
        mShakeListenerHelper.start();
    }

    /**
     * 关闭传感器
     */
    private void closeSensor() {
        mShakeListenerHelper.stop();
    }

    /**
     * 设置 R.drawable.head_nail 图片的view
     * @param MarginLeft
     * @param marginTop
     * @param resId
     */
    private void setImageViewParams(int MarginLeft, int marginTop, int resId, String type) {
        // -1表是不设置图片
        if (resId == -1) return;
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(resId);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(MarginLeft, marginTop, 0, 0);
        imageView.setOnClickListener(this);
        imageView.setTag(MarginLeft + "+" + marginTop + "+" + type);
        mRootFrameLayout.myAddViewInLayout(imageView, params);
    }

    @Override
    public void onClick(final View v) {
        if(mCurrentSelectToolId == TOOL_LOOK_DETAILS) {
            if (v.getTag() != null) {
                String[] tag  = ((String) v.getTag()).split("\\+");
                int x = Integer.parseInt(tag[0]);
                int y = Integer.parseInt(tag[1]);
                if ("good".equals(tag[2])) {
                    mPresenter.getGoodNailHeadDetails(x, y);
                }else if ("bad".equals(tag[2])){
                    mPresenter.getBadNailHeadDetails(x, y);
                }else if ("crack".equals(tag[2])) {
                    mPresenter.getCrackDetails(x, y);
                }
            }
        }else if (mCurrentSelectToolId == TOOL_CLAW_HAMMER) {
            if (v.getTag() != null) {
                String[] tag  = ((String) v.getTag()).split("\\+");
                final int x = Integer.parseInt(tag[0]);
                final int y = Integer.parseInt(tag[1]);
                if ("good".equals(tag[2])) {
                    // 确认是否拔出
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("提示").setMessage("真的要拔出钉子吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    showProgress("更新数据中，请稍等...");

                                    MoodGoodNail nail = new MoodGoodNail(mUser.getId(), x, y, null, DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"), null, 0, 0, 0);
                                    // 拔出钉子，更新信息
                                    mPresenter.updateGoodNailFromServer(nail, v);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            }).show();
                }else if ("bad".equals(tag[2])){
                    // 确认是否拔出
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("提示").setMessage("真的要拔出钉子吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    showProgress("更新数据中，请稍等...");

                                    MoodBadNail nail = new MoodBadNail(mUser.getId(), x, y, null, DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"), null, 0, 0, 0, 0);
                                    mPresenter.updateBadNailFromServer(nail, v);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            }).show();
                }else if ("crack".equals(tag[2])) {
                    showShortToast("这是一个劣迹斑斑的洞， 或许...");
                }
            }
        }
    }


    /**
     * 只有对外可见时，匿名才有效
     * @param name
     * @param visible
     * @param anonymous
     * @return
     */
    private String getTopShowName(String name, int visible, int anonymous) {
        if (visible == 1 && anonymous == 1) {
            return "匿名用户";
        }
        return name;
    }

    /**
     * 弹出crack信息dialog
     * @param date
     * @param num
     * @param power
     */
    private void popCrackDetails(String date, int num, int power) {
        popDetailsDialog(date, "你在这天钉下这颗钉子，你敲了 " + num + " 下，用了 " + power + " 的力度。");
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
    public void getInitialCracksSuccess(ArrayList<Crack> cracks) {
        final TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.mood_crack_image_res_id);
        // 加载裂缝
        int resId = -1;
        for (Crack crack: cracks) {
            if (crack.getResId() != -1) {
                resId = typedArray.getResourceId(crack.getResId(), 0);
                setImageViewParams(crack.getX(), crack.getY(), resId, "crack");
            }
        }
        typedArray.recycle();
    }

    @Override
    public void getInitialMoodGoodNailsSuccess(ArrayList<MoodGoodNail> moodGoodNails) {
        // 加载好运钉子钉帽
        for (MoodGoodNail nail : moodGoodNails) {
            setImageViewParams(nail.getX(), nail.getY(), R.drawable.ic_mood_good_head_nail, "good");
        }
    }

    @Override
    public void getInitialMoodBadNailsSuccess(ArrayList<MoodBadNail> moodBadNails) {
        // 加载厄运钉子钉帽
        for (MoodBadNail nail: moodBadNails) {
            setImageViewParams(nail.getX(), nail.getY(), R.drawable.ic_mood_bad_head_nail, "bad");
        }
    }

    @Override
    public void placeValid(final int centerPointX, final int centerPointY, final int[] imageSize) {
        if (NetStateCheckHelper.isNetWork(getContext())) {
            mIsVisible = false;
            mIsAnonymous = false;
            // 弹出编辑框
            final String date = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
            final EditInfoDialog editInfoDialog = new EditInfoDialog(getContext());
            editInfoDialog.show();
            editInfoDialog.setData(1, date);
            editInfoDialog.setVisibleCheckBox();
            if (mCurrentSelectNailId == TOOL_GOOD_NAIL) {
                // 钉下好运钉子
                editInfoDialog.setDialogBtnClickListener(new EditInfoDialog.DialogBtnClickListener() {
                    @Override
                    public void doConfirm(String text) {
                        showProgress("更新数据中，请稍候...");
                        int left = centerPointX - imageSize[0] / 2;
                        int top = centerPointY - imageSize[1] / 2;
                        final MoodGoodNail nail = new MoodGoodNail(mUser.getId(), left, top, date, null, text, 0, 1, 1);
                        if (!mIsVisible) nail.setVisibility(0);
                        if (!mIsAnonymous) nail.setAnonymous(0);
                        mPresenter.submitGoodNailEditInfoToServer(nail, editInfoDialog);
                    }
                    @Override
                    public void doCancel() {
                        editInfoDialog.dismiss();
                    }

                    @Override
                    public void isVisible(boolean isChecked) {
                        mIsVisible = isChecked;
                    }

                    @Override
                    public void isAnonymous(boolean isChecked) {
                        mIsAnonymous = isChecked;
                    }
                });
            } else if (mCurrentSelectNailId == TOOL_BAD_NAIL) {
                // 钉下厄运钉子
                editInfoDialog.setDialogBtnClickListener(new EditInfoDialog.DialogBtnClickListener() {
                    @Override
                    public void doConfirm(String text) {
                        editInfoDialog.dismiss();
                        popCountShakeNumDialog(centerPointX, centerPointY, date, text);
                    }

                    @Override
                    public void doCancel() {
                        editInfoDialog.dismiss();
                    }

                    @Override
                    public void isVisible(boolean isChecked) {
                        mIsVisible = isChecked;
                    }

                    @Override
                    public void isAnonymous(boolean isChecked) {
                        mIsAnonymous = isChecked;
                    }
                });

            } else {
                NetStateCheckHelper.setNetworkMethod(getContext());
            }
        }
    }

    @Override
    public void placeInValid() {
        showShortToast("此处不可用！");
    }

    @Override
    public void submitGoodNailFirstEditInfoSuccess(MoodGoodNail moodGoodNail, EditInfoDialog editInfoDialog) {
        editInfoDialog.dismiss();
        mPresenter.submitGoodNailEditInfoToLocal(moodGoodNail);

        Message msg = mHandler.obtainMessage(REQUEST_INSERT_GOOD);
        msg.obj = moodGoodNail;
        mHandler.sendMessage(msg);
    }

    @Override
    public void submitGoodNailFirstEditInfoFailed(EditInfoDialog editInfoDialog) {
        editInfoDialog.dismiss();

        mHandler.sendEmptyMessage(REQUEST_FAILED);
    }

    @Override
    public void submitGoodNailFirstEditInfoNotPass() {
        mHandler.sendEmptyMessage(REQUEST_INPUT_SENSITIVE);
    }

    @Override
    public void submitBadNailFirstEditInfoSuccess(MoodBadNail moodBadNail, Crack crack, int nailHeadMarginLeft, int nailHeadMarginTop) {
        mPresenter.submitBadNailEditInfoToLocal(moodBadNail, crack);

        Message msg = mHandler.obtainMessage(REQUEST_INSERT_BAD);
        msg.obj = crack;
        msg.arg1 = nailHeadMarginLeft;
        msg.arg2 = nailHeadMarginTop;
        mHandler.sendMessage(msg);
    }

    @Override
    public void submitBadNailFirstEditInfoFailed() {
        mHandler.sendEmptyMessage(REQUEST_FAILED);
    }

    @Override
    public void submitBadNailFirstEditInfoNotPass() {
        mHandler.sendEmptyMessage(REQUEST_INPUT_SENSITIVE);
    }

    @Override
    public void updateGoodNailSuccess(MoodGoodNail nail, View v) {
        mPresenter.updateGoodNailFromLocal(nail);

        Message msg = mHandler.obtainMessage(REQUEST_UPDATE_GOOD);
        msg.obj = v;
        mHandler.sendMessage(msg);
    }

    @Override
    public void updateGoodNailFailed() {
        mHandler.sendEmptyMessage(REQUEST_FAILED);
    }

    @Override
    public void updateBadNailSuccess(MoodBadNail nail, View v) {
        mPresenter.updateBadNailFromLocal(nail);

        Message msg = mHandler.obtainMessage(REQUEST_UPDATE_BAD);
        msg.obj = v;
        mHandler.sendMessage(msg);
    }

    @Override
    public void updateBadNailFailed() {
        mHandler.sendEmptyMessage(REQUEST_FAILED);
    }

    @Override
    public void onGetGoodNailHeadDetails(MoodGoodNail nail) {
        Intent i = new Intent(getContext(), LookCommentDetailActivity.class);
        i.putExtra("name", getTopShowName(mUser.getName(), nail.getVisibility(), nail.getAnonymous()));
        i.putExtra("date", nail.getFirstDate());
        i.putExtra("content", nail.getRecord());
        i.putExtra("type", "good");
        startActivity(i);
    }

    @Override
    public void onGetBadNailHeadDetails(MoodBadNail nail) {
        Intent i = new Intent(getContext(), LookCommentDetailActivity.class);
        i.putExtra("id", mUser.getId());
        i.putExtra("x", nail.getX());
        i.putExtra("y", nail.getY());
        i.putExtra("name", getTopShowName(mUser.getName(), nail.getVisibility(), nail.getAnonymous()));
        i.putExtra("date", nail.getFirstDate());
        i.putExtra("content", nail.getRecord());
        i.putExtra("visible", nail.getVisibility());
        i.putExtra("type", "bad");
        startActivity(i);
    }

    @Override
    public void onGetCrackDetails(Crack info) {
        popCrackDetails(info.getDate(), info.getNum(), info.getPower());
    }

    @Override
    public boolean isSelectedNail(int selectedToolId) {
        if (selectedToolId == TOOL_GOOD_NAIL || selectedToolId == TOOL_BAD_NAIL) {
            mCurrentSelectNailId = selectedToolId;
            return true;
        }
        return false;
    }
}
