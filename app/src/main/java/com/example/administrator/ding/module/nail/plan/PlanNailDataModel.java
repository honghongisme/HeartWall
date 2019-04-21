package com.example.administrator.ding.module.nail.plan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ding.bean.nail.Nail;
import com.example.administrator.ding.bean.nail.PlanNail;
import com.example.administrator.ding.bean.nail.PlanPullNail;
import com.example.administrator.ding.base.Constans;
import com.example.administrator.ding.base.IBaseNetRequestListener;
import com.example.administrator.ding.utils.SQLiteHelper;
import com.example.administrator.ding.module.nail.OnGetInitNailListListener;
import com.example.administrator.ding.utils.DateUtil;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;

public class PlanNailDataModel {

    private SQLiteDatabase mSQLiteDatabase;
    private String[] tableNames;
    private OkHttpClient client;
    private Gson gson;

    public PlanNailDataModel(Context context) {
        setTablesName();
        client = new OkHttpClient();
        gson = new Gson();
        mSQLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    /**
     * 初始化本类需要使用的TableName数组
     */
    private void setTablesName() {
        tableNames = new String[]{"plan_nail", "plan_pull_nail", "plan_week", "plan_month"};
    }

    /**
     * 获取每个钉帽的位置
     * @param onGetNormalNailListListener
     */
    public void getChildViewsLocation(OnGetInitNailListListener onGetNormalNailListListener) {
        String sql = "select x,y from " + tableNames[0];
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        ArrayList<Nail> nailList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Nail nail = new Nail();
            nail.setX(cursor.getInt(cursor.getColumnIndex("x")));
            nail.setY(cursor.getInt(cursor.getColumnIndex("y")));
            nailList.add(nail);
        }
        onGetNormalNailListListener.onSuccess(nailList);
        cursor.close();
    }

    /**
     * 保存第一次编辑的信息
     * @param nail
     */
    public void saveFirstEditInformationToLocal(PlanNail nail) {
        String sql = "insert into " + tableNames[0] + " values(" + nail.getX() + "," + nail.getY() + ",'" + nail.getDate() + "','" + nail.getRecord() + "');";
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     *
     * @param nail
     * @param listener
     */
    public void saveFirstEditInfoToServer(PlanNail nail, final IBaseNetRequestListener listener) {
        FormBody body = new FormBody.Builder()
                .add("OperationType", "1")
                .add("Nail", gson.toJson(nail))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "PlanServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailed();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onSuccess();
            }
        });

    }

    /**
     * 如果该处已有障碍，返回false；否则返回true
     * @param rawX 触摸图片左上坐标
     * @param rawY
     * @param d
     * @return
     */
    public boolean isPlaceValid(int rawX, int rawY, int d) {
        String sql = "select x, y from " + tableNames[0];
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int x = cursor.getInt(cursor.getColumnIndex("x"));
            int y = cursor.getInt(cursor.getColumnIndex("y"));
            int centerX = x + d / 2;
            int centerY = y + d / 2;
            double centerPointDistance = Math.sqrt(Math.abs(centerX - rawX) * Math.abs(centerX - rawX) + Math.abs(centerY - rawY) * Math.abs(centerY - rawY));
            if (centerPointDistance + 20 < d) return false;
        }
        cursor.close();
        return true;
    }

    /**
     * 根据点击的钉帽，返回其粗略信息
     * @param x
     * @param y
     * @return {日期，记录}
     */
    public String[] getFirstEditInfo(int x, int y) {
        String sql = "select date, record from " + tableNames[0] + " where x=" + x + " and y=" + y;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        String date = null;
        String record = null;
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex("date"));
            record = cursor.getString(cursor.getColumnIndex("record"));
        }
        cursor.close();
        return date == null ? null : new String []{date, record};
    }

    /**
     * 从数据库里删除该条信息
     * @param x
     * @param y
     */
    public void saveLastEditInfoToLocal(int x, int y, String lastDate, String lastRecord) {
        String sql = "select date, record from " + tableNames[0] + " where x=" + x + " and y=" + y;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        String firstDate = null;
        String firstRecord = null;
        while (cursor.moveToNext()) {
            firstDate = cursor.getString(cursor.getColumnIndex("date"));
            firstRecord = cursor.getString(cursor.getColumnIndex("record"));
        }
        cursor.close();
        sql =  "insert into " + tableNames[1] + " values('" + firstDate + "','" + firstRecord + "','" + lastDate + "','" + lastRecord + "');" +
                "delete from " + tableNames[0] + " where x=" + x + " and y=" + y;
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     *
     * @param planNail
     * @param planPullNail
     */
    public void saveLastEditInfoToServer(PlanNail planNail, PlanPullNail planPullNail, final IBaseNetRequestListener listener) {
        FormBody body = new FormBody.Builder()
                .add(("OperationType"), "2")
                .add("Nail", gson.toJson(planNail))
                .add("PullNail", gson.toJson(planPullNail))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "PlanServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onSuccess();
            }
        });
    }

    /**
     * 更新周/月日期信息
     * @param tips 表名数组下标（2周，3月）
     * @param type 操作类型（1钉，0拔）
     * @param pattern
     */
    public void updateDateData(int tips, int type, String pattern) {
        if(tips == 2) insertDateDataRecord(tips, pattern);
        else insertDateDataRecord(tips, "yyyy");
        String sql = null;
        if(type == 1) {
            sql = "update " + tableNames[tips] + " set nailNumber=nailNumber+1 where date='" + DateUtil.getDateStr(pattern) + "'";
        }else if (type == 0){
            sql = "update " + tableNames[tips] + " set pullNumber=pullNumber+1 where date='" + DateUtil.getDateStr(pattern) + "'";
        }
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     * 新增周/月日期记录
     * @param tips
     * @param pattern
     */
    public void insertDateDataRecord(int tips, String pattern) {
        ArrayList<String> data = (tips == 2) ? DateUtil.getCurrentWeeksDate(pattern) : DateUtil.getCurrentMonthsDate(pattern);
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

}
