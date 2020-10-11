package com.isyxf.sso.cas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @RequestMapping("/login.do")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("nextText", "你好, 欢迎━(*｀∀´*)ノ亻!来到登录页面");
        mv.setViewName("login.html");

        return mv;
    }
}
