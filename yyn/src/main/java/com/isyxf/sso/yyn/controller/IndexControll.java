package com.isyxf.sso.yyn.controller;

import com.isyxf.sso.yyn.utils.CookiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.html");

        String userInfo = CookiesUtils.getSingleCookie(request,"user_info");

        mv.addObject("msg", "欢迎来到pp系统");
        mv.addObject("isLogin", StringUtils.isNotBlank("userInfo"));
        return mv;
    }
}
