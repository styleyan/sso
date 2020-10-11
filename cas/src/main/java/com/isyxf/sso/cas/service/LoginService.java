package com.isyxf.sso.cas.service;

import com.isyxf.sso.cas.mapper.UserMapper;
import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户信息
     * @param userBO
     */
    public User queryUser(UserBO userBO) {
        User user = userMapper.queryUserByLogin(userBO);
        return user;
    }
}
