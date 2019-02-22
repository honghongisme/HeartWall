package com.example.administrator.ding.model.entities;

import android.content.Context;
import com.example.administrator.ding.utils.SystemResHelper;

/**
 * 可移动钉子位置信息类
 */
public class NailLocationParams {

    private int left;
    private int top;

    public NailLocationParams(Context context){
        // 让钉子默认位置在屏幕中间
        int[] screenSize = SystemResHelper.getScreenSize(context);
        left = screenSize[0]/2;
        top = screenSize[1]/2;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public void setXY(int x, int y) {
        this.left = x;
        this.top = y;
    }

}
