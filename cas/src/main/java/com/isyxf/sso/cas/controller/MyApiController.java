package com.isyxf.sso.cas.controller;

import com.alibaba.fastjson.JSON;
import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import com.isyxf.sso.cas.service.LoginService;
import com.isyxf.sso.cas.utils.CookiesUtils;
import com.isyxf.sso.cas.utils.JsonResult;
import com.isyxf.sso.cas.utils.MD5Utils;
import com.isyxf.sso.cas.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class MyApiController {
    public static String REDIS_USER_TOKEN = "redis_user_token";
    public static String REDIS_USER_TICKET = "redis_user_ticket";
    public static String REDIS_TMP_TICKET = "redis_tpm_ticket";

    @Autowired
    private LoginService loginService;

    /**
     * 使用一次性临时票据来验证用户是否登录，把用户会话信息返回给站点，使用完毕后，需要销毁临时票据
     * @param tmpTicket
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/api/verifyTmpTicket")
    public JsonResult verifyTmpTicket(@RequestParam("tmpTicket") String tmpTicket, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tmpTicketKeyName = REDIS_TMP_TICKET + ":" + tmpTicket;
        String tmpTicketValue = (String)RedisUtils.getValue(tmpTicketKeyName);

        if (StringUtils.isBlank(tmpTicketValue)) {
            return JsonResult.failure(2339, "临时票据无效");
        }

        // 0. 如果临时票据 OK, 则需要销毁，并且拿到 CAS 端 cookie 中的全局 userTicker, 以此在获取用户会话
        if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
            return JsonResult.failure(2339, "临时票据无效");
        } else {
            // 销毁临时票据
            RedisUtils.delKey(tmpTicketKeyName);
        }

        // 1. 验证并且获取用户的userTicket
        String userTicket = CookiesUtils.getSingleCookie(request, "cookie_user_ticket");
        String userId = (String)RedisUtils.getValue(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return JsonResult.failure(2339, "用户票据异常");
        }

        // 2. 验证门票对应的 user 会话是否存在
        String userRedis = (String)RedisUtils.getValue(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)) {
            return JsonResult.failure(2339, "用户票据异常");
        }

        User user = JSON.parseObject(userRedis, User.class);
        return JsonResult.success(user);
    }

    @PostMapping("/api/login")
    public JsonResult getUser(@RequestBody UserBO userBO, HttpServletResponse response) {
        User user = loginService.queryUser(userBO);
        int timeout = 60 * 10;

        if (user != null) {
            // 2. 实现 redis 会话
            RedisUtils.setValueTimeout(REDIS_USER_TOKEN + ":" + user.getId(), JSON.toJSONString(user), timeout);

            // 3. 生成 ticket 门票，全局门票，代表用户在CAS端登录过
            String userTicket = UUID.randomUUID().toString().trim();
            // 4. userTicket 关联用户id, 并且放入到redis中，代表这个用户有门票
            RedisUtils.setValueTimeout(REDIS_USER_TICKET + ":" + userTicket, user.getId(), timeout);

            setCookie("cookie_user_ticket", userTicket, response);

            // 5. 生成临时票据，回跳到调用端网站，是有CAS端所签发的一个临时性ticket
            String tmpTicket = createTmpTicket();

            return JsonResult.success(tmpTicket);
        } else {
            return JsonResult.failure(2001, "用户不存在");
        }
    }

    /**
     * 创建临时票据
     * @return
     */
    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();

        try {
            RedisUtils.setValueTimeout(REDIS_TMP_TICKET + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmpTicket;
    }

    /**
     * 添加cookie
     */
    private void setCookie(String key, String val, HttpServletResponse response) {
        Cookie ck = new Cookie(key, val);
        ck.setPath("/");
        ck.setDomain("yxf.me");
        ck.setMaxAge(60 * 60 * 60);
        response.addCookie(ck);
    }
}
