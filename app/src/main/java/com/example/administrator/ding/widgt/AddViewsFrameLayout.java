package com.example.administrator.ding.widgt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 每次addView()会调用requestLayout，当频繁多次调用addView()，会使整个视图树的每个view进行measure和layout；
 * 可以将所有view addViewInLayout()后统一requestLayout()；
 *
 * viewGroup的addViewInLayout()方法是protect，因此要自定义viewGroup
 *
 * Created by Administrator on 2018/7/26.
 */

public class AddViewsFrameLayout extends FrameLayout {


    public AddViewsFrameLayout(@NonNull Context context) {
        super(context);
    }

    public AddViewsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddViewsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddViewsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void myAddViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        super.addViewInLayout(child,index,params);
    }

    public void myAddViewInLayout(View child, ViewGroup.LayoutParams params) {
        int index = this.getChildCount();
        myAddViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return super.addViewInLayout(child, index, params);
    }

}
