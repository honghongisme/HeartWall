package com.example.administrator.ding.model.view;

public class CommentItem {

    /**
     * 发表评论的用户名
     */
    private String userName;
    /**
     * 发表时间
     */
    private String date;
    /**
     * 发表内容
     */
    private String content;

    public CommentItem() {
    }

    public CommentItem(String userName, String date, String content) {
        this.userName = userName;
        this.date = date;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
