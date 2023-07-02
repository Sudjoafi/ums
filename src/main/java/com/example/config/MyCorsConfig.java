package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/*
*cors处理跨域问题
* */
@Configuration
public class MyCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        //添加cors配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        //允许异步访问域名(不能写"*"，会导致cocke无法使用)
        configuration.addAllowedOrigin("http://localhost:8082");//请求的前端服务器地址
        //是否发送cocke信息
        configuration.setAllowCredentials(true);
        //允许的请求方式
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("PATCH");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("HEAD");
//        configuration.addAllowedMethod("*");

        //允许的头信息
        configuration.addAllowedHeader("*");

        //添加映射路径，我们拦截所有请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);//设置注册方法

        //返回新的CorsFilter
        return new CorsFilter(source);
    }
}
