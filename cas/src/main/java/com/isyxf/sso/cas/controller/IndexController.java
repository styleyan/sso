package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.utils.CookiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class IndexController extends BaseController {

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value = "callbackUrl", required = false) String callbackUrl, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index.html");
        User user = UserInfo(request);

        Boolean bol = user != null;

        if (user != null && StringUtils.isNotBlank(callbackUrl)) {
            try {
                String encodeString = URLEncoder.encode(CookiesUtils.getSingleCookie(request,"access_token"), "UTF-8");
                modelAndView.setViewName("redirect:" + callbackUrl + "?ticket=" + encodeString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            modelAndView.addObject("isLogin", bol);
            modelAndView.addObject("msg", bol ? "已登录" : "请登录");
            modelAndView.addObject("callbackUrl", callbackUrl);
        }

        return modelAndView;
    }
}
