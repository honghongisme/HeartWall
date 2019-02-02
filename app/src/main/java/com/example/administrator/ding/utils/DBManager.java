package com.example.administrator.ding.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.ConnectException;

/**
 * Created by Administrator on 2018/8/20.
 */

public class DBManager {

    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private Context context;


    public DBManager(Context context) {
        this.context = context;
        openDB();
    }

    /**
     * 打开数据库
     */
    private void openDB() {
        sqLiteDatabase = context.openOrCreateDatabase("nail.db", Context.MODE_PRIVATE, null);
    }

    /**
     * 建表
     * @param sql
     */
    public void createTable(String sql) {
        execSQLs(sql);
    }

    /**
     * 判断表是否存在
     * @param tableName
     * @return
     */
    public boolean isExistTable(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        try {
            String sql = "select count(*) as c from Sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 批量操作开启事务
     */
    public void beginTransaction() {
        sqLiteDatabase.beginTransaction();
    }

    /**
     * 批量操作提交事务
     */
    public void endTransaction() {
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    /**
     * 执行多条sql语句
     * @param sqls
     */
    public void execSQLs(String sqls) {
        String[] sqlArray = sqls.split(";");
        for(String sql : sqlArray) {
            sqLiteDatabase.execSQL(sql);
        }
    }

    /**
     * 执行一条sql语句
     * @param sql
     */
    public void execSQL(String sql) {
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * 查询语句
     * @param sql
     * @return
     */
    public Cursor queryAll(String sql) {
        cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }

    /**
     * 关闭资源
     */
    public void closeAll() {
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

}
