package com.example.administrator.ding.module.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ding.utils.SQLiteHelper;
import com.example.administrator.ding.utils.DateUtil;

/**
 * Created by Administrator on 2018/8/23.
 */

public class MainModel {


    private SQLiteDatabase mSQLiteDatabase;

    public MainModel(Context context) {
        mSQLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    /**
     * 清空用户数据
     */
    public void clearDB() {
        String[] tableNames = {"user", "plan_nail", "plan_pull_nail", "plan_week", "plan_month", "mood_good_nail", "mood_bad_nail", "mood_week", "mood_month", "crack"};
        String sql;
        for (String tableName : tableNames) {
            sql = "delete from " + tableName;
            mSQLiteDatabase.execSQL(sql);
        }
    }

    /**
     * 获取心情模块评论
     * @param badNailNumber
     * @param badPullNumber
     * @param goodNailNumber
     * @return
     */
    public String getMoodComment(int badNailNumber, int badPullNumber, int goodNailNumber) {
        String text = "今天是个平淡的一天";
        if(goodNailNumber > 0 && badNailNumber > 0 && badPullNumber > 0) {
            text = "今天真是精彩的一天";
        }else if (badNailNumber > 0 && badPullNumber > 0) {
            text = "今天你似乎遇到了很多烦心事";
        }else if (goodNailNumber > 0) {
            text = "今天是个幸运的一天";
        }
        return text;
    }

    /**
     * 获取计划模块评论
     * @param nailNumber
     * @param pullNumber
     * @return
     */
    public String getPlanComment(int nailNumber, int pullNumber) {
        String text = "你今天似乎很闲";
        if(nailNumber > 0 && pullNumber == 0) text = "你似乎还有一些事情没做";
        else if(nailNumber > 0 && pullNumber > 0 ) text = "你最近似乎很忙碌";
        else if(nailNumber == 0 && pullNumber > 0) text = "你最近似乎很努力";
        return text;
    }

    /**
     * 从本地获取今日计划记录
     * @return
     */
    public int[] getTodayPlanNum() {
        String tableName = "plan_week";
        String sql = "select nailNumber, pullNumber from " + tableName + " where date='" + DateUtil.getDateStr("yyyy-MM-dd EEE") + "'";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        int nailNumber = -1;
        int pullNumber = -1;
        while(cursor.moveToNext()) {
            nailNumber = cursor.getInt(cursor.getColumnIndex("nailNumber"));
            pullNumber = cursor.getInt(cursor.getColumnIndex("pullNumber"));
        }
        cursor.close();
        if(nailNumber != -1 && pullNumber != -1)  return new int[]{nailNumber, pullNumber};
        return null;
    }

    /**
     * 从本地获取今日情绪记录
     * @return {厄运钉入，厄运拔出，好运钉入}
     */
    public int[] getTodayMoodNum() {
        String tableName = "mood_week";
        String sql = "select goodNailNum, badNailNum, badPullNum from " + tableName + " where date='" + DateUtil.getDateStr("yyyy-MM-dd EEE") + "'";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        int goodNailNum = -1;
        int badNailNum = -1;
        int badPullNum = -1;
        while(cursor.moveToNext()) {
            goodNailNum = cursor.getInt(cursor.getColumnIndex("goodNailNum"));
            badNailNum = cursor.getInt(cursor.getColumnIndex("badNailNum"));
            badPullNum = cursor.getInt(cursor.getColumnIndex("badPullNum"));
        }
        cursor.close();
        if(goodNailNum != -1 && badNailNum != -1 && badPullNum != -1)  return new int[]{badNailNum, badPullNum, goodNailNum};
        return null;
    }

}
