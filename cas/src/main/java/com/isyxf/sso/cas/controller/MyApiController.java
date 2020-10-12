package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import com.isyxf.sso.cas.service.LoginService;
import com.isyxf.sso.cas.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class MyApiController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/getUser")
    public JsonResult getUser(@RequestBody UserBO userBO, HttpServletResponse response) {
        User user = loginService.queryUser(userBO);

        if (user != null) {
            user.setReturnUrl(userBO.getReturnUrl() + "?=ticket=" + "fffffff");
            Cookie cookie = new Cookie("token", UUID.randomUUID().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);
            return JsonResult.success(user);
        } else {
            return JsonResult.failure(2001, "用户不存在");
        }
    }
}
