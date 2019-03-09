package com.example.administrator.ding.model.impl;

import android.content.Context;

import com.example.administrator.ding.config.Constans;
import com.example.administrator.ding.model.entities.*;
import com.example.administrator.ding.presenter.OnGetLoginResultListener;
import com.example.administrator.ding.model.ILoginModel;
import com.example.administrator.ding.utils.DBManager;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/8/23.
 */

public class LoginModelImpl implements ILoginModel {

    private DBManager dbManager;
    private Gson gson;
    private String sql;

    public LoginModelImpl(Context context) {
        dbManager = new DBManager(context);
        gson = new Gson();
    }

    /**
     * 保存user信息
     * @param userJsonStr
     * @return
     */
    public User saveUserToLocal(String userJsonStr) {
        User user = gson.fromJson(userJsonStr, User.class);
        sql = "create table if not exists user(id int, accountNumber char(8) unique not null, password char(15) not null, sex char(2) not null, department char(15) not null, name char(10), identity char(10))";
        dbManager.createTable(sql);
        sql = "insert into user values(" + user.getId() + ", '" + user.getAccountNumber() + "', '" + user.getPassword() + "', '" + user.getSex() + "', '" + user.getDepartment() + "', '" + user.getName() + "', '" + user.getIdentity() + "')";
        dbManager.execSQL(sql);
        return user;
    }

    /**
     * 建表
     */
    private void createTables() {
        sql = "create table if not exists plan_nail(x int, y int, date datetime, record text, primary key(x, y));" +
                "create table if not exists plan_pull_nail(firstDate datetime, firstRecord text, lastDate datetime, lastRecord text);" +
                "create table if not exists plan_week(date text primary key, nailNumber int, pullNumber int);" +
                "create table if not exists plan_month(date text primary key, nailNumber int, pullNumber int);" +
                "create table if not exists mood_good_nail(x int, y int, firstDate datetime, lastDate datetime, record text, state int, visibility int, anonymous int);" +
                "create table if not exists mood_bad_nail(x int, y int, firstDate datetime, lastDate datetime, record text, state int, visibility int, anonymous int, commentTag int);" +
                "create table if not exists crack(x int, y int, date datetime, num int, power int, resId int);" +
                "create table if not exists mood_week(date text primary key, goodNailNum int, goodPullNum int, badNailNum int, badPullNum int);" +
                "create table if not exists mood_month(date text primary key, goodNailNum int, goodPullNum int, badNailNum int, badPullNum int)";
        dbManager.createTable(sql);
    }

    /**
     * 同步数据库
     * @param planNailJson
     * @param planPullNailJson
     * @param planWeekJson
     * @param planMonthJson
     * @param moodGoodNailJson
     * @param moodBadNailJson
     * @param moodWeekJson
     * @param moodMonthJson
     * @param crackJson
     */
    public void syncInfo(String planNailJson, String planPullNailJson, String planWeekJson, String planMonthJson, String moodGoodNailJson, String moodBadNailJson, String moodWeekJson, String moodMonthJson, String crackJson) {
        createTables();

        Type type = new TypeToken<ArrayList<PlanNail>>(){}.getType();
        ArrayList<PlanNail> planNailArrayList = gson.fromJson(planNailJson, type);

        type = new TypeToken<ArrayList<PlanPullNail>>(){}.getType();
        ArrayList<PlanPullNail> planPullNailArrayList = gson.fromJson(planPullNailJson, type);

        type = new TypeToken<ArrayList<PlanDate>>(){}.getType();
        ArrayList<PlanDate> planWeekArrayList = gson.fromJson(planWeekJson, type);

        type = new TypeToken<ArrayList<PlanDate>>(){}.getType();
        ArrayList<PlanDate> planMonthArrayList = gson.fromJson(planMonthJson, type);

        type = new TypeToken<ArrayList<MoodGoodNail>>(){}.getType();
        ArrayList<MoodGoodNail> moodGoodNailArrayList = gson.fromJson(moodGoodNailJson, type);

        type = new TypeToken<ArrayList<MoodBadNail>>(){}.getType();
        ArrayList<MoodBadNail> moodBadNailArrayList = gson.fromJson(moodBadNailJson, type);

        type = new TypeToken<ArrayList<MoodDate>>(){}.getType();
        ArrayList<MoodDate> moodWeekArrayList = gson.fromJson(moodWeekJson, type);

        type = new TypeToken<ArrayList<MoodDate>>(){}.getType();
        ArrayList<MoodDate> moodMonthArrayList = gson.fromJson(moodMonthJson, type);

        type = new TypeToken<ArrayList<Crack>>(){}.getType();
        ArrayList<Crack> crackArrayList = gson.fromJson(crackJson, type);

        dbManager.beginTransaction();
        savePlanNailInfoList(planNailArrayList);
        savePlanPullNailInfoList(planPullNailArrayList);
        savePlanWeekInfoList(planWeekArrayList);
        savePlanMonthInfoList(planMonthArrayList);
        saveMoodGoodNailInfoList(moodGoodNailArrayList);
        saveMoodBadNailInfoList(moodBadNailArrayList);
        saveMoodWeekInfoList(moodWeekArrayList);
        saveMoodMonthInfoList(moodMonthArrayList);
        saveCrack(crackArrayList);
        dbManager.endTransaction();
    }

