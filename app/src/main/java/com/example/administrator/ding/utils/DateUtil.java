package com.example.administrator.ding.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Administrator on 2018/8/16.
 */

public class DateUtil {


    public static String getDateStr(String pattern) {
        return getDateStr(new Date(), pattern);
    }


    public static String getDateStr(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return simpleDateFormat.format(date);
    }


    public static Date getDate(String dateStr, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本年每月list
     * @param pattern
     * @return
     */
    public static ArrayList<String> getCurrentMonthsDate(String pattern) {
        String year = getDateStr(pattern);
        ArrayList<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (i < 10) months.add(year + "-0" + i);
            else months.add(year + "-" + i);
        }
        return months;
    }

    /**
     * 获取本周日期list
     */
    private static final int FIRST_DAY = Calendar.MONDAY;

    public static ArrayList<String> getCurrentWeeksDate(String pattern) {
        Calendar calendar = Calendar.getInstance();
        setToFirstDay(calendar);
        ArrayList<String> date = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            date.add(getDay(calendar, pattern));
            calendar.add(Calendar.DATE, 1);
        }
        System.out.println(date);
        return date;
    }

    private static void setToFirstDay(Calendar calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
            calendar.add(Calendar.DATE, -1);
        }
    }

    private static String getDay(Calendar calendar, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(calendar.getTime());
    }

}
