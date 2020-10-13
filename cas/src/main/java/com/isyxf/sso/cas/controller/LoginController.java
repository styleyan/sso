package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController extends BaseController {

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        User user = UserInfo(request);

        if (user != null) {
            modelAndView.setViewName("index.html");
        } else {
            modelAndView.setViewName("redirect:login.do");
        }
        return modelAndView;
    }

    @RequestMapping("/login.do")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        User user = UserInfo(request);

        if (user != null) {
            modelAndView.setViewName("redirect:/");
        } else {
            modelAndView.addObject("nextText", "你好, 欢迎━(*｀∀´*)ノ亻!来到登录页面");
            modelAndView.setViewName("login.html");
        }

        return modelAndView;
    }
}