    /**
     *
     * @param nailArrayList
     */
    private void savePlanNailInfoList(ArrayList<PlanNail> nailArrayList) {
        for (PlanNail nail : nailArrayList) {
            sql = "insert into plan_nail values(" + nail.getX() + ", " + nail.getY() + ", '" + nail.getDate() + "', '" + nail.getRecord() + "')";
            dbManager.execSQL(sql);
        }
    }

    private void savePlanPullNailInfoList(ArrayList<PlanPullNail> nailArrayList) {
        for (PlanPullNail nail : nailArrayList) {
            sql =  "insert into plan_pull_nail values('" + nail.getFirstDate() + "','" + nail.getFirstRecord() + "','" + nail.getLastDate() + "','" + nail.getLastRecord() + "')";
            dbManager.execSQL(sql);
        }
    }

    private void savePlanWeekInfoList(ArrayList<PlanDate> list) {
        for (PlanDate nail : list) {
            sql =  "insert into plan_week values('" + nail.getDate() + "', " + nail.getNailNum() + ", " + nail.getPullNum() + ")";
            dbManager.execSQL(sql);
        }
    }

    private void savePlanMonthInfoList(ArrayList<PlanDate> list) {
        for (PlanDate nail : list) {
            sql =  "insert into plan_month values('" + nail.getDate() + "', " + nail.getNailNum() + ", " + nail.getPullNum() + ")";
            dbManager.execSQL(sql);
        }
    }

    private void saveMoodGoodNailInfoList(ArrayList<MoodGoodNail> nailArrayList) {
        for (MoodGoodNail nail : nailArrayList) {
            sql = "insert into mood_good_nail values(" + nail.getX() + ", " + nail.getY() + ", '" + nail.getFirstDate() + "', '" + nail.getLastDate() + "', '" + nail.getRecord() + "', " + nail.getState() + ", " + nail.getVisibility() + ", " + nail.getAnonymous() + ")";
            dbManager.execSQL(sql);
        }
    }

    private void saveMoodBadNailInfoList(ArrayList<MoodBadNail> nailArrayList) {
        for (MoodBadNail nail : nailArrayList) {
            sql = "insert into mood_bad_nail values(" + nail.getX() + ", " + nail.getY() + ", '" + nail.getFirstDate() + "', '" + nail.getLastDate() + "', '" + nail.getRecord() + "', " + nail.getState() + ", " + nail.getVisibility() + ", " + nail.getAnonymous() + ", " + nail.getCommentTag() + ")";
            dbManager.execSQL(sql);
        }
    }

    private void saveMoodWeekInfoList(ArrayList<MoodDate> dates) {
        for (MoodDate date : dates) {
            sql = "insert into mood_week values('" + date.getDate() + "', " + date.getGoodNailNum() + ", " + date.getGoodPullNum() + ", " + date.getBadNailNum() + ", " + date.getBadPullNailNum() + ")";
            dbManager.execSQL(sql);
        }
    }

    private void saveMoodMonthInfoList(ArrayList<MoodDate> dates) {
        for (MoodDate date : dates) {
            sql = "insert into mood_month values('" + date.getDate() + "', " + date.getGoodNailNum() + ", " + date.getGoodPullNum() + ", " + date.getBadNailNum() + ", " + date.getBadPullNailNum() + ")";
            dbManager.execSQL(sql);
        }
    }

    private void saveCrack(ArrayList<Crack> cracks) {
        for (Crack crack : cracks) {
            sql = "insert into crack values(" + crack.getX() + ", " + crack.getY() + ", '" + crack.getDate() + "', " + crack.getNum() + ", " + crack.getPower() + ", " + crack.getResId() + ")";
            dbManager.execSQL(sql);
        }
    }

    @Override
    public void requestServer(final Context context, String account, String password, final OnGetLoginResultListener onGetLoginResultListener) {
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        FormBody body = new FormBody.Builder()
                .add("AccountNumber", account)
                .add("Password", password)
                .build();
        final Request request = new Request.Builder()
                .url(Constans.SERVER_IP_ADDRESS + "LoginServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!NetStateCheckHelper.isNetWork(context)) {
                    onGetLoginResultListener.isNoNet();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String info = response.body().string();
                System.out.println(info);
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    String result = jsonObject.getString("Result");
                    if ("failed".equals(result)) {
                        onGetLoginResultListener.isNotExistUser();
                    }else if ("success".equals(result)){
                        boolean isLogin = jsonObject.getBoolean("IsLogin");
                        User user = saveUserToLocal(jsonObject.getString("User"));
                        System.out.println("onResponse: " + user.getId() + " ; " + user.getAccountNumber() + " ; " + user.getPassword() + " ; " + user.getName() + " ; " + user.getSex() + " ; " + user.getIdentity() + " ; " + user.getDepartment());
                        if (isLogin) {
                            // 同步信息
                            syncInfo(jsonObject.getString("planNail"),
                                    jsonObject.getString("planPullNail"),
                                    jsonObject.getString("planWeek"),
                                    jsonObject.getString("planMonth"),
                                    jsonObject.getString("moodGoodNail"),
                                    jsonObject.getString("moodBadNail"),
                                    jsonObject.getString("moodWeek"),
                                    jsonObject.getString("moodMonth"),
                                    jsonObject.getString("crack"));
                        }
                        onGetLoginResultListener.onSuccess(user);
                    }
                } catch (JSONException e) {
                    onGetLoginResultListener.onFailed();
                    e.printStackTrace();
                }

            }
        });
    }

}
