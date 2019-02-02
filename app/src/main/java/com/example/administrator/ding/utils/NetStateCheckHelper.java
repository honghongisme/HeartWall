package com.example.administrator.ding.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetStateCheckHelper {

    /**
     * 判断手机的网络状态（是否联网）
     * @param context
     * @return
     */
    public static int getNetWorkState(Context context) {
        // 网络状态初始值
        int resultType = -1;  //-1(当前网络异常，没有联网)
        // 通过上下文得到系统服务，参数为网络连接服务，返回网络连接的管理类
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 通过网络管理类的实例得到联网日志的状态，返回联网日志的实例
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        // 判断联网日志是否为空
        if (activeNetworkInfo == null) {
            // 状态为空当前网络异常，没有联网
            return resultType;
        }
        // 拿到使用的网络类型
        int type = activeNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            // 网络类型为运营商（移动/联通/电信）
            resultType = 0;
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            // 网络类型为WIFI（无线网）
            resultType = 1;
        }
        return resultType;
    }

    /**
     * 判断网络是否为wifi
     * @param context
     * @return
     */
    public static boolean isExistWiFiNet(Context context) {
        if (getNetWorkState(context) == 1) return true;
        return false;
    }

    /**
     * 判断网络是否为流量
     * @param context
     * @return
     */
    public static boolean isExistDateNet(Context context) {
        if (getNetWorkState(context) == 0) return true;
        return false;
    }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isNetWork(Context context) {
        int type = getNetWorkState(context);
        if (type != 0 && type != 1) {
            return false;
        }
        return true;
    }

    /**
     * 打开设置网络界面
     * @param context
     */
    public static void setNetworkMethod(final Context context){
        //提示对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络未连接,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent=null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }

}
