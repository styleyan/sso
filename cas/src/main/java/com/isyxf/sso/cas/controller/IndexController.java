package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.service.UserService;
import com.isyxf.sso.cas.utils.CookiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value = "returnUrl", required = false) String returnUrl, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getUserInfo(request);

        /**
         * TODO: 还需要对 returnUrl 进行校验，是否是一个对的链接地址，最好是这个 returnUrl 有白名单设置
         */
        if (user != null && StringUtils.isNotBlank(returnUrl)) {
            String tmpTick = userService.createTmpTicket();
            modelAndView.setViewName("redirect:" + returnUrl + "?tmpTicket=" + tmpTick);
            return modelAndView;
        }

        modelAndView.setViewName("index.html");
        modelAndView.addObject("callbackUrl", returnUrl);
        modelAndView.addObject("isLogin", user != null);
        modelAndView.addObject("msg", "欢迎来到单点登录系统");

        return modelAndView;
    }
}
