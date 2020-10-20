package com.isyxf.sso.cas.service;

import com.alibaba.fastjson.JSON;
import com.isyxf.sso.cas.mapper.UserMapper;
import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import com.isyxf.sso.cas.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    /**
     * 登录认证
     */
    public static String REDIS_USER_TOKEN = "redis_user_token";
    /**
     * 用户全局门票
     */
    public static String REDIS_USER_TICKET = "redis_user_ticket";
    /**
     * 临时门票
     */
    public static String REDIS_TMP_TICKET = "redis_tpm_ticket";
    /**
     * 用户cookie key
     */
    public static String USER_TICKET_COOKIE = "user_ticket_cookie";

    @Autowired
    private UserMapper userMapper;


    /**
     * 查询用户信息
     * @param userBO
     */
    public String login(UserBO userBO, HttpServletResponse response) {
        // 1. 通过用户账号、密码查询是否有该用户
        User user = userMapper.queryUserByLogin(userBO);

        if (user != null) {
            int timeout = 60 * 10;
            // 2. 实现 redis 会话，保存用户信息
            RedisUtils.setValueTimeout(REDIS_USER_TOKEN + ":" + user.getId(), JSON.toJSONString(user), 60 * 60 *24 * 365);
            // 3. 生成全局门票，代表用户在CAS端登录过，userTicket 关联用户id, 并且放入到redis中，代表这个用户有门票
            String userTicket = UUID.randomUUID().toString().trim();
            RedisUtils.setValueTimeout(REDIS_USER_TICKET + ":" + userTicket, user.getId(), timeout);
            // 4. 把全局门票key放入到cookie中
            CookiesUtils.setCookie(response, USER_TICKET_COOKIE, userTicket, "/", timeout, "yxf.me");
            // 5. 生成临时票据，回跳到调用端网站，是有CAS端所签发的一个临时性ticket
            String tmpTicket = createTmpTicket();

            return tmpTicket;
        }
        return null;
    }

    /**
     * 使用一次性临时票据来验证用户是否登录，把用户会话信息返回给站点，使用完毕后，需要销毁临时票据
     * @param request request 对象
     * @param tmpTicket 临时门票
     * @return
     */
    public User verifyTmpTicket(HttpServletRequest request, String tmpTicket) throws Exception {
        String tmpTicketKeyName = REDIS_TMP_TICKET + ":" + tmpTicket;
        String tmpTicketValue = (String)RedisUtils.getValue(tmpTicketKeyName);

        if (StringUtils.isBlank(tmpTicketValue)) {
            return null;
        }
        // 0. 如果临时票据 OK, 则需要销毁，并且需要拿到 CAS 端 cookie 中的全局 userTicker, 以此在获取用户会话
        if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
            return null;
        } else {
            // 销毁临时票据
            RedisUtils.delKey(tmpTicketKeyName);
        }

        return getUserInfo(request);
    }

    /**
     * 通过 cookie 验证 用户信息是否正确
     * @param request
     * @return
     */
    public User getUserInfo(HttpServletRequest request) {
        String userTicket = CookiesUtils.getSingleCookie(request,USER_TICKET_COOKIE);
        if (StringUtils.isBlank(userTicket)) {
            return null;
        }
        // 1. 通过cookie中的用户门票获取用户id
        String userId = (String)RedisUtils.getValue(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return null;
        }

        // 2. 通过用户id得到用户信息
        String userRedis = (String)RedisUtils.getValue(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)) {
            return null;
        }

        return JSON.parseObject(userRedis, User.class);
    }

    /**
     * 创建临时门票
     * @return
     */
    public String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();

        try {
            RedisUtils.setValueTimeout(REDIS_TMP_TICKET + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmpTicket;
    }
}
