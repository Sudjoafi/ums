package com.example.config;

import com.example.interceptor.JwtInterCeptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* 注册拦截器
* */

@Configuration
public class MyInterCeptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterCeptor jwtInterCeptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(jwtInterCeptor);//注册
        registration.addPathPatterns("/**")//拦截所有
                //放行的路径
                .excludePathPatterns(
                        "/user/login",
                        "/user/info",
                        "/user/logout",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/**");
    }
}
