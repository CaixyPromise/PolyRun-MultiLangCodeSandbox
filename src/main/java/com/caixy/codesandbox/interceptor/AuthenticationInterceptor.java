package com.caixy.codesandbox.interceptor;

import com.caixy.codesandbox.expection.CodeSandBoxException;
import com.caixy.codesandbox.models.enums.CodeSandBoxCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截器：鉴权拦截器
 *
 * @name: com.caixy.codesandbox.intercetor.AuthenticationInterceptor
 * @author: CAIXYPROMISE
 * @since: 2024-02-05 00:12
 **/
@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor
{
    @Value("${auth.request-header-text}")
    private String requestHeaderText;
    @Value("${auth.request-header-value}")
    private String requestHeaderValue;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String color = request.getHeader(requestHeaderText);
        return color != null && color.equals(requestHeaderValue);
    }

}
