package com.guhecloud.platform.database.interceptor;

import com.guhecloud.platform.base.model.ErrorResponse;
import com.guhecloud.platform.base.util.ExceptionUtils;
import com.guhecloud.platform.base.util.HttpUtils;
import com.guhecloud.platform.database.exception.UniqueViolationException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 数据库异常处理拦截器
 *
 * @author Woody
 * @date 2021/7/14
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseExceptionInterceptor {
    /**
     * 数据唯一约束异常
     *
     * @param response 请求响应对象
     * @param e        异常对象
     *
     * @return 请求结果
     */
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(UniqueViolationException.class)
    public ErrorResponse<?> exception(HttpServletResponse response, UniqueViolationException e) {
        log.warn("Client error[{}]: {}", HttpStatus.PRECONDITION_FAILED.value(), e.getMessage());
        var result = ErrorResponse.of(HttpStatus.PRECONDITION_FAILED.value(), null, "数据重复", e.getErrors());
        response.setStatus(result.status());
        HttpUtils.initializeUnifiedResponseHeader(response);
        return result;
    }

    /**
     * 数据持久化异常转换
     *
     * @param response 请求响应对象
     * @param e        异常对象
     *
     * @return 请求结果
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public ErrorResponse<?> exception(HttpServletResponse response, MyBatisSystemException e) {
        UniqueViolationException uniqueViolationException = ExceptionUtils.lookup(e, UniqueViolationException.class);
        if (uniqueViolationException != null) {
            return this.exception(response, uniqueViolationException);
        }
        log.error("Server error[{}]: {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e);
        var result = ErrorResponse.ofSimpleMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务内部错误");
        response.setStatus(result.status());
        HttpUtils.initializeUnifiedResponseHeader(response);
        return result;
    }
}
