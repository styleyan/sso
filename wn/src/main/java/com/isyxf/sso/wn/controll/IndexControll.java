package com.isyxf.sso.wn.controll;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.isyxf.sso.wn.pojo.User;
import com.isyxf.sso.wn.service.IndexServiceImpl;
import com.isyxf.sso.wn.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.UUID;

/**
 * @author xiaofei.yan
 * @Create 2020-10-12 10:29
 * @Descript 首页控制器
 */
@Controller
public class IndexControll {
    /**
     * 打开首页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value = "ticket", required = false) String ticket, HttpServletRequest request, HttpServletResponse response) {
        String user = "";
        ModelAndView mv = new ModelAndView();
        Cookie[] ck = request.getCookies();
        String userInfo = "";

        // 检测cookie
        if (ck != null) {
            for (int i = 0; i < ck.length; i++) {
                String cookieName = ck[i].getName();
                if (cookieName.equals("user_info")) {
                    userInfo = ck[i].getValue();
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(userInfo)) {
            mv.addObject("msg", "我已经登录");
            mv.setViewName("index.html");
            return mv;
        }

        // cookies 不存在，如果 ticket 存在，则从redis里查询，从redis 查询数据
        if (StringUtils.isNotBlank(ticket)) {
            user = (String)RedisUtils.getValue(ticket);
//            // 字符串转 Java bean 对象
//            if (StringUtils.isNotBlank(userInfoStr)) {
//                user = JSON.parseObject(userInfoStr, User.class);
//            }
        }

        // 是否存在用户
        if (StringUtils.isNotBlank(user)) {
            int timeout = 60 * 60 *24 *365;
            // 不编码，设置cookies会报异常
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] ins = user.getBytes();

            Cookie cookie = new Cookie("user_info", encoder.encode(ins));
            cookie.setPath("/");
            cookie.setMaxAge(timeout);
            response.addCookie(cookie);

            mv.addObject("msg", "我已经登录");
            mv.setViewName("index.html");
        } else {
            String encodeUrl = "";
            try {
                encodeUrl = URLEncoder.encode("http://127.0.0.1:8090", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            mv.setViewName("redirect:http://127.0.0.1:8078?callbackUrl=" + encodeUrl);
        }

        return mv;
    }
}
