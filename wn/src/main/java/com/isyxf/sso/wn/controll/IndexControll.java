package com.isyxf.sso.wn.controll;

import com.isyxf.sso.wn.service.IndexServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author xiaofei.yan
 * @Create 2020-10-12 10:29
 * @Descript 首页控制器
 */
@Controller
public class IndexControll {

    @Autowired
    private IndexServiceImpl indexService;

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        Boolean b = indexService.isLogin();
        ModelAndView mv = new ModelAndView();

        Cookie[] ck = request.getCookies();
        String token = "";

        for (int i = 0; i < ck.length; i++) {
            String cookieName = ck[i].getName();
            if (cookieName == "access_token") {
                token = ck[i].getValue();
                break;
            }
        }

        if (b) {
            mv.addObject("login", "我已经登录");
            mv.setViewName("index.html");

            Cookie cookie = new Cookie("access_token", UUID.randomUUID().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
//            mv.setViewName("redirect:https://www.baidu.com");

        }

//        try {
//            response.sendRedirect("https://www.baidu.com");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        response.setHeader("Location", "https://www.baidu.com");
        response.setStatus(302);
        mv.addObject("login", "我已经登录");
        mv.setViewName("index.html");
        return mv;
    }
}
