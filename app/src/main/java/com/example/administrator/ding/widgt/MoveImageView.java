package com.example.administrator.ding.widgt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.administrator.ding.utils.SystemResHelper;

/**
 * 随手指移动的ImageView
 * Created by Administrator on 2018/6/10.
 */

public class MoveImageView extends android.support.v7.widget.AppCompatImageView{


    private int lastX = 0;
    private int lastY = 0;

    private int x;
    private int y;

    private OnClickNailListener onClickNailListener = null;
    private Context context;


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
     * view随着手指拖动而移动
     * 当view被点击时，回调坐标
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                x = lastX;
                y = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int)event.getRawX() - lastX;
                int dy = (int)event.getRawY() - lastY;

                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                if(left < 0){
                    left = 0;
                    right = left + getWidth();
                }
                int[] screenSize = SystemResHelper.getScreenSize(context);
                int screenWidth = screenSize[0];
                if(right > screenWidth){
                    right = screenWidth;
                    left = right - getWidth();
                }
                if(top < 0){
                    top = 0;
                    bottom = top + getHeight();
                }
                int screenHeight = screenSize[1];
                if(bottom > screenHeight){
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
         //    layout(left, top, right, bottom);
                this.setFrame(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //自定义的view重写onTouchEvent方法后onClick事件会发生冲突，需要加上下面一句，onclick的入口
                performClick();
                //若click
                if(Math.abs(event.getRawX() - x) < 10 && Math.abs(event.getRawY() - y) < 10) {
                    int[] centerPoint = countCenterPoint((int)event.getRawX(), (int)event.getRawY(), (int)event.getX(), (int)event.getY(), getWidth(), getHeight());
                    onClickNailListener.getClickPointLocation(centerPoint[0], centerPoint[1]);
                } else {
                    //若move
                    onClickNailListener.getMoveLocation((int)(event.getRawX() - event.getX()), (int)(event.getRawY() - event.getY()));
                }
                break;
            default:
                break;
        }
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


    public void setLocation(int left, int top, int right, int bottom) {
        setFrame(left, top, right, bottom);
    }
}
