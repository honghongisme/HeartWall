package com.example.administrator.ding.fragments;

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
import com.example.administrator.ding.activities.LookCommentDetailActivity;
import com.example.administrator.ding.base.BaseFragment;
import com.example.administrator.ding.bean.*;
import com.example.administrator.ding.config.MyApplication;
import com.example.administrator.ding.factory.model.MoodNailDataModel;
import com.example.administrator.ding.listener.OnGetCheckResultListener;
import com.example.administrator.ding.listener.OnGetRequestResultListener;
import com.example.administrator.ding.utils.CrackCountHelper;
import com.example.administrator.ding.utils.DateUtil;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.utils.ShakeListenerHelper;
import com.example.administrator.ding.widgt.*;

import java.util.ArrayList;

public class MoodNailFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 菜单工具编号
     */
    private static final int TOOL_GOOD_NAIL = 0;
    private static final int TOOL_BAD_NAIL = 1;
    private static final int TOOL_HAMMER = 2;
    private static final int TOOL_CLAW_HAMMER = 3;
    private static final int TOOL_LOOK_DETAILS = 4;
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
     * 工具菜单子项标题
     */
    private final String[] toolMenuTitleArrays = {"好运钉子", "厄运钉子", "锤子", "羊角锤", "查看"};
    /**
     * 工具功能描述
     */
    private final String[] toolFuncDescArrays = {"“ 钉下钉子，记录<b><tt> 愉快 </b></tt>的事，它将会一直留在墙面上，经常点击看看，让你拥有一天的好心情 ”",
            "“ 当你遇到<b><tt> 无法忍受的坏事 </b></tt>时，钉下一颗钉子，未来你可以拔出它。 ”",
            "“ <b><tt> 钉 </b></tt>下一颗钉子 ”",
            "“ <b><tt> 拔 </b></tt>出一颗钉子 ”",
            "“ 可以<b><tt> 查看 </b></tt>墙面上钉子的某些信息 ”"};
    /**
     * 工具额外描述
     */
    private final String[] toolExtraArrays = {"“ 遇到让你心情愉悦的事时，不妨钉下这颗钉子 ”",
            "“ 钉下钉子时试试多挥动几次 ”",
            "“ 当你钉下坏事钉子时，不妨试着多挥动几次 ”",
            "“ 拔出厄运钉子时，墙面会留下难看的洞 ”",
            "“ 这个还用解释吗？ ”"};
    /**
     * 可添加节点的根布局
     */
    private AddViewsFrameLayout nailNailCustomFl;
    /**
     * 可移动钉子ImageView
     */
    private MoveImageView moveImageView;
    /**
     * 可移动钉子的位置信息类
     */
    private NailLocationParams nailLocation;
    /**
     * 当前选中的钉子编号(-1为无)
     */
    private int currentSelectNailId = -1;
    /**
     * 当前选中的工具编号
     */
    private int currentSelectToolId = 4;
    /**
     * model层
     */
    private MoodNailDataModel model;
    /**
     * intent里的用户信息
     */
    private User user;
    /**
     * 记录摇晃次数的dialog
     */
    private Dialog countShakeNumDialog;
    /**
     * 记录摇晃次数的dialog的view
     */
    private View countShakeNumDialogLayout;
    /**
     * 传感器
     */
    private ShakeListenerHelper shakeListenerHelper;
    /**
     * 晃动速度数组
     */
    private ArrayList<Double> shakeSpeedList;
    /**
     * 是否公开
     */
    private boolean isVisible;
    /**
     * 是否匿名
     */
    private boolean isAnonymous;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        nailNailCustomFl = (AddViewsFrameLayout) inflater.inflate(R.layout.activity_nail, null);
        nailNailCustomFl.setBackgroundResource(R.drawable.mood_bg);

        user = ((MyApplication)(getActivity().getApplication())).getUser();
        model = new MoodNailDataModel(getContext());

        initHandler();
        setMenu();
        setMoveImageViewListener();
        addViews();

        return nailNailCustomFl;
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_INSERT_GOOD:
                        setGoodNailHeadImage((MoodGoodNail) msg.obj);
                        resetMoveImageView();
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
                        resetMoveImageView();
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
     * 设置菜单
     */
    private void setMenu() {
        ArcMenu arcMenu = nailNailCustomFl.findViewById(R.id.arcMenu);
        TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.mood_tool_menu_image_res_id);
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
                TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.mood_tool_func_image_res_id);
                int resId = typedArray.getResourceId(pos, 0);
                typedArray.recycle();
                popToolFuncDescDialog(resId, toolMenuTitleArrays[pos], toolFuncDescArrays[pos], toolExtraArrays[pos]);
            }
        });
    }

    /**
     * 设置拖动钉子的监听
     */
    private void setMoveImageViewListener() {
        moveImageView = nailNailCustomFl.findViewById(R.id.move_iv);
        moveImageView.setClickNailListener(new MoveImageView.OnClickNailListener() {
            @Override
            public void getClickPointLocation(final int centerPointX, final int centerPointY) {
                if (currentSelectToolId == TOOL_HAMMER) {
                    final int[] imageSize = model.getVectorImageSizeByRes(R.drawable.ic_mood_good_head_nail);
                    if (model.isPlaceValid(centerPointX, centerPointY, imageSize[0])) {
                        if (NetStateCheckHelper.isNetWork(getContext())) {
                            isVisible = false;
                            isAnonymous = false;
                            // 弹出编辑框
                            final String date = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
                            final EditInfoDialog editInfoDialog = new EditInfoDialog(getContext());
                            editInfoDialog.show();
                            //                editInfoDialog.setData(1, date, R.drawable.mood_dialog_bg);
                            editInfoDialog.setData(1, date);
                            editInfoDialog.setVisibleCheckBox();
                            if (currentSelectNailId == TOOL_GOOD_NAIL) {
                                // 钉下好运钉子
                                editInfoDialog.setDialogBtnClickListener(new EditInfoDialog.DialogBtnClickListener() {
                                    @Override
                                    public void doConfirm(String text) {
                                        showProgress("更新数据中，请稍候...");
                                        int left = centerPointX - imageSize[0] / 2;
                                        int top = centerPointY - imageSize[1] / 2;
                                        final MoodGoodNail nail = new MoodGoodNail(user.getId(), left, top, date, null, text, 0, 1, 1);
                                        if (!isVisible) nail.setVisibility(0);
                                        if (!isAnonymous) nail.setAnonymous(0);

                                        model.saveGoodNailEditInfoToServer(nail, new OnGetCheckResultListener() {
                                            @Override
                                            public void onSuccess() {
                                                editInfoDialog.dismiss();
                                                model.saveGoodNailEditInfoToLocal(nail);
                                                model.updateDate(3, 1, 1, "yyyy-MM-dd EEE");
                                                model.updateDate(4, 1, 1, "yyyy-MM");

                                                Message msg = new Message();
                                                msg.obj = nail;
                                                sendLoadingMessage(REQUEST_INSERT_GOOD, msg);
                                            }

                                            @Override
                                            public void onFailed() {
                                                editInfoDialog.dismiss();
                                                sendLoadingMessage(REQUEST_FAILED, null);
                                            }

                                            @Override
                                            public void onCheckFailed() {
                                                sendLoadingMessage(REQUEST_INPUT_SENSITIVE, null);
                                            }
                                        });
                                    }
                                    @Override
                                    public void doCancel() {
                                        editInfoDialog.dismiss();
                                    }

                                    @Override
                                    public void isVisible(boolean isChecked) {
                                        isVisible = isChecked;
                                    }

                                    @Override
                                    public void isAnonymous(boolean isChecked) {
                                        isAnonymous = isChecked;
                                    }
                                });
                            } else if (currentSelectNailId == TOOL_BAD_NAIL) {
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
                                        isVisible = isChecked;
                                    }

                                    @Override
                                    public void isAnonymous(boolean isChecked) {
                                        isAnonymous = isChecked;
                                    }
                                });

                            } else {
                                NetStateCheckHelper.setNetworkMethod(getContext());
                            }
                        }
                    }else {
                        showShortToast("此处不可用！");
                    }
                } else {
                    showShortToast("请选用3号锤子");
                }
            }

            @Override
            public void getMoveLocation(int left, int top) {
                nailLocation.setXY(left, top);
            }
        });
    }

    /**
     * 向根布局frameLayout里添加节点
     */
    private void addViews() {
        final TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.mood_crack_image_res_id);
        // 加载裂缝
        ArrayList<Crack> cracks = model.getAllNailCrackLocation();
        int resId = -1;
        for (Crack crack: cracks) {
            if (crack.getResId() != -1) {
                resId = typedArray.getResourceId(crack.getResId(), 0);
                setImageViewParams(crack.getX(), crack.getY(), resId, "crack");
            }
        }
        typedArray.recycle();

        // 加载好运钉子钉帽
        ArrayList<MoodGoodNail> moodGoodNails = model.getAllGoodNailHeadLocation();
        for (MoodGoodNail nail : moodGoodNails) {
            setImageViewParams(nail.getX(), nail.getY(), R.drawable.ic_mood_good_head_nail, "good");
        }

        // 加载厄运钉子钉帽
        ArrayList<MoodBadNail> moodBadNails = model.getAllBadNailHeadLocation();
        for (MoodBadNail nail: moodBadNails) {
            setImageViewParams(nail.getX(), nail.getY(), R.drawable.ic_mood_bad_head_nail, "bad");
        }

        // 刷新视图树
        nailNailCustomFl.requestLayout();
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
        TypedArray typedArray = getContext().getResources().obtainTypedArray(R.array.mood_tool_menu_image_res_id);
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
        if (selectedToolId == TOOL_GOOD_NAIL || selectedToolId == TOOL_BAD_NAIL) {
            currentSelectNailId = selectedToolId;
            // 重置位置信息类
            nailLocation = new NailLocationParams(getContext());
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(nailLocation.getLeft(), nailLocation.getTop(),0,0);
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
     * 设置好运钉子钉帽并保存信息
     * @param nail
     */
    private void setGoodNailHeadImage(MoodGoodNail nail) {
        //设置钉帽图片
        setImageViewParams(nail.getX(), nail.getY(), R.drawable.ic_mood_good_head_nail, "good");
        nailNailCustomFl.requestLayout();
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
        nailNailCustomFl.addView(imageView, nailNailCustomFl.getChildCount(), params);
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
                nailNailCustomFl.removeView(imageView);
            }
        }, duration);
    }

    /**
     * 弹出摇晃计数框
     */
    private void popCountShakeNumDialog(final int x, final int y, final String date, final String record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        countShakeNumDialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_count_shake_num, null);
        countShakeNumDialog = builder.create();
        countShakeNumDialog.show();
        countShakeNumDialog.getWindow().setContentView(countShakeNumDialogLayout);
        // 开启传感器
        startSensor();
        TextView finishBtn = countShakeNumDialogLayout.findViewById(R.id.finish);
        TextView cancelBtn = countShakeNumDialogLayout.findViewById(R.id.cancel);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countShakeNumDialog.dismiss();
                closeSensor();
                showProgress("更新数据中，请稍等...");

                int[] nailHeadImageSize = model.getVectorImageSizeByRes(R.drawable.ic_mood_bad_head_nail);
                final int nailHeadMarginLeft = x - nailHeadImageSize[0] / 2;
                final int nailHeadMarginTop = y - nailHeadImageSize[1] / 2;
                final MoodBadNail nail = new MoodBadNail(user.getId(), nailHeadMarginLeft, nailHeadMarginTop, date, null, record, 0, 1, 1, 0);
                if (!isVisible) nail.setVisibility(0);
                if (!isAnonymous) nail.setAnonymous(0);

                CrackCountHelper helper = new CrackCountHelper(shakeSpeedList);
                int crackImageResId = helper.getCrackImageResId();
                Crack crack = null;
                if (crackImageResId != -1) {
                    int[] crackImageSize = model.getNormalImageSizeByRes(crackImageResId);
                    int crackMarginLeft = x - crackImageSize[0] / 2;
                    int crackMarginTop = y - crackImageSize[1] / 2;
                    System.out.println("----------------helper.getCrackImageResIdFromTypedArray() = " + helper.getCrackImageResIdFromTypedArray() );
                    crack = new Crack(user.getId(), crackMarginLeft, crackMarginTop, date, shakeSpeedList.size(), helper.getPower(), helper.getCrackImageResIdFromTypedArray());
                }

                final Crack temp = crack;
                // 保存钉帽和裂缝的信息
                model.insertBadNailInfoToServer(nail, crack, new OnGetCheckResultListener() {
                    @Override
                    public void onSuccess() {
                        model.saveBadNailInfoToLocal(nail);
                        model.saveCrackInfoToLocal(temp);
                        // 更新每日
                        model.updateDate(3, 0, 1, "yyyy-MM-dd EEE");
                        model.updateDate(4, 0, 1, "yyyy-MM");
                        Message msg = new Message();
                        msg.obj = temp;
                        msg.arg1 = nailHeadMarginLeft;
                        msg.arg2 = nailHeadMarginTop;
                        sendLoadingMessage(REQUEST_INSERT_BAD, msg);
                    }

                    @Override
                    public void onFailed() {
                        sendLoadingMessage(REQUEST_FAILED, null);
                    }

                    @Override
                    public void onCheckFailed() {
                        sendLoadingMessage(REQUEST_INPUT_SENSITIVE, null);
                    }
                });
                resetMoveImageView();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countShakeNumDialog.dismiss();
                closeSensor();
            }
        });
    }

    /**
     * 更新计数dialog的数字
     */
    private void updateDialogShakeNum() {
        TextView num = countShakeNumDialogLayout.findViewById(R.id.num);
        num.setText((Integer.parseInt(num.getText().toString()) + 1) + "");
    }

    /**
     * 开启传感器
     */
    private void startSensor() {
        shakeListenerHelper = new ShakeListenerHelper(getContext());
        shakeSpeedList = new ArrayList<>();
        shakeListenerHelper.setOnShakeListener(new ShakeListenerHelper.OnShakeListener() {
            @Override
            public void onShake(double speed) {
                shakeSpeedList.add(speed);
                updateDialogShakeNum();
            }
        });
        shakeListenerHelper.start();
    }

    /**
     * 关闭传感器
     */
    private void closeSensor() {
        shakeListenerHelper.stop();
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
        nailNailCustomFl.myAddViewInLayout(imageView, params);
    }

    @Override
    public void onClick(final View v) {
        if(currentSelectToolId == TOOL_LOOK_DETAILS) {
            if (v.getTag() != null) {
                String[] tag  = ((String) v.getTag()).split("\\+");
                int x = Integer.parseInt(tag[0]);
                int y = Integer.parseInt(tag[1]);
                if ("good".equals(tag[2])) {
                    MoodGoodNail nail = model.getGoodNailHeadDetails(x, y);
                    Intent i = new Intent(getContext(), LookCommentDetailActivity.class);
                    i.putExtra("name", getTopShowName(user.getName(), nail.getVisibility(), nail.getAnonymous()));
                    i.putExtra("date", nail.getFirstDate());
                    i.putExtra("content", nail.getRecord());
                    i.putExtra("type", "good");
                    startActivity(i);
                }else if ("bad".equals(tag[2])){
                    MoodBadNail nail = model.getBadNailHeadDetails(x, y);
                    Intent i = new Intent(getContext(), LookCommentDetailActivity.class);
                    i.putExtra("id", user.getId());
                    i.putExtra("x", x);
                    i.putExtra("y", y);
                    i.putExtra("name", getTopShowName(user.getName(), nail.getVisibility(), nail.getAnonymous()));
                    i.putExtra("date", nail.getFirstDate());
                    i.putExtra("content", nail.getRecord());
                    i.putExtra("visible", nail.getVisibility());
                    i.putExtra("type", "bad");
                    startActivity(i);
                }else if ("crack".equals(tag[2])) {
                    Crack info = model.getCrackDetails(x, y);
                    popCrackDetails(info.getDate(), info.getNum(), info.getPower());
                }
            }
        }else if (currentSelectToolId == TOOL_CLAW_HAMMER) {
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

                                    MoodGoodNail nail = new MoodGoodNail(user.getId(), x, y, null, DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"), null, 0, 0, 0);
                                    model.updateGoodNailFromServer(nail, new OnGetRequestResultListener() {
                                        @Override
                                        public void onSuccess() {
                                            model.updateGoodNailFromLocal(x, y, DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"));
                                            model.updateDate(3, 1, 0,"yyyy-MM-dd EEE");
                                            model.updateDate(4, 1, 0,"yyyy-MM");
                                            Message msg = new Message();
                                            msg.obj = v;
                                            sendLoadingMessage(REQUEST_UPDATE_GOOD, msg);
                                        }

                                        @Override
                                        public void onFailed() {
                                            sendLoadingMessage(REQUEST_FAILED, null);
                                        }

                                    });
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

                                    MoodBadNail nail = new MoodBadNail(user.getId(), x, y, null, DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"), null, 0, 0, 0, 0);
                                    model.updateBadNailFromServer(nail, new OnGetRequestResultListener() {
                                        @Override
                                        public void onSuccess() {
                                            model.updateBadNailFromLocal(x, y, DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss"));
                                            model.updateDate(3, 0, 0,"yyyy-MM-dd EEE");
                                            model.updateDate(4, 0, 0,"yyyy-MM");
                                            Message msg = new Message();
                                            msg.obj = v;
                                            sendLoadingMessage(REQUEST_UPDATE_BAD, msg);
                                        }

                                        @Override
                                        public void onFailed() {
                                            sendLoadingMessage(REQUEST_FAILED, null);
                                        }

                                    });
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
