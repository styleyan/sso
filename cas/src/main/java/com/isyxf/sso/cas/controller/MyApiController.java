package com.isyxf.sso.cas.controller;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import com.isyxf.sso.cas.service.UserService;
import com.isyxf.sso.cas.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class MyApiController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userBO
     * @param response
     * @return
     */
    @PostMapping("/api/login")
    public JsonResult getUser(@RequestBody UserBO userBO, HttpServletResponse response) {
        String tmpTick = userService.login(userBO, response);

        if (StringUtils.isNotBlank(tmpTick)) {
            return JsonResult.success(tmpTick);
        }

        return JsonResult.failure(2004, "用户不存在");
    }

    /**
     * 使用一次性临时票据来验证用户是否登录，把用户会话信息返回给站点，使用完毕后，需要销毁临时票据
     * @param tmpTicket
     * @param request
     * @return
     */
    @PostMapping("/api/verifyTmpTicket")
    public JsonResult verifyTmpTicket(@RequestParam("tmpTicket") String tmpTicket, HttpServletRequest request) throws Exception {
        User user = userService.verifyTmpTicket(request, tmpTicket);

        if (user != null) {
            return JsonResult.success(user);
        }

        return JsonResult.failure(2003, "校验失败");
    }

}
