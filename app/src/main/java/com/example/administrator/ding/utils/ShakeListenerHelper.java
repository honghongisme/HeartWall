package com.example.administrator.ding.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeListenerHelper implements SensorEventListener {

    // 摇晃速度阈值
    private static final int SPEED_SHRESHOLD = 3000;
    // 两次检测的时间间隔
    private static final int UPDATE_INTERVAL_TIME = 100;
    // 传感器管理器
    private SensorManager sensorManager;
    // 传感器
    private Sensor sensor;
    // 重力感应监听器
    private OnShakeListener onShakeListener;
    private Context mContext;
    // 手机上一个位置时重力感应坐标
    private float lastX, lastY, lastZ;
    // 上次检测时间
    private long lastUpdateTime;

    // 构造器
    public ShakeListenerHelper(Context c) {
        // 获得监听对象
        mContext = c;
        start();
    }

    // 开始
    public void start() {
        // 获得传感器管理器
        sensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            // 获得重力传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        // 注册
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }

    }

    // 停止检测
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    // 设置重力感应监听器
    public void setOnShakeListener(OnShakeListener onShakeListener) {
        this.onShakeListener = onShakeListener;
    }

    // 重力感应器感应获得变化数据
    @Override
    public void onSensorChanged(SensorEvent event) {
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPDATE_INTERVAL_TIME)
            return;
        // 重新记计时，现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;

        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;
        // 计算速度
        double speed = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000);
        // 达到速度阀值
        if(speed > SPEED_SHRESHOLD) {
            onShakeListener.onShake(speed);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnShakeListener {
        void onShake(double speed);
    }

}
