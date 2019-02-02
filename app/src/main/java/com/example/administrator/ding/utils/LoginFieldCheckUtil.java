package com.example.administrator.ding.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/8/23.
 */

public class LoginFieldCheckUtil {

    /**
     * 判断号码是否有效
     * @param phone
     * @return
     */
    /*public static boolean isPhoneValid(String phone) {
        //手机号码正则表达式
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))[0-9]{8}$";
        phone = phone.trim();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            return false;
        }
        return true;
    }*/

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0)
            return true;
        else
            return false;
    }

}
