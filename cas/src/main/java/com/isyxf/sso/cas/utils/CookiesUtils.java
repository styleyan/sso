package com.isyxf.sso.cas.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaofei.yan
 * @Create 2020-10-13 19:33
 * @Descript cookies操作
 */
public class CookiesUtils {
    /**
     * 获得单个
     */
    public static String getSingleCookie(HttpServletRequest request, String key) {
        Cookie[] ck = request.getCookies();
        String val = "";

        for (int i = 0; i < ck.length; i++) {
            if (ck[i].getName().equals(key)) {
                val = ck[i].getValue();
                break;
            }
        }
        return val;
    }
}
