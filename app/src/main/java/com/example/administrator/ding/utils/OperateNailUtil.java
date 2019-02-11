package com.example.administrator.ding.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.graphics.drawable.VectorDrawableCompat;

public class OperateNailUtil {

    /**
     * 获取xml图片资源的宽高
     * @param res
     * @return
     */
    public static int[] getVectorImageSizeByRes(Context context, int res) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources(),
                res, context.getTheme());
        int width = vectorDrawableCompat.getIntrinsicWidth();
        int height = vectorDrawableCompat.getIntrinsicHeight();
        return new int[]{width, height};
    }

    /**
     * 获取普通图片格式资源的宽高
     * @param res
     * @return
     */
    public static int[] getNormalImageSizeByRes(Context context, int res) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(context.getResources(), res, options);
        return new int[]{options.outHeight, options.outWidth};
    }
}
