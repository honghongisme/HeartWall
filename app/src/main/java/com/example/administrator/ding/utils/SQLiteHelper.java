package com.example.administrator.ding.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "HEART_WALL_DB";
    private static final int DB_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists plan_nail(x int, y int, date datetime, record text, primary key(x, y))");
        db.execSQL("create table if not exists plan_pull_nail(firstDate datetime, firstRecord text, lastDate datetime, lastRecord text)");
        db.execSQL("create table if not exists plan_week(date text primary key, nailNumber int, pullNumber int)");
        db.execSQL("create table if not exists plan_month(date text primary key, nailNumber int, pullNumber int)");
        db.execSQL("create table if not exists mood_good_nail(x int, y int, firstDate datetime, lastDate datetime, record text, state int, visibility int, anonymous int)");
        db.execSQL("create table if not exists mood_bad_nail(x int, y int, firstDate datetime, lastDate datetime, record text, state int, visibility int, anonymous int, commentTag int)");
        db.execSQL("create table if not exists crack(x int, y int, date datetime, num int, power int, resId int)");
        db.execSQL("create table if not exists mood_week(date text primary key, goodNailNum int, goodPullNum int, badNailNum int, badPullNum int)");
        db.execSQL("create table if not exists mood_month(date text primary key, goodNailNum int, goodPullNum int, badNailNum int, badPullNum int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
