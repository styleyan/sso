package com.isyxf.sso.wn.controll;

import com.isyxf.sso.wn.service.IndexServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IndexServiceImpl indexService;

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        Boolean b = indexService.isLogin();
        ModelAndView mv;

        if (!b) {
            mv = new ModelAndView();
            mv.addObject("login", "我已经登录");
            mv.setViewName("index.html");
        } else {
            mv = new ModelAndView("redirect:https://www.baidu.com");
        }

        return mv;
    }
}
