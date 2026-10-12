package com.guhecloud.platform.security.token;

import com.guhecloud.platform.security.interceptor.TokenReplayValidateInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.method.HandlerMethod;

/**
 * 令牌重放验证器
 *
 * @author Woody
 * @date 2021/6/20
 * @see TokenReplayValidateInterceptor
 */
public interface TokenReplayValidator {
    /**
     * 判断令牌是否重放
     *
     * @param request Http请求对象
     * @param method  目标方法
     * @param jwt     请求令牌
     *
     * @return true/false
     */
    boolean isReplayed(HttpServletRequest request, HandlerMethod method, Jwt jwt);
}
