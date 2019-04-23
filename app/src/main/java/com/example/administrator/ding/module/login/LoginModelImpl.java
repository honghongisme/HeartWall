package com.example.administrator.ding.module.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.ding.base.Constans;
import com.example.administrator.ding.model.entry.Crack;
import com.example.administrator.ding.model.entry.MoodBadNail;
import com.example.administrator.ding.model.entry.MoodDate;
import com.example.administrator.ding.model.entry.MoodGoodNail;
import com.example.administrator.ding.model.entry.PlanDate;
import com.example.administrator.ding.model.entry.PlanNail;
import com.example.administrator.ding.model.entry.PlanPullNail;
import com.example.administrator.ding.model.entry.User;
import com.example.administrator.ding.database.login.UserInfoStore;
import com.example.administrator.ding.model.LoginInfoModel;
import com.example.administrator.ding.network.ILoginService;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.utils.SQLiteHelper;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/23.
 */
public class LoginModelImpl implements ILoginContract.Model {

    private String mSql;
    private SQLiteDatabase mSQLiteDatabase;
    private Context mContext;

    public LoginModelImpl(Context context) {
        mContext = context;
        mSQLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    public void sync(ArrayList<PlanNail> planNail, ArrayList<PlanPullNail> planPullNail, ArrayList<PlanDate> planWeek,
                     ArrayList<PlanDate> planMonth, ArrayList<MoodGoodNail> moodGoodNail, ArrayList<MoodBadNail> moodBadNail,
                     ArrayList<MoodDate> moodWeek, ArrayList<MoodDate> moodMonth, ArrayList<Crack> crack) {
        // 开启事务
        mSQLiteDatabase.beginTransaction();
        savePlanNailInfoList(planNail);
        savePlanPullNailInfoList(planPullNail);
        savePlanWeekInfoList(planWeek);
        savePlanMonthInfoList(planMonth);
        saveMoodGoodNailInfoList(moodGoodNail);
        saveMoodBadNailInfoList(moodBadNail);
        saveMoodWeekInfoList(moodWeek);
        saveMoodMonthInfoList(moodMonth);
        saveCrack(crack);
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

    public void doLogin(String account, String password, final IOnLoginReqListener listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constans.SERVER_IP_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ILoginService service = retrofit.create(ILoginService.class);
        service.doLogin(account, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginInfoModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!NetStateCheckHelper.isNetWork(mContext)) {
                            listener.isNoNet();
                        }
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(LoginInfoModel loginModel) {
                        String result = loginModel.getResult();
                        if ("failed".equals(result)) {
                            listener.isNotExistUser();
                        } else if ("success".equals(result)){
                            boolean isLogin = loginModel.getLogin();
                            User user = loginModel.getUser();
                            new UserInfoStore().saveUser(mContext, user);
                            if (isLogin) {
                                // 同步信息
                                sync(loginModel.getPlanNail(),
                                        loginModel.getPlanPullNail(),
                                        loginModel.getPlanWeek(),
                                        loginModel.getPlanMonth(),
                                        loginModel.getMoodGoodNail(),
                                        loginModel.getMoodBadNail(),
                                        loginModel.getMoodWeek(),
                                        loginModel.getMoodMonth(),
                                        loginModel.getCrack());
                            }
                            listener.onSuccess(user);
                        }
                    }
                });
    }
}
