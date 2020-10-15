package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.utils.CookiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value = "callbackUrl", required = false) String callbackUrl, HttpServletRequest request) {
        String loginStatus = CookiesUtils.getSingleCookie(request, "cookie_user_ticket");

        ModelAndView modelAndView = new ModelAndView("index.html");
        modelAndView.addObject("callbackUrl", callbackUrl);
        modelAndView.addObject("isLogin", StringUtils.isNotBlank(loginStatus));
        modelAndView.addObject("msg", "CAS界面");

        return modelAndView;
    }
}
