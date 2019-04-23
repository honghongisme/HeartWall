package com.example.administrator.ding.model.entry;

/**
 * 消极情绪钉子的评论
 */
public class MoodBadComment {

    /**
     * 用户id（谁评论的）
     */
    private int id;
    /**
     * 标识符（谁的钉子）
     */
    private int tag;
    /**
     * 时间
     */
    private String date;
    /**
     * 记录
     */
    private String record;
    /**
     * 是否匿名
     */
    private int isAnonymous;

    public MoodBadComment() {
    }

    public MoodBadComment(int id, int tag, String date, String record, int isAnonymous) {
        this.id = id;
        this.tag = tag;
        this.date = date;
        this.record = record;
        this.isAnonymous = isAnonymous;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
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

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
