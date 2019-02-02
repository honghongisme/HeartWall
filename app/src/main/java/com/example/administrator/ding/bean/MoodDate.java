package com.example.administrator.ding.bean;

/**
 * 情绪钉子日期记录
 */
public class MoodDate {

    /**
     * 用户id
     */
    private int id;
    /**
     * 日期
     */
    private String date;
    /**
     * 积极钉子订下的数量
     */
    private int goodNailNum;
    /**
     * 积极钉子拔出的数量
     */
    private int goodPullNum;
    /**
     * 消极钉子订下数量
     */
    private int badNailNum;
    /**
     * 消极订下拔出数量
     */
    private int badPullNailNum;

    public MoodDate() {
    }

    public MoodDate(int id, String date, int goodNailNum, int goodPullNum, int badNailNum, int badPullNailNum) {
        this.id = id;
        this.date = date;
        this.goodNailNum = goodNailNum;
        this.goodPullNum = goodPullNum;
        this.badNailNum = badNailNum;
        this.badPullNailNum = badPullNailNum;
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

    public int getGoodNailNum() {
        return goodNailNum;
    }

    public void setGoodNailNum(int goodNailNum) {
        this.goodNailNum = goodNailNum;
    }

    public int getGoodPullNum() {
        return goodPullNum;
    }

    public void setGoodPullNum(int goodPullNum) {
        this.goodPullNum = goodPullNum;
    }

    public int getBadNailNum() {
        return badNailNum;
    }

    public void setBadNailNum(int badNailNum) {
        this.badNailNum = badNailNum;
    }

    public int getBadPullNailNum() {
        return badPullNailNum;
    }

    public void setBadPullNailNum(int badPullNailNum) {
        this.badPullNailNum = badPullNailNum;
    }
}
