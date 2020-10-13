package com.isyxf.sso.cas.controller;

import com.alibaba.fastjson.JSON;
import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.utils.CookiesUtils;
import com.isyxf.sso.cas.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;

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
        String token = CookiesUtils.getSingleCookie(request,"access_token");
        User user = null;

        if (StringUtils.isBlank(token)) {
            return null;
        }


        if (StringUtils.isNotBlank(token)) {
            String userInfoStr = (String) RedisUtils.getValue(token);

            if (StringUtils.isNotBlank(userInfoStr)) {
                user = JSON.parseObject(userInfoStr, User.class);
            }
        }
        return user;
    }
}
