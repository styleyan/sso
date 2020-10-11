package com.isyxf.sso.cas.mapper;

import com.isyxf.sso.cas.pojo.User;
import com.isyxf.sso.cas.pojo.UserBO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    /**
     * 保存用户
     */
    Integer insertUser(User user);

    /**
     * 通过账号、密码检索用户
     */
    User queryUserByLogin(UserBO userBO);
}