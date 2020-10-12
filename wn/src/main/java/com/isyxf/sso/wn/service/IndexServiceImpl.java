package com.isyxf.sso.wn.service;

import com.isyxf.sso.wn.utils.RedisUtils;
import org.springframework.stereotype.Service;

/**
 * @author xiaofei.yan
 * @Create 2020-10-12 11:20
 * @Descript 首页逻辑
 */
@Service
public class IndexServiceImpl {

    /**
     * 是否登录
     * @return
     */
    public Boolean isLogin() {
        Object tk = RedisUtils.getValue("ticket");

        if (tk == null) {
            return false;
        }

        return true;
    }
}
