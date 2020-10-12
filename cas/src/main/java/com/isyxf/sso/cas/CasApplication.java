package com.isyxf.sso.cas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.isyxf.sso.cas.mapper"})
public class CasApplication {
    public static void main(String[] args) {
        SpringApplication.run(CasApplication.class, args);
    }

}
