package com.example.administrator.ding.module.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ding.base.Constans;
import com.example.administrator.ding.bean.Crack;
import com.example.administrator.ding.bean.nail.MoodBadNail;
import com.example.administrator.ding.bean.MoodDate;
import com.example.administrator.ding.bean.nail.MoodGoodNail;
import com.example.administrator.ding.bean.PlanDate;
import com.example.administrator.ding.bean.nail.PlanNail;
import com.example.administrator.ding.bean.nail.PlanPullNail;
import com.example.administrator.ding.bean.User;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.utils.SQLiteHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/23.
 */
public class LoginModelImpl implements ILoginContract.Model {

    private Gson mGson;
    private String mSql;
    private SQLiteDatabase mSQLiteDatabase;
    private Context mContext;

    public LoginModelImpl(Context context) {
        mGson = new Gson();
        mContext = context;
        mSQLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
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
    public void sync(String planNailJson, String planPullNailJson, String planWeekJson, String planMonthJson, String moodGoodNailJson, String moodBadNailJson, String moodWeekJson, String moodMonthJson, String crackJson) {
        Type type = new TypeToken<ArrayList<PlanNail>>(){}.getType();
        ArrayList<PlanNail> planNailArrayList = mGson.fromJson(planNailJson, type);

        type = new TypeToken<ArrayList<PlanPullNail>>(){}.getType();
        ArrayList<PlanPullNail> planPullNailArrayList = mGson.fromJson(planPullNailJson, type);

        type = new TypeToken<ArrayList<PlanDate>>(){}.getType();
        ArrayList<PlanDate> planWeekArrayList = mGson.fromJson(planWeekJson, type);

        type = new TypeToken<ArrayList<PlanDate>>(){}.getType();
        ArrayList<PlanDate> planMonthArrayList = mGson.fromJson(planMonthJson, type);

        type = new TypeToken<ArrayList<MoodGoodNail>>(){}.getType();
        ArrayList<MoodGoodNail> moodGoodNailArrayList = mGson.fromJson(moodGoodNailJson, type);

        type = new TypeToken<ArrayList<MoodBadNail>>(){}.getType();
        ArrayList<MoodBadNail> moodBadNailArrayList = mGson.fromJson(moodBadNailJson, type);

        type = new TypeToken<ArrayList<MoodDate>>(){}.getType();
        ArrayList<MoodDate> moodWeekArrayList = mGson.fromJson(moodWeekJson, type);

        type = new TypeToken<ArrayList<MoodDate>>(){}.getType();
        ArrayList<MoodDate> moodMonthArrayList = mGson.fromJson(moodMonthJson, type);

        type = new TypeToken<ArrayList<Crack>>(){}.getType();
        ArrayList<Crack> crackArrayList = mGson.fromJson(crackJson, type);

        mSQLiteDatabase.beginTransaction();
        savePlanNailInfoList(planNailArrayList);
        savePlanPullNailInfoList(planPullNailArrayList);
        savePlanWeekInfoList(planWeekArrayList);
        savePlanMonthInfoList(planMonthArrayList);
        saveMoodGoodNailInfoList(moodGoodNailArrayList);
        saveMoodBadNailInfoList(moodBadNailArrayList);
        saveMoodWeekInfoList(moodWeekArrayList);
        saveMoodMonthInfoList(moodMonthArrayList);
        saveCrack(crackArrayList);
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    private void savePlanNailInfoList(ArrayList<PlanNail> nailArrayList) {
        for (PlanNail nail : nailArrayList) {
            mSql = "insert into plan_nail values(" + nail.getX() + ", " + nail.getY() + ", '" + nail.getDate() + "', '" + nail.getRecord() + "')";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void savePlanPullNailInfoList(ArrayList<PlanPullNail> nailArrayList) {
        for (PlanPullNail nail : nailArrayList) {
            mSql =  "insert into plan_pull_nail values('" + nail.getFirstDate() + "','" + nail.getFirstRecord() + "','" + nail.getLastDate() + "','" + nail.getLastRecord() + "')";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void savePlanWeekInfoList(ArrayList<PlanDate> list) {
        for (PlanDate nail : list) {
            mSql =  "insert into plan_week values('" + nail.getDate() + "', " + nail.getNailNum() + ", " + nail.getPullNum() + ")";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void savePlanMonthInfoList(ArrayList<PlanDate> list) {
        for (PlanDate nail : list) {
            mSql =  "insert into plan_month values('" + nail.getDate() + "', " + nail.getNailNum() + ", " + nail.getPullNum() + ")";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void saveMoodGoodNailInfoList(ArrayList<MoodGoodNail> nailArrayList) {
        for (MoodGoodNail nail : nailArrayList) {
            mSql = "insert into mood_good_nail values(" + nail.getX() + ", " + nail.getY() + ", '" + nail.getFirstDate() + "', '" + nail.getLastDate() + "', '" + nail.getRecord() + "', " + nail.getState() + ", " + nail.getVisibility() + ", " + nail.getAnonymous() + ")";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void saveMoodBadNailInfoList(ArrayList<MoodBadNail> nailArrayList) {
        for (MoodBadNail nail : nailArrayList) {
            mSql = "insert into mood_bad_nail values(" + nail.getX() + ", " + nail.getY() + ", '" + nail.getFirstDate() + "', '" + nail.getLastDate() + "', '" + nail.getRecord() + "', " + nail.getState() + ", " + nail.getVisibility() + ", " + nail.getAnonymous() + ", " + nail.getCommentTag() + ")";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void saveMoodWeekInfoList(ArrayList<MoodDate> dates) {
        for (MoodDate date : dates) {
            mSql = "insert into mood_week values('" + date.getDate() + "', " + date.getGoodNailNum() + ", " + date.getGoodPullNum() + ", " + date.getBadNailNum() + ", " + date.getBadPullNailNum() + ")";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void saveMoodMonthInfoList(ArrayList<MoodDate> dates) {
        for (MoodDate date : dates) {
            mSql = "insert into mood_month values('" + date.getDate() + "', " + date.getGoodNailNum() + ", " + date.getGoodPullNum() + ", " + date.getBadNailNum() + ", " + date.getBadPullNailNum() + ")";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    private void saveCrack(ArrayList<Crack> cracks) {
        for (Crack crack : cracks) {
            mSql = "insert into crack values(" + crack.getX() + ", " + crack.getY() + ", '" + crack.getDate() + "', " + crack.getNum() + ", " + crack.getPower() + ", " + crack.getResId() + ")";
            mSQLiteDatabase.execSQL(mSql);
        }
    }

    @Override
    public void requestServer(String account, String password, final IOnLoginReqListener listener) {
        final OkHttpClient client = new OkHttpClient.Builder().build();
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
                if (!NetStateCheckHelper.isNetWork(mContext)) {
                    listener.isNoNet();
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
                        listener.isNotExistUser();
                    }else if ("success".equals(result)){
                        boolean isLogin = jsonObject.getBoolean("IsLogin");
                        User user = new Gson().fromJson(jsonObject.getString("User"), User.class);
                        new UserInfoStore().saveUser(mContext, user);
                        System.out.println("onResponse: " + user.getId() + " ; " + user.getAccountNumber() + " ; " + user.getPassword() + " ; " + user.getName() + " ; " + user.getSex() + " ; " + user.getIdentity() + " ; " + user.getDepartment());
                        if (isLogin) {
                            // 同步信息
                            sync(jsonObject.getString("planNail"),
                                    jsonObject.getString("planPullNail"),
                                    jsonObject.getString("planWeek"),
                                    jsonObject.getString("planMonth"),
                                    jsonObject.getString("moodGoodNail"),
                                    jsonObject.getString("moodBadNail"),
                                    jsonObject.getString("moodWeek"),
                                    jsonObject.getString("moodMonth"),
                                    jsonObject.getString("crack"));
                        }
                        listener.onSuccess(user);
                    }
                } catch (JSONException e) {
                    listener.onFailed();
                    e.printStackTrace();
                }

            }
        });
    }

}
