package com.guhecloud.platform.security.interceptor;

import com.guhecloud.platform.base.standard.exception.PermissionDeniedException;
import com.guhecloud.platform.security.annotation.Repeatable;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.token.TokenReplayValidator;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 令牌重放验证拦截器
 *
 * @author Woody
 * @date 2021/7/3
 */
@Slf4j
@Component
@ConditionalOnBean(TokenReplayValidator.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenReplayValidateInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private final TokenReplayValidator tokenReplayValidator;

    @PostConstruct
    public void initialize() {
        log.info("Loading token replay validator: {}", this.tokenReplayValidator);
    }

    /**
     * 判断接口方法是否支持请求重放
     *
     * @param method 目标接口方法
     *
     * @return true/false
     */
    private boolean isRepeatableMethod(HandlerMethod method) {
        Repeatable repeatable = method.getMethodAnnotation(Repeatable.class);
        if (repeatable == null) {
            repeatable = method.getBeanType().getAnnotation(Repeatable.class);
        }
        return repeatable == null || repeatable.value();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 防重放验证
        if (handler instanceof HandlerMethod && !this.isRepeatableMethod((HandlerMethod) handler)) {
            Jwt jwt = SecurityContextHolder.getJwt(true);
            if (jwt == null) {
                throw new PermissionDeniedException("Token unspecified");
            } else if (this.tokenReplayValidator.isReplayed(request, (HandlerMethod) handler, jwt)) {
                throw new AccessDeniedException("Token replayed");
            }
        }
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).order(Integer.MIN_VALUE).addPathPatterns("/**");
    }
}
