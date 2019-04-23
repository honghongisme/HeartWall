package com.example.administrator.ding.model.entry;

/**
 * 计划钉子时间记录（周/年）
 */
public class PlanDate {

    /**
     * 用户id
     */
    private int id;
    /**
     * 日期
     */
    private String date;
    /**
     * 钉下的数量
     */
    private int nailNum;
    /**
     * 拔出的数量
     */
    private int pullNum;

    public PlanDate() { }

    public PlanDate(int id, String date, int nailNum, int pullNum) {
        this.id = id;
        this.date = date;
        this.nailNum = nailNum;
        this.pullNum = pullNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNailNum() {
        return nailNum;
    }

    public void setNailNum(int nailNum) {
        this.nailNum = nailNum;
    }

    public int getPullNum() {
        return pullNum;
    }

    public void setPullNum(int pullNum) {
        this.pullNum = pullNum;
    }
}
