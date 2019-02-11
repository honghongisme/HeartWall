package com.example.administrator.ding.model.entities;

public class BagListItem {

    private String firstDate;
    private String lastDate;
    private String someContent;

    public BagListItem(String firstDate, String lastDate, String someContent) {
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        this.someContent = someContent;
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

    public String getSomeContent() {
        return someContent;
    }

    public void setSomeContent(String someContent) {
        this.someContent = someContent;
    }
}
