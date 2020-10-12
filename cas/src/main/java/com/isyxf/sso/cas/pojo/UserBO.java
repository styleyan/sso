package com.isyxf.sso.cas.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBO {
    private String username;
    private String password;
    private String returnUrl;
}
