package com.example.administrator.ding.module.bag;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ding.base.Constans;
import com.example.administrator.ding.model.view.BagListItem;
import com.example.administrator.ding.model.entry.MoodBadNail;
import com.example.administrator.ding.model.entry.MoodGoodNail;
import com.example.administrator.ding.model.entry.PlanPullNail;
import com.example.administrator.ding.base.IBaseNetRequestListener;
import com.example.administrator.ding.utils.SQLiteHelper;
import com.example.administrator.ding.utils.DateUtil;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/25.
 */

public class NailListDataModel{

    private String[] tableNames;
    private SQLiteDatabase mSQLiteDatabase;


    public NailListDataModel(Context context) {
        setTablesName();
        mSQLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    /**
     * 初始化本类需要使用的TableName数组
     */
    private void setTablesName() {
        tableNames = new String[]{"plan_nail", "plan_pull_nail", "mood_good_nail", "mood_bad_nail", "plan_week", "plan_month"};
    }

    /**
     * 保存第二次编辑信息
     * @param firstDate
     * @param lastDate
     * @param lastRecord
     */
    public void saveLastEditInfo(String firstDate, String firstRecord, String lastDate, String lastRecord) {
        String sql =  "insert into " + tableNames[1] + " values('" + firstDate + "','" + firstRecord + "','" + lastDate + "','" + lastRecord + "')" ;
        mSQLiteDatabase.execSQL(sql);
        sql = "delete from " + tableNames[0] + " where date='" + firstDate + "'";
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     *
     * @param planPullNail
     */
    public void saveLastEditInfoToServer(PlanPullNail planPullNail, final IBaseNetRequestListener listener) {
        FormBody body = new FormBody.Builder()
                .add(("OperationType"), "3")
                .add("Nail", new Gson().toJson(planPullNail))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "PlanServlet")
                .post(body)
                .build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess();
            }
        });
    }

    /**
     * 更新周/月日期信息
     * @param tips 表名数组下标（4周，5月）
     * @param pattern
     */
    public void updateDateData(int tips, String pattern) {
        if(tips == 4) insertDateDataRecord(tips, pattern);
        else insertDateDataRecord(tips, "yyyy");
        String sql = "update " + tableNames[tips] + " set pullNumber=pullNumber+1 where date='" + DateUtil.getDateStr(pattern) + "'";
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     * 新增周/月日期记录
     * @param tips
     * @param pattern
     */
    private void insertDateDataRecord(int tips, String pattern) {
        ArrayList<String> data = (tips == 4) ? DateUtil.getCurrentWeeksDate(pattern) : DateUtil.getCurrentMonthsDate(pattern);
        String sql = "select count(*) as isHaveRecord from " + tableNames[tips] + " where date='" + data.get(0) + "';";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        int isHaveRecord = 0;
        while (cursor.moveToNext()) {
            isHaveRecord = cursor.getInt(cursor.getColumnIndex("isHaveRecord"));
        }
        cursor.close();
        if (isHaveRecord == 0) {
            for(int i = 0; i < data.size(); i++) {
                sql = "insert into " + tableNames[tips] + " values('" + data.get(i) + "', 0, 0);";
                mSQLiteDatabase.execSQL(sql);
            }
        }
    }

    /**
     * 获取已取下的计划钉子细节
     * @param firstDate 钉下日期
     * @param lastDate 拔出日期
     * @return {钉下记录，拔出记录}
     */
    public String[] getPlanPullNailDetails(String firstDate, String lastDate) {
        String sql = "select firstRecord, lastRecord from " + tableNames[1] + " where firstDate='" + firstDate + "' and lastDate='" + lastDate + "'";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        String firstRecord = null;
        String lastRecord = null;
        while (cursor.moveToNext()) {
            firstRecord = cursor.getString(cursor.getColumnIndex("firstRecord"));
            lastRecord = cursor.getString(cursor.getColumnIndex("lastRecord"));
        }
        cursor.close();
        return new String[] {firstRecord, lastRecord};
    }

    /**
     * 获取墙上的计划钉子细节
     * @param firstDate 钉下日期
     * @return 钉下记录
     */
    public String getPlanNailDetails(String firstDate) {
        String sql = "select record from " + tableNames[0] + " where date='" + firstDate + "'";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        String record = null;
        while (cursor.moveToNext()) {
            record = cursor.getString(cursor.getColumnIndex("record"));
        }
        cursor.close();
        return record;
    }


    /**
     * 获取计划组在墙上的数据list
     * @param arrayList
     * @param roomTable
     */
    public void getPlanGroupRoomDataList(ArrayList<BagListItem> arrayList, String roomTable) {
        BagListItem item = null;
        String sql = "select date, record from " + roomTable;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        while(cursor.moveToNext()) {
            String firstDate = cursor.getString(cursor.getColumnIndex("date"));
            String record = cursor.getString(cursor.getColumnIndex("record"));
            String lastDate = "";
            String content = null;
            if (record.length() > 10) {
                content = record.substring(0, 10) + "...";
            } else {
                content = record;
            }
            item = new BagListItem(firstDate, lastDate, content);
            arrayList.add(item);
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }

    /**
     * 获取计划组已拔下的数据list
     * @param arrayList
     * @param removeTable
     */
    public void getPlanGroupRemoveDataList(ArrayList<BagListItem> arrayList, String removeTable) {
        BagListItem item = null;
        String sql = "select firstDate, lastDate, firstRecord from " + removeTable;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        while(cursor.moveToNext()) {
            String firstDate = cursor.getString(cursor.getColumnIndex("firstDate"));
            String lastDate = cursor.getString(cursor.getColumnIndex("lastDate"));
            String record = cursor.getString(cursor.getColumnIndex("firstRecord"));
            String content = null;
            if (record.length() > 10) {
                content = record.substring(0, 10) + "...";
            } else {
                content = record;
            }
            item = new BagListItem(firstDate, lastDate, content);
            arrayList.add(item);
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }

    /**
     * 获取好运钉子组的数据list
     * @param arrayList
     */
    public void getMoodGoodGroupDataList(ArrayList<BagListItem> arrayList) {
        getMoodNailDateList(arrayList, "mood_good_nail");
    }

    /**
     * 获取厄运钉子组的数据list
     * @param arrayList
     */
    public void getMoodBadGroupDataList(ArrayList<BagListItem> arrayList) {
        getMoodNailDateList(arrayList, "mood_bad_nail");
    }

    /**
     *
     * @param arrayList
     * @param tableName
     */
    private void getMoodNailDateList(ArrayList<BagListItem> arrayList, String tableName) {
        BagListItem item = null;
        String sql = "select firstDate, record from " + tableName + " where state = 1";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql ,null);
        while(cursor.moveToNext()) {
            String firstDate = cursor.getString(cursor.getColumnIndex("firstDate"));
            String lastDate = "";
            String record = cursor.getString(cursor.getColumnIndex("record"));
            String content = null;
            if (record.length() > 10) {
                content = record.substring(0, 10) + "...";
            } else {
                content = record;
            }
            item = new BagListItem(firstDate, lastDate, content);
            arrayList.add(item);
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }

    /**
     * 获取厄运钉子的信息
     * @param firstDate
     * @return
     */
    public MoodBadNail getBadNailDetailsByDate(String firstDate) {
        String sql = "select x, y, record, visibility, anonymous from mood_bad_nail where firstDate = '" + firstDate + "' and state = 1";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        MoodBadNail nail = null;
        while (cursor.moveToNext()) {
            nail = new MoodBadNail();
            nail.setX(cursor.getInt(cursor.getColumnIndex("x")));
            nail.setY(cursor.getInt(cursor.getColumnIndex("y")));
            nail.setFirstDate(firstDate);
            nail.setRecord(cursor.getString(cursor.getColumnIndex("record")));
            nail.setVisibility(cursor.getInt(cursor.getColumnIndex("visibility")));
            nail.setAnonymous(cursor.getInt(cursor.getColumnIndex("anonymous")));
        }
        cursor.close();
        return nail;
    }

    /**
     * 获取好运钉子的信息
     * @param firstDate
     * @return
     */
    public MoodGoodNail getGoodNailDetailsByDate(String firstDate) {
        String sql = "select record, visibility, anonymous from mood_good_nail where firstDate = '" + firstDate +"' and state = 1";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        MoodGoodNail nail = null;
        while (cursor.moveToNext()) {
            nail = new MoodGoodNail();
            nail.setRecord(cursor.getString(cursor.getColumnIndex("record")));
            nail.setFirstDate(firstDate);
            nail.setVisibility(cursor.getInt(cursor.getColumnIndex("visibility")));
            nail.setAnonymous(cursor.getInt(cursor.getColumnIndex("anonymous")));
        }
        cursor.close();
        return nail;
    }
}
