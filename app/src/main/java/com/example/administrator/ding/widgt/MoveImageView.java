package com.example.administrator.ding.widgt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import android.widget.FrameLayout;
import com.example.administrator.ding.utils.SystemResHelper;

/**
 * 随手指移动的ImageView
 * Created by Administrator on 2018/6/10.
 */

public class MoveImageView extends android.support.v7.widget.AppCompatImageView{


    private int lastX = 0;
    private int lastY = 0;
    private OnClickNailListener onClickNailListener = null;
    private Context context;
    private boolean isClick;


    public MoveImageView(Context context) {
        super(context);
        this.context = context;
    }

    public MoveImageView(Context context, AttributeSet attrs ) {
        super(context, attrs);
        this.context = context;
    }

    public MoveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * view随着手指拖动而移动,遇到边界时不能继续朝滑。
     * 当view被点击时，回调坐标
     * 本项目里这个view父布局即根布局，getLeft即该view到屏幕左侧边缘的位置
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getRawY();
        int y = (int)event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                isClick = false;
                int distanceX = x - lastX;
                int distanceY = y - lastY;
                int[] screenSize = SystemResHelper.getScreenSize(context);
                int left, top, right, bottom;
                left = (int) (event.getRawX() + distanceX);
                right = (int) (left + getWidth());
                top = (int) (event.getRawY() + distanceY);
                bottom = (int) (top + getHeight());
                // 设置边界
                if (right > screenSize[0]) {
                    right = screenSize[0];
                    left = right - getWidth();
                } else if (left < 0){
                    left = 0;
                    right = getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = getHeight();
                } else if (bottom > screenSize[1]) {
                    bottom = screenSize[1];
                    top = bottom - getHeight();
                }
                setFrame(left, top, right, bottom);
                break;
            case MotionEvent.ACTION_UP:
                if (isClick) {
                    int[] centerPoint = countCenterPoint((int)event.getRawX(), (int)event.getRawY(), (int)event.getX(), (int)event.getY(), getWidth(), getHeight());
                    onClickNailListener.getClickPointLocation(centerPoint[0], centerPoint[1]);
                    isClick = false;
                } else {
                    // 若move
                    onClickNailListener.getMoveLocation(getLeft(), getTop());
                }
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    public void setClickNailListener(OnClickNailListener onClickNailListener) {
        this.onClickNailListener = onClickNailListener;
    }

    public interface OnClickNailListener{
        /**
         * 点击时回调触摸图片的中心位置
         * @param centerPointX 图片中心坐标
         * @param centerPointY
         */
        void getClickPointLocation(int centerPointX, int centerPointY);

        /**
         * 移动时回调触摸图片左上边距
         * @param left
         * @param top
         */
        void getMoveLocation(int left, int top);
    }

    /**
     * 根据触摸点计算图片的中心坐标
     * @param rawX 触摸点到屏幕左边缘的距离
     * @param rawY
     * @param x 触摸点到图片左边缘的距离
     * @param y
     * @param width 图片的宽高
     * @param height
     * @return { 图片中心点横坐标， 图片中心点纵坐标}
     */
    public int[] countCenterPoint(int rawX, int rawY, int x, int y, int width, int height) {
        int tempX = rawX + (width/2 - x);
        int tempY = rawY + (height/2 - y);
        return new int[]{tempX, tempY};
    }
}
