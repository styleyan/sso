package com.isyxf.sso.wn.controll;

import com.isyxf.sso.wn.utils.CookiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
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
    public ModelAndView index(@RequestParam(value = "ticket", required = false) String ticket, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.html");
        mv.addObject("msg", "欢迎来到pp系统");

        Cookie ck = new Cookie("aaa", "ffff");
        ck.setPath("/");
        ck.setMaxAge(100 * 60 * 60);

        response.addCookie(ck);
        return mv;
    }
}
