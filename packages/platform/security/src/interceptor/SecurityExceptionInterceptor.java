package com.guhecloud.platform.security.interceptor;

import com.guhecloud.platform.base.model.ErrorResponse;
import com.guhecloud.platform.base.util.HttpUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 安全异常处理拦截器
 *
 * @author Woody
 * @date 2021/7/3
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionInterceptor {
    /**
     * 认证异常转换
     *
     * @param response 请求响应对象
     * @param e        异常对象
     *
     * @return 请求结果
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse<?> exception(HttpServletResponse response, AuthenticationException e) {
        log.warn("Client error[{}]: {}", HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        HttpUtils.initializeUnifiedResponseHeader(response);
        return ErrorResponse.ofSimpleMessage(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    /**
     * 拒绝访问异常转换
     *
     * @param response 请求响应对象
     * @param e        异常对象
     *
     * @return 请求结果
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse<?> exception(HttpServletResponse response, AccessDeniedException e) {
        log.warn("Client error[{}]: {}", HttpStatus.FORBIDDEN.value(), e.getMessage());
        HttpUtils.initializeUnifiedResponseHeader(response);
        return ErrorResponse.ofSimpleMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
    }
}
