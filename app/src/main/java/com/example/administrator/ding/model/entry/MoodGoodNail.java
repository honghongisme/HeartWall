package com.example.administrator.ding.model.entry;

public class MoodGoodNail {

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
    private String firstDate;
    /**
     * 拔出时间
     */
    private String lastDate;
    /**
     * 钉下时编辑的内容
     */
    private String record;
    /**
     * 状态，是否拔下
     */
    private int state;
    /**
     * 是否公开
     */
    private int visibility;
    /**
     * 是否匿名
     */
    private int anonymous;

    public MoodGoodNail() {
    }

    public MoodGoodNail(int id, int x, int y, String firstDate, String lastDate, String record, int state, int visibility, int anonymous) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        this.record = record;
        this.state = state;
        this.visibility = visibility;
        this.anonymous = anonymous;
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

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }
}
