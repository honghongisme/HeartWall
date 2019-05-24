package com.example.administrator.ding.model.view;

import com.example.administrator.ding.model.entry.Crack;
import com.example.administrator.ding.model.entry.MoodDate;
import com.example.administrator.ding.model.entry.PlanDate;
import com.example.administrator.ding.model.entry.User;
import com.example.administrator.ding.model.entry.MoodBadNail;
import com.example.administrator.ding.model.entry.MoodGoodNail;
import com.example.administrator.ding.model.entry.PlanNail;
import com.example.administrator.ding.model.entry.PlanPullNail;

import java.util.ArrayList;

public class LoginInfoModel {

    private String Result;
    private Boolean IsLogin;
    private User User;
    private ArrayList<PlanNail> planNail;
    private ArrayList<PlanPullNail> planPullNail;
    private ArrayList<PlanDate> planWeek;
    private ArrayList<PlanDate> planMonth;
    private ArrayList<MoodGoodNail> moodGoodNail;
    private ArrayList<MoodBadNail> moodBadNail;
    private ArrayList<MoodDate> moodWeek;
    private ArrayList<MoodDate> moodMonth;
    private ArrayList<Crack> crack;

    public String getResult() {
        return Result;
    }

    public Boolean getLogin() {
        return IsLogin;
    }

    public com.example.administrator.ding.model.entry.User getUser() {
        return User;
    }

    public ArrayList<PlanNail> getPlanNail() {
        return planNail;
    }

    public ArrayList<PlanPullNail> getPlanPullNail() {
        return planPullNail;
    }

    public ArrayList<PlanDate> getPlanWeek() {
        return planWeek;
    }

    public ArrayList<PlanDate> getPlanMonth() {
        return planMonth;
    }

    public ArrayList<MoodGoodNail> getMoodGoodNail() {
        return moodGoodNail;
    }

    public ArrayList<MoodBadNail> getMoodBadNail() {
        return moodBadNail;
    }

    public ArrayList<MoodDate> getMoodWeek() {
        return moodWeek;
    }

    public ArrayList<MoodDate> getMoodMonth() {
        return moodMonth;
    }

    public ArrayList<Crack> getCrack() {
        return crack;
    }
}
