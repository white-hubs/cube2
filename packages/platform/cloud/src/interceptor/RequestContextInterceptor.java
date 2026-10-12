package com.guhecloud.platform.cloud.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestContextInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 重置请求上下文，标记为 inheritable-可遗传的，便于在开启的新线程中也能取得请求上下文
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).order(Integer.MIN_VALUE).addPathPatterns("/**");
    }

}
