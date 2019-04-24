package com.example.administrator.ding.utils;

import com.example.administrator.ding.R;

import java.util.ArrayList;

public class CrackCountHelper {

    private ArrayList<Double> speedList;
    /**
     * 平均速度
     */
    private int averageSpeed;
    /**
     * 最大速度
     */
    private int maxSpeed;
    /**
     * 晃动次数
     */
    private int count;

    private int crackImageResIdFromTypedArray;


    public CrackCountHelper(ArrayList<Double> speedList) {
        this.speedList = speedList;
        // 加入初始值
        this.speedList.add(3000.00);
        this.count = speedList.size();
    }

    /**
     * 根据综合数据获取选择的裂缝图片id
     * @return 裂缝图片id
     */
    public int getCrackImageResId() {
        int result = -1;
        int num = -1;
        int power = getPower();
        System.out.println("------------------power = " + power);
        if (power < 5) {

        } else if (power < 7) {
            result = R.drawable.crack01;
            num = 0;
        } else if (power < 9) {
            result = R.drawable.crack02;
            num = 1;
        } else if (power <= 10) {
            result = R.drawable.crack03;
            num = 2;
        }
        System.out.println("------------------result = " + result);
        setCrackImageResIdFromTypedArray(num);
        return result;
    }

    /**
     * 获取TypedArray里的资源id
     * @return
     */
    public int getCrackImageResIdFromTypedArray() {
        return crackImageResIdFromTypedArray;
    }

    public void setCrackImageResIdFromTypedArray(int crackImageResIdFromTypedArray) {
        this.crackImageResIdFromTypedArray = crackImageResIdFromTypedArray;
    }

    /**
     * 根据speedList计算最大speed
     * @return
     */
    private double getMaxSpeed() {
        if (count == 0) return 0;
        double max = speedList.get(0);
        for (double n : speedList) {
            if (max < n) max = n;
        }
        return max;
    }

    /**
     * 根据speedList计算评平均speed
     * @return
     */
    private double getAverageSpeed() {
        float sum = 0;
        for(double n : speedList) {
            sum += n;
        }
        return sum == 0 ? 0 : sum/count;
    }

    /**
     * 根据计算结果获取力度值（1-10）
     *
     * 据统计，速度一般在3000-6000中，分10个等级的话，3000/10=3000，因此等级为(avSpeed - 3000) / 300，向上取整还要+1
     * @return
     */
    public int getPower() {
        return ((int) ((getAverageSpeed() - 3000) / 300)) + 1;
    }
}
