package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import com.isyxf.sso.cas.service.LoginService;
import com.isyxf.sso.cas.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyApiController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/getUser")
    public JsonResult getUser(@RequestBody UserBO userBO) {
        User user = loginService.queryUser(userBO);

        if (user != null) {
            return JsonResult.success(user);
        } else {
            return JsonResult.failure(2001, "用户不存在");
        }
    }
}
