package com.isyxf.sso.cas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 统一配置
 */
@Configuration
public class CorsConfig {
    public CorsConfig() {}

    /**
     * 配置跨域
     */
    @Bean
    CorsFilter corsFilter() {
        // 1. 添加 cors 配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 设置可跨越的域名
//        config.addAllowedOrigin("http://www.wn.me");
//        config.addAllowedOrigin("http://www.wn.me:8090");

        config.addAllowedOrigin("*");
        // 设置是否发送 cookie 信息
        config.setAllowCredentials(true);

        // 设置允许请求的方式
        config.addAllowedMethod("*");

        // 设置允许的header
        config.addAllowedHeader("*");

        // 2. 为 url 添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的 coreSource
        return new CorsFilter(corsSource);
    }
}
