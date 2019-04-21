package com.example.administrator.ding.bean.nail;

/**
 * 已拔下的计划钉子
 */
public class PlanPullNail {

    /**
     * 用户id
     */
    private int id;
    /**
     * 第一次钉下的时间
     */
    private String firstDate;
    /**
     * 第一次钉下的内容
     */
    private String firstRecord;
    /**
     * 最后一次拔出的时间
     */
    private String lastDate;
    /**
     * 最后一次拔出的内容
     */
    private String lastRecord;

    public PlanPullNail(int id, String firstDate, String firstRecord, String lastDate, String lastRecord) {
        this.id = id;
        this.firstDate = firstDate;
        this.firstRecord = firstRecord;
        this.lastDate = lastDate;
        this.lastRecord = lastRecord;
    }

    public PlanPullNail() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getFirstRecord() {
        return firstRecord;
    }

    public void setFirstRecord(String firstRecord) {
        this.firstRecord = firstRecord;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastRecord() {
        return lastRecord;
    }

    public void setLastRecord(String lastRecord) {
        this.lastRecord = lastRecord;
    }
}
