package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaofei.yan
 * @Create 2020-10-13 15:00
 * @Descript 通用控制器
 */
public class BaseController {
    /**
     * 判断是否登录
     * @return
     */
    public User UserInfo(HttpServletRequest request) {
        Cookie[] ck = request.getCookies();
        String token = null;
        User user = null;

        if (ck == null) {
            return user;
        }
        for (int i = 0; i < ck.length; i++) {
            if (ck[i].getName().equals("access_token")) {
                token = ck[i].getValue();
                break;
            }
        }

        if (StringUtils.isNotBlank(token)) {
            user = (User)RedisUtils.getValue(token);
        }
        return user;
    }
}
