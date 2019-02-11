package com.example.administrator.ding.model.entities;

/**
 * 裂缝bean
 */
public class Crack {

    /**
     * 用户id
     */
    private int id;
    /**
     * x坐标
     */
    private int x;
    /**
     * y坐标
     */
    private int y;
    /**
     * 日期
     */
    private String date;
    /**
     * 钉钉子时挥动次数
     */
    private int num;
    /**
     * 钉子钉子时用的力度
     */
    private int power;
    /**
     * 图片资源id
     */
    private int resId;

    public Crack() {
    }

    public Crack(int id, int x, int y, String date, int num, int power, int resId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.date = date;
        this.num = num;
        this.power = power;
        this.resId = resId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
