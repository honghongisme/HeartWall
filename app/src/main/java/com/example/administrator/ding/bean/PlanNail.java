package com.example.administrator.ding.bean;

/**
 * 墙上的正常钉子（计划，积极）
 */
public class PlanNail {

    /**
     * 用户id
     */
    private int id;
    /**
     * 坐标轴上x坐标
     */
    private int x;
    /**
     * 坐标轴上y坐标
     */
    private int y;
    /**
     * 钉下时间
     */
    private String date;
    /**
     * 钉下时编辑的内容
     */
    private String record;

    public PlanNail(int id, int x, int y, String date, String record) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.date = date;
        this.record = record;
    }

    public PlanNail(){}

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

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
