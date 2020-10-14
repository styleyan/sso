package com.isyxf.sso.cas.controller;

import com.alibaba.fastjson.JSON;
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
    private static String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";
    private static String REDIS_USER_TICKET = "REDIS_USER_TICKET";

    @Autowired
    private LoginService loginService;

    @PostMapping("/api/getUser")
    public JsonResult getUser(@RequestBody UserBO userBO, HttpServletResponse response) {
        User user = loginService.queryUser(userBO);

        if (user != null) {
            // 2. 实现 redis 会话
            String uniqueToken = UUID.randomUUID().toString().trim();
            user.setUserUniqueToken(uniqueToken);
            RedisUtils.setValue(REDIS_USER_TOKEN + ":" + user.getId(), JSON.toJSONString(user));

            // 3. 生成 ticket 门票，全局门票，代表用户在CAS端登录过
            String userTicket = UUID.randomUUID().toString().trim();

            // 4. userTicket 关联用户id, 并且放入到redis中，代表这个用户有门票
            RedisUtils.setValue(REDIS_USER_TICKET + ":" + userTicket, user.getId());

            // 5. 生成临时票据，回跳到调用端网站，是有CAS端所签发的一个临时性ticket
            return JsonResult.success(user);
        } else {
            return JsonResult.failure(2001, "用户不存在");
        }
    }
}
