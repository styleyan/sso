package com.isyxf.sso.cas.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class User {
    /**
     * 主键id 用户id
     */
    private String id;

    /**
     * 用户名 用户名
     */
    private String username;

    /**
     * 密码 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 昵称 昵称
     */
    private String nickname;

    /**
     * 真实姓名 真实姓名
     */
    private String realname;

    /**
     * 头像 头像
     */
    private String face;

    /**
     * 手机号 手机号
     */
    @JsonIgnore
    private String mobile;

    /**
     * 邮箱地址 邮箱地址
     */
    private String email;

    /**
     * 性别 性别 1:男  0:女  2:保密
     */
    private Integer sex;

    /**
     * 生日 生日
     */
    private Date birthday;

    /**
     * 创建时间 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间 更新时间
     */
    private Date updatedTime;

    /**
     * 回调地址
     */
    private String returnUrl;
}