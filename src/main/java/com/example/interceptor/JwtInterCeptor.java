package com.example.interceptor;

import com.alibaba.fastjson2.JSON;
import com.example.common.Result;
import com.example.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* JWT 定义拦截器
* */

@Slf4j
@Component
public class JwtInterCeptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        log.debug(request.getRequestURI() + "等待验证" + token);
        if (token != null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证通过");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败，禁止访问");
        response.setContentType("application/json;charset=utf-8");
        Result<Object> fail = Result.fail(20003, "jwt无效，请重新登陆");
        response.getWriter().write(JSON.toJSONString(fail));
        return false;//拦截
    }
}