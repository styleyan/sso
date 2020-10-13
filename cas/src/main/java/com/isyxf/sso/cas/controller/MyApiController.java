package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import com.isyxf.sso.cas.service.LoginService;
import com.isyxf.sso.cas.utils.JsonResult;
import com.isyxf.sso.cas.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class MyApiController extends BaseController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/getUser")
    public JsonResult getUser(@RequestBody UserBO userBO, HttpServletResponse response) {
        User user = loginService.queryUser(userBO);

        if (user != null) {
            String ticket = "userToken:" + UUID.randomUUID().toString().trim() + user.getId();
            int timeout = 60 * 60 *24 *365;
            RedisUtils.setValueTimeout(ticket, user, timeout);
            Cookie cookie = new Cookie("access_token", ticket);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(timeout);

            response.addCookie(cookie);
            user.setReturnUrl(userBO.getReturnUrl() + "?=ticket=" + "fffffff");
            return JsonResult.success(user);
        } else {
            return JsonResult.failure(2001, "用户不存在");
        }
    }
}
