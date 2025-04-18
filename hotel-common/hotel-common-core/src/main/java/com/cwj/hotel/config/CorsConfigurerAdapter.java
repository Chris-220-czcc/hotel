package com.cwj.hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域请求处理
 */
@Configuration
public class CorsConfigurerAdapter implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对那些请求进行跨域处理
                .allowedOrigins("*")    // 允许的请求来源
                .allowedMethods("*")    // 允许的请求方法
                .allowedHeaders("*")    // 允许带有请求头信息
                .allowCredentials(false) // 是否允许证书
                .allowedOriginPatterns("*") // 支持的域
                .maxAge(36000); // 请求的跨域时间，单位是秒
    }
}
