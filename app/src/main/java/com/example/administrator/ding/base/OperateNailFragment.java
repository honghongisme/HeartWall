package com.example.administrator.ding.base;

import android.content.res.TypedArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.bean.NailLocationParams;
import com.example.administrator.ding.widgt.AddViewsFrameLayout;
import com.example.administrator.ding.widgt.ArcMenu;
import com.example.administrator.ding.widgt.MoveImageView;
import com.example.administrator.ding.widgt.ToolFuncDescDialog;

public abstract class OperateNailFragment extends SimpleFragment {


    /**
     * 工具编号
     */
    protected static final int TOOL_LOOK_DETAILS = 0;
    protected static final int TOOL_HAMMER = 1;
    protected static final int TOOL_CLAW_HAMMER = 2;

    /**
     * 工具菜单子项标题
     */
    protected String[] toolMenuTitleArrays;
    /**
     * 工具功能描述
     */
    protected String[] toolFuncDescArrays;
    /**
     * 工具额外描述
     */
    protected String[] toolExtraArrays;
    /**
     * 可添加节点的根布局
     */
    protected AddViewsFrameLayout mRootFrameLayout;
    /**
     * 可移动钉子的位置信息类
     */
    protected NailLocationParams mNailLocation;
    /**
     * 可移动钉子ImageView
     */
    protected MoveImageView mMoveIv;
    /**
     * 当前选中的工具编号
     */
    protected int mCurrentSelectToolId;


    /**
     * 设置菜单
     * @param menuToolImageResId
     * @param funcToolImageResId
     */
    protected void setMenu(final int menuToolImageResId, final int funcToolImageResId) {
        ArcMenu arcMenu = mRootFrameLayout.findViewById(R.id.arcMenu);
        TypedArray typedArray = getContext().getResources().obtainTypedArray(menuToolImageResId);
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
                if (mNailLocation != null) {
                    recoverMoveImageViewLocation();
                }
            }
        });
        arcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClickMenu(View view, int pos, String extraInfo) {
                setCurrentToolLocation(menuToolImageResId, pos);
            }
        });
        arcMenu.setOnMenuItemLongClickListener(new ArcMenu.OnMenuItemLongClickListener() {
            @Override
            public void onLongClickItem(View view, int pos) {
                TypedArray typedArray = getContext().getResources().obtainTypedArray(funcToolImageResId);
                int resId = typedArray.getResourceId(pos, 0);
                typedArray.recycle();
                popToolFuncDescDialog(resId, toolMenuTitleArrays[pos], toolFuncDescArrays[pos], toolExtraArrays[pos]);
            }
        });
    }

    /**
     * 设置当前选中的工具的图像位置并切换工具(右下角)
     * 整个视图树中index = 0是menu，index = 1是currentTool， index = 2是moveIv(不用时设置为gone)
     * @param selectedToolId 选中的工具
     */
    private void setCurrentToolLocation(int menuToolImageResId, int selectedToolId) {
        mCurrentSelectToolId = selectedToolId;
        // 移出上一个使用的tool
        mRootFrameLayout.removeViewAt(1);
        TypedArray typedArray = getContext().getResources().obtainTypedArray(menuToolImageResId);
        // 当前选中的工具
        ImageView currentSelectToolIv = new ImageView(getContext());
        currentSelectToolIv.setImageResource(typedArray.getResourceId(selectedToolId, 0));
        currentSelectToolIv.setOnClickListener((View.OnClickListener) this);
   //     currentSelectToolIv.setTag(selectedToolId);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.setMargins(0,0,20,20);
        // 设置工具在父viewGroup里的固定位置1上，便于移出上一个显示的工具（xml里同理）
        mRootFrameLayout.myAddViewInLayout(currentSelectToolIv, 1, params);
        // 当选中拔钉子和查看细节工具时，隐藏移动钉子并重置innerNail
        if (selectedToolId == TOOL_CLAW_HAMMER || selectedToolId == TOOL_LOOK_DETAILS) {
            hideAndClearMoveImageView();
        }
        // 当选中钉子时，再设置一层可移动的钉子iv
        if (isSelectedNail(selectedToolId)) {
            // 重置可移动钉子的位置
            resetMoveNailParams(typedArray.getResourceId(selectedToolId, 0));
        } else {
            //如果之前已选过钉子，则恢复位置
            if (mNailLocation != null) {
                recoverMoveImageViewLocation();
            }else {
                mMoveIv.setVisibility(View.GONE);
                // 如果是选中钉子，setLayoutParams本就会重绘视图树，这里是选中非钉子工具时
                mRootFrameLayout.requestLayout();
            }
        }
        typedArray.recycle();
    }

    /**
     * 重置可移动钉子到默认位置
     * @param needShowNailImage
     */
    public void resetMoveNailParams(int needShowNailImage) {
        mNailLocation = new NailLocationParams(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(mNailLocation.getLeft(), mNailLocation.getTop(),0,0);
        mMoveIv.setVisibility(View.VISIBLE);
        mMoveIv.setImageResource(needShowNailImage);
        mMoveIv.setLayoutParams(params);
    }

    /**
     * 每次点击menu的选项都会重绘视图树，因此要恢复钉子的位置
     */
    protected void recoverMoveImageViewLocation() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(mNailLocation.getLeft(), mNailLocation.getTop(),0,0);
        mMoveIv.setVisibility(View.VISIBLE);
        mMoveIv.setLayoutParams(params);
    }

    /**
     * 隐藏并清除钉子的位置信息
     */
    protected void hideAndClearMoveImageView() {
        mNailLocation = null;
        mMoveIv.setVisibility(View.GONE);
        mRootFrameLayout.requestLayout();
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

    public abstract boolean isSelectedNail(int selectedToolId);

}
