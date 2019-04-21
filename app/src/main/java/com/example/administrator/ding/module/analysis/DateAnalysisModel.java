package com.example.administrator.ding.module.analysis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ding.utils.SQLiteHelper;


import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/27.
 */

public class DateAnalysisModel{

    private SQLiteDatabase mSQLiteDatabase;

    public DateAnalysisModel(Context context) {
        mSQLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    /**
     * 获取心情柱状图数据
     * @param onGetMoodDataListener
     */
    public void getMoodColumnarChartDate(OnGetMoodDataListener onGetMoodDataListener) {
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<int[][]> yValues = new ArrayList<>();
        String tableName = "mood_month";
        setPlanColumnarChartXTextLabelData(xValues);
        String sql = "select goodNailNum, goodPullNum, badNailNum, badPullNum from " + tableName;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            setMoodColumnarChartDateToZero(yValues);
            onGetMoodDataListener.onSuccess(xValues, yValues);
            return;
        }
        cursor.moveToPosition(cursor.getCount()-13);
        int goodNailNum;
        int goodPullNum;
        int badNailNum;
        int badPullNum;
        while(cursor.moveToNext()) {
            goodNailNum = cursor.getInt(cursor.getColumnIndex("goodNailNum"));
            goodPullNum = cursor.getInt(cursor.getColumnIndex("goodPullNum"));
            badNailNum = cursor.getInt(cursor.getColumnIndex("badNailNum"));
            badPullNum = cursor.getInt(cursor.getColumnIndex("badPullNum"));
            yValues.add(new int[][]{{goodNailNum, goodPullNum, badNailNum, badPullNum}});
        }
        onGetMoodDataListener.onSuccess(xValues, yValues);
        cursor.close();
    }

    /**
     *获取心情折线图图数据
     * @param onGetMoodDataListener
     */
    public void getMoodLineChartDate(OnGetMoodDataListener onGetMoodDataListener) {
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<int[][]> yValues = new ArrayList<>();
        String tableName = "mood_week";
        String sql = "select * from " + tableName;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        if(cursor.getCount() == 0) {
            setMoodLineChartDateToZero(xValues, yValues);
            onGetMoodDataListener.onSuccess(xValues, yValues);
            return;
        }
        cursor.moveToPosition(cursor.getCount()-8);
        int goodNailNum;
        int goodPullNum;
        int badNailNum;
        int badPullNum;
        while (cursor.moveToNext()) {
            goodNailNum = cursor.getInt(cursor.getColumnIndex("goodNailNum"));
            goodPullNum = cursor.getInt(cursor.getColumnIndex("goodPullNum"));
            badNailNum = cursor.getInt(cursor.getColumnIndex("badNailNum"));
            badPullNum = cursor.getInt(cursor.getColumnIndex("badPullNum"));
            yValues.add(new int[][]{{goodNailNum, goodPullNum, badNailNum, badPullNum}});
        }
        setLineChartXLabel(xValues);
        onGetMoodDataListener.onSuccess(xValues, yValues);
        cursor.close();
    }

    /**
     * 获取计划柱状图数据
     * @param onGetPlanDateListener
     */
    public void getPlanColumnarChartDate(OnGetPlanDateListener onGetPlanDateListener) {
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<int[]> yValues = new ArrayList<>();
        String tableName = "plan_month";
        setPlanColumnarChartXTextLabelData(xValues);
        String sql = "select nailNumber, pullNumber from " + tableName;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            setPlanColumnarChartDateToZero(yValues);
            onGetPlanDateListener.onSuccess(xValues, yValues);
            return;
        }
        cursor.moveToPosition(cursor.getCount()-13);
        while(cursor.moveToNext()) {
            int nailNumber = cursor.getInt(cursor.getColumnIndex("nailNumber"));
            int pullNumber = cursor.getInt(cursor.getColumnIndex("pullNumber"));
            yValues.add(new int[]{nailNumber, pullNumber});
        }
        onGetPlanDateListener.onSuccess(xValues, yValues);
        cursor.close();
    }

    /**
     * 获取计划折线图数据
     * @param onGetPlanDateListener
     */
    public void getPlanLineChartDate(OnGetPlanDateListener onGetPlanDateListener) {
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<int[]> yValues = new ArrayList<>();
        String tableName = "plan_week";
        String sql = "select * from " + tableName;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        if(cursor.getCount() == 0) {
            setPlanLineChartDateToZero(xValues, yValues);
            onGetPlanDateListener.onSuccess(xValues, yValues);
            return;
        }
        cursor.moveToPosition(cursor.getCount()-8);
        while (cursor.moveToNext()) {
            int nailNumber = cursor.getInt(cursor.getColumnIndex("nailNumber"));
            int pullNumber = cursor.getInt(cursor.getColumnIndex("pullNumber"));
            yValues.add(new int[]{nailNumber, pullNumber});
        }
        setLineChartXLabel(xValues);
        onGetPlanDateListener.onSuccess(xValues, yValues);
        cursor.close();
    }

    private void setLineChartXLabel(ArrayList<String> xValues) {
        xValues.add("周一");
        xValues.add("周二");
        xValues.add("周三");
        xValues.add("周四");
        xValues.add("周五");
        xValues.add("周六");
        xValues.add("周日");
    }

    /**
     * 折线图数据置零
     * @param xValues
     * @param yValues
     */
    private void setPlanLineChartDateToZero(ArrayList<String> xValues, ArrayList<int[]> yValues) {
        setLineChartXLabel(xValues);
        for(int i = 0; i < 7; i++) {
            yValues.add(new int[]{0, 0});
        }
    }

    /**
     * 柱状图数据置0
     */
    private void setPlanColumnarChartDateToZero(ArrayList<int[]> yValues) {
        for(int i = 0; i < 12; i++) {
            yValues.add(new int[]{0, 0});
        }
    }

    /**
     * 设置柱状图横轴坐标
     * @param xValues
     */
    private void setPlanColumnarChartXTextLabelData(ArrayList<String> xValues) {
        for(int i = 1; i < 13; i++) {
            //这里的i和坐标轴上的刻度对应，设置从1开始，空出0刻度
            xValues.add(i + "月");
        }
    }

    /**
     * 折线图数据置零
     * @param xValues
     * @param yValues
     */
    private void setMoodLineChartDateToZero(ArrayList<String> xValues, ArrayList<int[][]> yValues) {
        setLineChartXLabel(xValues);
        for(int i = 0; i < 7; i++) {
            yValues.add(new int[][]{{0, 0, 0, 0}});
        }
    }

    /**
     * 柱状图数据置0
     */
    private void setMoodColumnarChartDateToZero(ArrayList<int[][]> yValues) {
        for(int i = 0; i < 12; i++) {
            yValues.add(new int[][]{{0, 0, 0, 0}});
        }
    }

    public interface OnGetPlanDateListener {
        void onSuccess(ArrayList<String> xValues, ArrayList<int[]> yValues);
    }

    public interface OnGetMoodDataListener {
        void onSuccess(ArrayList<String> xValues, ArrayList<int[][]> yValues);
    }

}
