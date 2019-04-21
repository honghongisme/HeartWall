package com.example.administrator.ding.module.nail.mood;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ding.bean.Crack;
import com.example.administrator.ding.bean.nail.MoodBadNail;
import com.example.administrator.ding.bean.nail.MoodGoodNail;
import com.example.administrator.ding.base.Constans;
import com.example.administrator.ding.base.IBaseNetRequestListener;
import com.example.administrator.ding.utils.SQLiteHelper;
import com.example.administrator.ding.module.nail.OnGetCheckResultListener;
import com.example.administrator.ding.utils.DateUtil;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MoodNailDataModel {

    private String[] tableNames;
    private Gson gson;
    private OkHttpClient client;
    private SQLiteDatabase mSQLiteDatabase;

    public MoodNailDataModel(Context context) {
        mSQLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
        setTablesName();
        gson = new Gson();
        client = new OkHttpClient();
    }

    /**
     * 初始化本类需要使用的TableName数组
     */
    private void setTablesName() {
        tableNames = new String[]{"mood_good_nail", "mood_bad_nail", "crack", "mood_week", "mood_month"};
    }

    /**
     * 获取每个坏钉帽的位置
     * @return
     */
    public ArrayList<MoodBadNail> getAllBadNailHeadLocation() {
        String sql = "select x,y from " + tableNames[1] + " where state = 0";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        ArrayList<MoodBadNail> nailList = new ArrayList<>();
        while (cursor.moveToNext()) {
            MoodBadNail nail = new MoodBadNail();
            nail.setX(cursor.getInt(cursor.getColumnIndex("x")));
            nail.setY(cursor.getInt(cursor.getColumnIndex("y")));
            nailList.add(nail);
        }
        cursor.close();
        return nailList;
    }

    /**
     * 获取每个好钉帽的位置
     * @return
     */
    public ArrayList<MoodGoodNail> getAllGoodNailHeadLocation() {
        String sql = "select x,y from " + tableNames[0] + " where state = 0";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        ArrayList<MoodGoodNail> nailList = new ArrayList<>();
        while (cursor.moveToNext()) {
            MoodGoodNail nail = new MoodGoodNail();
            nail.setX(cursor.getInt(cursor.getColumnIndex("x")));
            nail.setY(cursor.getInt(cursor.getColumnIndex("y")));
            nailList.add(nail);
        }
        cursor.close();
        return nailList;
    }

    /**
     * 获取每个裂缝的位置
     * @return
     */
    public ArrayList<Crack> getAllNailCrackLocation() {
        String sql = "select x, y, resId from " + tableNames[2];
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        ArrayList<Crack> crackList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Crack crack = new Crack();
            crack.setX(cursor.getInt(cursor.getColumnIndex("x")));
            crack.setY(cursor.getInt(cursor.getColumnIndex("y")));
            crack.setResId(cursor.getInt(cursor.getColumnIndex("resId")));
            crackList.add(crack);
        }
        cursor.close();
        return crackList;
    }

    /**
     * 保存好运钉子编辑的信息
     * @param nail
     */
    public void saveGoodNailEditInfoToLocal(MoodGoodNail nail) {
        String sql = "insert into " + tableNames[0] + "(x, y, firstDate, record, state, visibility, anonymous) values(" + nail.getX() + "," + nail.getY() + ",'" + nail.getFirstDate() + "','" + nail.getRecord() + "', " + nail.getState() + ", " + nail.getVisibility() + ", " + nail.getAnonymous() + ")";
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     *
     * @param nail
     */
    public void saveGoodNailEditInfoToServer(MoodGoodNail nail, final OnGetCheckResultListener onGetCheckResultListener) {
        FormBody body = new FormBody.Builder()
                .add("OperationType", "3")
                .add("IGoodNail", gson.toJson(nail))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "MoodServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetCheckResultListener.onFailed();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getBoolean("CheckResult")) {
                        onGetCheckResultListener.onSuccess();
                    } else {
                        onGetCheckResultListener.onNotPassSensitiveWordsCheck();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 保存厄运钉子信息
     * @param nail
     */
    public void saveBadNailInfoToLocal(MoodBadNail nail) {
        String sql = "insert into " + tableNames[1] + "(x, y, firstDate, record, state, visibility, anonymous) values(" + nail.getX() + "," + nail.getY() + ",'" + nail.getFirstDate() + "', '" + nail.getRecord() + "', " + nail.getState() + ", " + nail.getVisibility() + ", " + nail.getAnonymous() + ")";
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     * 保存裂缝
     * @param crack
     */
    public void saveCrackInfoToLocal(Crack crack) {
        if (crack != null) {
            String sql = "insert into " + tableNames[2] + " values(" + crack.getX() + ", " + crack.getY() + ", '" +  crack.getDate() + "', " + crack.getNum() + ", " + crack.getPower() + ", " + crack.getResId() + ")";
            mSQLiteDatabase.execSQL(sql);
        }
    }

    /**
     *
     * @param badNail
     * @param crack
     */
    public void insertBadNailInfoToServer(MoodBadNail badNail, Crack crack, final OnGetCheckResultListener onGetCheckResultListener) {
        FormBody body;
        if (crack == null) {
            body = new FormBody.Builder()
                    .add("OperationType", "1")
                    .add("IBadNail", gson.toJson(badNail))
                    .add("Crack", "")
                    .build();
        } else {
            body = new FormBody.Builder()
                    .add("OperationType", "1")
                    .add("IBadNail", gson.toJson(badNail))
                    .add("Crack", gson.toJson(crack))
                    .build();
        }
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "MoodServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetCheckResultListener.onFailed();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getBoolean("CheckResult")) {
                        onGetCheckResultListener.onSuccess();
                    } else {
                        onGetCheckResultListener.onNotPassSensitiveWordsCheck();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 判断点击处是否有效
     * 如果该处已有障碍，返回false；否则返回true
     * @param currentCenterX 触摸图片中心坐标
     * @param currentCenterY
     * @param d
     * @return
     */
    public boolean isPlaceValid(int currentCenterX, int currentCenterY, int d) {
        String sql = "select x, y from " + tableNames[0] + " where state = 0";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        if (isNotExistFromDB(currentCenterX, currentCenterY, d, cursor)) {
            sql = "select x, y from " + tableNames[1] + " where state = 0";
            cursor = mSQLiteDatabase.rawQuery(sql, null);
            if (isNotExistFromDB(currentCenterX, currentCenterY, d, cursor)) {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return true;
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return true;
    }

    /**
     * 根据cursor里的数据判断是否有个该点位置极似的点
     * @param currentCenterX
     * @param currentCenterY
     * @param d
     * @param cursor
     * @return
     */
    private boolean isNotExistFromDB(int currentCenterX, int currentCenterY, int d, Cursor cursor) {
        while (cursor.moveToNext()) {
            int x = cursor.getInt(cursor.getColumnIndex("x"));
            int y = cursor.getInt(cursor.getColumnIndex("y"));
            int centerX = x + d / 2;
            int centerY = y + d / 2;
            double centerPointDistance = Math.sqrt(Math.abs(currentCenterX - centerX) * Math.abs(currentCenterX - centerX) + Math.abs(currentCenterY - centerY) * Math.abs(currentCenterY - centerY));
            if (centerPointDistance + 20 < d) return false;
        }
        return true;
    }

    /**
     * 获取厄运钉子钉帽的信息
     * @param x
     * @param y
     * @return 钉下日期
     */
    public MoodBadNail getBadNailHeadDetails(int x, int y) {
        String sql = "select record, firstDate, visibility, anonymous from " + tableNames[1] + " where x=" + x + " and y=" + y + " and state = 0";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        MoodBadNail nail = null;
        while (cursor.moveToNext()) {
            nail = new MoodBadNail();
            nail.setRecord(cursor.getString(cursor.getColumnIndex("record")));
            nail.setFirstDate(cursor.getString(cursor.getColumnIndex("firstDate")));
            nail.setVisibility(cursor.getInt(cursor.getColumnIndex("visibility")));
            nail.setAnonymous(cursor.getInt(cursor.getColumnIndex("anonymous")));
        }
        cursor.close();
        return nail;
    }

    /**
     * 获取好运钉子钉帽的信息
     * @param x
     * @param y
     * @return
     */
    public MoodGoodNail getGoodNailHeadDetails(int x, int y) {
        String sql = "select record, firstDate, visibility, anonymous from " + tableNames[0] + " where x=" + x + " and y=" + y + " and state = 0";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        MoodGoodNail nail = null;
        while (cursor.moveToNext()) {
            nail = new MoodGoodNail();
            nail.setRecord(cursor.getString(cursor.getColumnIndex("record")));
            nail.setFirstDate(cursor.getString(cursor.getColumnIndex("firstDate")));
            nail.setVisibility(cursor.getInt(cursor.getColumnIndex("visibility")));
            nail.setAnonymous(cursor.getInt(cursor.getColumnIndex("anonymous")));
        }
        cursor.close();
        return nail;
    }

    /**
     * 获取裂缝信息
     * @param x
     * @param y
     * @return {日期，次数，力度}
     */
    public Crack getCrackDetails(int x, int y) {
        String sql = "select date, num, power from " + tableNames[2] + " where x=" + x + " and y=" + y;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        String date = null;
        int num = 0;
        int power = 0;
        while (cursor.moveToNext()) {
            date = cursor.getString(cursor.getColumnIndex("date"));
            num = cursor.getInt(cursor.getColumnIndex("num"));
            power = cursor.getInt(cursor.getColumnIndex("power"));
        }
        cursor.close();
        if (date == null) {
            return null;
        }else {
            Crack crack = new Crack();
            crack.setDate(date);
            crack.setNum(num);
            crack.setPower(power);
            return crack;
        }
    }

    /**
     * 更新厄运钉子
     * @param x
     * @param y
     * @param lastDate
     */
    public void updateBadNailFromLocal(int x, int y, String lastDate) {
        String sql = "update " + tableNames[1] + " set lastDate = '" + lastDate + "', state = 1 where x = " + x + " and y = " + y + " and state = 0";
        mSQLiteDatabase.execSQL(sql);
    }

    public void updateBadNailFromServer(MoodBadNail nail, final IBaseNetRequestListener listener) {
        FormBody body = new FormBody.Builder()
                .add("OperationType", "2")
                .add("UBadNail", gson.toJson(nail))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "MoodServlet")
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
     * 更新好运钉子
     * @param x
     * @param y
     * @param lastDate
     */
    public void updateGoodNailFromLocal(int x, int y, String lastDate) {
        String sql = "update " + tableNames[0] + " set lastDate = '" + lastDate + "', state = 1 where x = " + x + " and y = " + y + " and state = 0";
        mSQLiteDatabase.execSQL(sql);
    }

    public void updateGoodNailFromServer(MoodGoodNail nail, final IBaseNetRequestListener listener) {
        FormBody body = new FormBody.Builder()
                .add("OperationType", "4")
                .add("UGoodNail", gson.toJson(nail))
                .build();
        Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "MoodServlet")
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
     * 更新周/月日期信息
     * @param tips 表名数组下标（3周，4月）
     * @param col 操作的字段（1 good，0 bad）
     * @param type 操作类型（1钉，0拔）
     * @param pattern
     */
    public void updateDate(int tips, int col, int type, String pattern) {
        if(tips == 3) insertDate(tips, pattern);
        else insertDate(tips, "yyyy");
        String sql = null;
        if(type == 1) {
            if (col == 1) {
                sql = "update " + tableNames[tips] + " set goodNailNum = goodNailNum + 1 where date='" + DateUtil.getDateStr(pattern) + "'";
            }else if (col == 0) {
                sql = "update " + tableNames[tips] + " set badNailNum = badNailNum + 1 where date='" + DateUtil.getDateStr(pattern) + "'";
            }
        }else if (type == 0){
            if (col == 1) {
                sql = "update " + tableNames[tips] + " set goodPullNum = goodPullNum + 1 where date='" + DateUtil.getDateStr(pattern) + "'";
            }else if (col == 0) {
                sql = "update " + tableNames[tips] + " set badPullNum = badPullNum + 1 where date='" + DateUtil.getDateStr(pattern) + "'";
            }
        }
        mSQLiteDatabase.execSQL(sql);
    }

    /**
     * 新增周/月日期记录
     * @param tips
     * @param pattern
     */
    public void insertDate(int tips, String pattern) {
        ArrayList<String> data = (tips == 3) ? DateUtil.getCurrentWeeksDate(pattern) : DateUtil.getCurrentMonthsDate(pattern);
        String sql = "select count(*) as isHaveRecord from " + tableNames[tips] + " where date='" + data.get(0) + "'";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        int isHaveRecord = 0;
        while (cursor.moveToNext()) {
            isHaveRecord = cursor.getInt(cursor.getColumnIndex("isHaveRecord"));
        }
        cursor.close();
        if (isHaveRecord == 0) {
            for(int i = 0; i < data.size(); i++) {
                sql = "insert into " + tableNames[tips] + " values('" + data.get(i) + "', 0, 0, 0, 0)";
                mSQLiteDatabase.execSQL(sql);
            }
        }
    }
}
