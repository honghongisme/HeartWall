package com.example.administrator.ding.fragments;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
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
import com.example.administrator.ding.R;
import com.example.administrator.ding.base.BaseFragment;
import com.example.administrator.ding.bean.*;
import com.example.administrator.ding.config.MyApplication;
import com.example.administrator.ding.factory.model.PlanNailDataModel;
import com.example.administrator.ding.listener.OnGetNailListListener;
import com.example.administrator.ding.listener.OnGetRequestResultListener;
import com.example.administrator.ding.utils.DateUtil;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.widgt.*;

import java.util.List;

public class PlanNailFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 工具编号
     */
    private static final int TOOL_NAIL = 0;
    private static final int TOOL_HAMMER = 1;
    private static final int TOOL_CLAW_HAMMER = 2;
    private static final int TOOL_LOOK_DETAILS = 3;
    /**
     * 网络请求状态
     */
    private static final int REQUEST_INSERT_SUCCESS = 4;
    private static final int REQUEST_DELETE_SUCCESS = 5;
    private static final int REQUEST_FAILED = 6;

    /**
     * 工具菜单子项标题
     */
    private final String[] toolMenuTitleArrays = {"计划钉子", "锤子", "羊角锤", "查看"};
    /**
     * 工具功能描述
     */
    private final String[] toolFuncDescArrays = {"“ 可以记录你的<b><tt> 计划 </b></tt>的钉子，可拔下。”",
            "“ <b><tt> 钉 </b></tt>下一颗钉子，记录你的计划 ”",
            "“ <b><tt> 拔 </b></tt>出一颗钉子，记录计划进度或完成情况 ”",
            "“ 可以<b><tt> 查看 </b></tt>墙面上钉子的信息 ”"};
    /**
     * 工具额外描述
     */
    private final String[] toolExtraArrays = {"“ 做一个会记录计划的人 ”",
            "“ 这真的是你要做的事情吗？ ”",
            "“ 你真的完成这件事了吗 ”",
            "“ 这个还用解释吗？ ”"};

    /**
     * 可移动钉子ImageView
     */
    private MoveImageView moveImageView;
    /**
     * 可移动钉子的位置信息类
     */
    private NailLocationParams nailLocation;
    /**
     * 当前选中的工具编号
     */
    private int currentSelectToolId = 3;
    /**
     * intent里的用户信息
     */
    private User user;
    /**
     * 可添加节点的根布局
     */
    private AddViewsFrameLayout nailNailCustomFl;
    /**
     * model层
     */
    private PlanNailDataModel model;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        nailNailCustomFl = (AddViewsFrameLayout) inflater.inflate(R.layout.activity_nail, null);
        nailNailCustomFl.setBackgroundResource(R.drawable.plan_bg);

        user = ((MyApplication)(getActivity().getApplication())).getUser();
        model = new PlanNailDataModel(getContext());

        initHandler();
        setMenu();
        setMoveImageViewListener();
        addViews();

        return nailNailCustomFl;
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_INSERT_SUCCESS:
                        setNailHeadInfo((PlanNail) msg.obj);
                        resetMoveImageView();
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

    private void setMenu() {
        ArcMenu arcMenu = nailNailCustomFl.findViewById(R.id.arcMenu);
        TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.plan_tool_menu_image_res_id);
        int toolCount = toolMenuTitleArrays.length;
        for (int i = 0; i < toolCount; i++) {
            arcMenu.addChildArcMenu(typedArray.getResourceId(i, 0), toolMenuTitleArrays[i], null);
        }
        typedArray.recycle();
        arcMenu.layoutChildMenu();
        arcMenu.setShowMenuBtnNum(toolCount);
        arcMenu.setOnMenuMainBtnClickListener(new ArcMenu.OnMenuMainBtnClickListener() {
            @Override
            public void onClick() {
                if (nailLocation != null) {
                    recoverMoveImageViewLocation();
                }
            }
        });
        arcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClickMenu(View view, int pos, String extraInfo) {
                setCurrentToolLocation(pos);
            }
        });
        arcMenu.setOnMenuItemLongClickListener(new ArcMenu.OnMenuItemLongClickListener() {
            @Override
            public void onLongClickItem(View view, int pos) {
                TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.plan_tool_func_image_res_id);
                int resId = typedArray.getResourceId(pos, 0);
                typedArray.recycle();
                popToolFuncDescDialog(resId, toolMenuTitleArrays[pos], toolFuncDescArrays[pos], toolExtraArrays[pos]);
            }
        });
    }

    private void setMoveImageViewListener() {
        moveImageView = nailNailCustomFl.findViewById(R.id.move_iv);
        moveImageView.setClickNailListener(new MoveImageView.OnClickNailListener() {
            @Override
            public void getClickPointLocation(final int centerPointX, final int centerPointY) {
                if (currentSelectToolId == TOOL_HAMMER) {
                    final int[] imageSize = model.getVectorImageSizeByRes(R.drawable.ic_plan_nail_head);
                    if (NetStateCheckHelper.isNetWork(getContext())) {
                        if (model.isPlaceValid(centerPointX, centerPointY, imageSize[0])) {
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
                                    final PlanNail planNail = new PlanNail(user.getId(), left, top, date, text);
                                    model.saveFirstEditInfoToServer(planNail, new OnGetRequestResultListener() {
                                        @Override
                                        public void onSuccess() {
                                            model.saveFirstEditInformationToLocal(planNail);
                                            model.updateDateData(2, 1, "yyyy-MM-dd EEE");
                                            model.updateDateData(3, 1, "yyyy-MM");

                                            Message message = new Message();
                                            message.obj = planNail;
                                            sendLoadingMessage(REQUEST_INSERT_SUCCESS, message);
                                        }

                                        @Override
                                        public void onFailed() {
                                            sendLoadingMessage(REQUEST_FAILED, null);
                                        }
                                    });
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
                        }else {
                            showShortToast("此处不可用！");
                        }
                    } else {
                        NetStateCheckHelper.setNetworkMethod(getContext());
                    }
                } else {
                    showShortToast("请选用2号锤子");
                }
            }

            @Override
            public void getMoveLocation(int left, int top) {
                nailLocation.setXY(left, top);
            }
        });
    }

    /**
     * 设置当前选中的工具的图像位置并切换工具
     * 整个视图树中index = 0是menu，index = 1是currentTool， index = 2是moveIv(不用时设置为gone)
     * @param selectedToolId 选中的工具
     */
    private void setCurrentToolLocation(int selectedToolId) {
        currentSelectToolId = selectedToolId;
        // 移出上一个使用的tool
        nailNailCustomFl.removeViewAt(1);
        //设置并添加显示当前工具
        TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.plan_tool_menu_image_res_id);
        // 当前选中的工具
        ImageView currentSelectToolIv = new ImageView(getContext());
        currentSelectToolIv.setImageResource(typedArray.getResourceId(selectedToolId, 0));
        currentSelectToolIv.setOnClickListener(this);
        currentSelectToolIv.setTag(selectedToolId);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.setMargins(0,0,20,20);
        // 设置工具在父viewGroup里的固定位置1上，便于移出上一个显示的工具（xml里同理）
        nailNailCustomFl.myAddViewInLayout(currentSelectToolIv, 1, params);
        // 当选中拔钉子和查看细节工具时，隐藏移动钉子并重置innerNail
        if (selectedToolId == TOOL_CLAW_HAMMER || selectedToolId == TOOL_LOOK_DETAILS) {
            resetMoveImageView();
        }
        // 当选中钉子时，再设置一层可移动的钉子iv
        if (selectedToolId == TOOL_NAIL) {
            // 重置位置信息类
            nailLocation = new NailLocationParams(getContext());
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(nailLocation.getLeft(),nailLocation.getTop(),0,0);
            moveImageView.setVisibility(View.VISIBLE);
            moveImageView.setImageResource(typedArray.getResourceId(selectedToolId, 0));
            moveImageView.setLayoutParams(params);
        }else {
            //如果之前已选过钉子，则恢复位置
            if (nailLocation != null) {
                recoverMoveImageViewLocation();
            }else {
                moveImageView.setVisibility(View.GONE);
                // 如果是选中钉子，setLayoutParams本就会重绘视图树，这里是选中非钉子工具时
                nailNailCustomFl.requestLayout();
            }
        }
        typedArray.recycle();
    }

    /**
     * 设置钉子钉帽并保存信息
     * @param nail
     */
    private void setNailHeadInfo(PlanNail nail) {
        //设置钉帽图片
        setImageViewParams(nail.getX(), nail.getY());
        nailNailCustomFl.requestLayout();
    }

    /**
     * 向根布局frameLayout里添加节点
     */
    private void addViews() {
        model.getChildViewsLocation(new OnGetNailListListener() {
            @Override
            public void onSuccess(List<Nail> nailList) {
                Nail nail = null;
                for (int i = 0; i < nailList.size(); i++) {
                    nail = nailList.get(i);
                    setImageViewParams(nail.getPointX(), nail.getPointY());
                }
            }
        });
        nailNailCustomFl.requestLayout();
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
        nailNailCustomFl.myAddViewInLayout(imageView, params);
    }

    @Override
    public void onClick(final View v) {
        if (currentSelectToolId == TOOL_LOOK_DETAILS) {
            if (v.getTag() != null) {
                String[] tag = ((String) v.getTag()).split("\\+");
                int x = Integer.parseInt(tag[0]);
                int y = Integer.parseInt(tag[1]);
                String[] info = model.getFirstEditInfo(x, y);
                popDetailsDialog(info[0], info[1]);
            }
        } else if (currentSelectToolId == TOOL_CLAW_HAMMER) {
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
                            PlanNail planNail = new PlanNail(user.getId(), x, y, null, null);
                            PlanPullNail planPullNail = new PlanPullNail(user.getId(), null, null, date, text);
                            model.saveLastEditInfoToServer(planNail, planPullNail, new OnGetRequestResultListener() {
                                @Override
                                public void onSuccess() {
                                    model.saveLastEditInfoToLocal(x, y, date, text);
                                    model.updateDateData(2, 0, "yyyy-MM-dd EEE");
                                    model.updateDateData(3, 0, "yyyy-MM");
                                    Message message = new Message();
                                    message.obj = v;
                                    sendLoadingMessage(REQUEST_DELETE_SUCCESS, message);
                                }

                                @Override
                                public void onFailed() {
                                    sendLoadingMessage(REQUEST_FAILED, null);
                                }

                            });
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
        nailNailCustomFl.removeView(view);
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

    /**
     * 每次点击menu的选项都会重绘视图树，因此要恢复钉子的位置
     */
    private void recoverMoveImageViewLocation() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(nailLocation.getLeft(),nailLocation.getTop(),0,0);
        moveImageView.setVisibility(View.VISIBLE);
        moveImageView.setLayoutParams(params);
    }

    /**
     * 重置可移动钉子
     */
    private void resetMoveImageView() {
        nailLocation = null;
        moveImageView.setVisibility(View.GONE);
        nailNailCustomFl.requestLayout();
    }

    /**
     * 弹出工具功能描述dialog
     * @param resId
     * @param title
     * @param toolDesc
     * @param extraTips
     */
    private void popToolFuncDescDialog(int resId, String title, String toolDesc, String extraTips) {
        ToolFuncDescDialog dialog = new ToolFuncDescDialog(getContext());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setData(resId, title, toolDesc, extraTips);
    }
}
