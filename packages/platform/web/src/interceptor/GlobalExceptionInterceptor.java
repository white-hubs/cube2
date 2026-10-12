package com.guhecloud.platform.base.interceptor;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.ImmutableMap;
import com.guhecloud.platform.base.model.ErrorResponse;
import com.guhecloud.platform.base.standard.exception.ClientException;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.standard.exception.OutOfRangeException;
import com.guhecloud.platform.base.standard.exception.PermissionDeniedException;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.standard.exception.ResourceNotAcceptableException;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.standard.exception.TooManyRequestException;
import com.guhecloud.platform.base.standard.exception.UnauthenticatedException;
import com.guhecloud.platform.base.standard.exception.UnimplementedException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.HttpUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常处理拦截器
 *
 * @author Woody
 * @date 2021/4/8
 */
@Slf4j
@Controller
@RestControllerAdvice
public class GlobalExceptionInterceptor implements ErrorController {
    /**
     * 参数绑定结果格式化
     *
     * @param result 参数绑定结果
     *
     * @return 参数名称/异常消息映射表
     */
    private Map<String, String> format(BindingResult result) {
        List<FieldError> errors = ObjectUtils.ifNull(result, BindingResult::getFieldErrors);
        if (CollectionUtils.isEmpty(errors)) {
            return Collections.emptyMap();
        }
        return errors.stream()
                .collect(Collectors.toMap(FieldError::getField, e -> ObjectUtils.ifNull(e.getDefaultMessage(), StringUtils.EMPTY_STRING), (o, n) -> n));
    }

    /**
     * 参数类型异常格式化
     *
     * @param e 参数类型异常
     *
     * @return 参数名称/异常消息映射表
     */
    private Map<String, String> format(MethodArgumentTypeMismatchException e) {
        return ImmutableMap.of(e.getName(), ObjectUtils.ifNull(e.getRequiredType(), Class::getSimpleName, () -> StringUtils.EMPTY_STRING));
    }

    /**
     * 参数类型异常格式化
     *
     * @param e 参数校验异常
     *
     * @return 参数名称/异常消息映射表
     */
    private Map<String, String> format(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException) {
            List<JsonMappingException.Reference> references = ((InvalidFormatException) cause).getPath();
            if (!CollectionUtils.isEmpty(references)) {
                return references.stream().collect(Collectors.toMap(JsonMappingException.Reference::getFieldName, r -> "Invalid format"));
            }
        }
        return Collections.emptyMap();
    }

    /**
     * 打印异常日志
     *
     * @param status Http 状态枚举
     * @param e      异常对象
     */
    private void logException(@NonNull HttpStatus status, @NonNull Exception e) {
        String message = e.getMessage();
        if (StringUtils.isEmpty(message)) {
            message = status.getReasonPhrase();
        }
        if (status.is4xxClientError()) {
            log.warn("Client error[{}]: {}", status.value(), message);
        } else if (status.is5xxServerError()) {
            log.error("Server error[{}]: {}", status.value(), message, e);
        }
    }

    /**
     * 异常方法
     *
     * @param response 请求响应对象
     * @param e        异常对象
     *
     * @return 异常结果
     */
    @RequestMapping("${server.error.path:/error}")
    public ErrorResponse<?> error(HttpServletResponse response, Exception e) {
        var status = HttpUtils.getStatus(response.getStatus());
        this.logException(status, e);
        HttpUtils.initializeUnifiedResponseHeader(response);
        return ErrorResponse.of(status.value(), null, status.getReasonPhrase(), null);
    }

    /**
     * 异常转换
     *
     * @param response 请求响应对象
     * @param e        异常对象
     *
     * @return 请求结果
     */
    @ExceptionHandler(Exception.class)
    public ErrorResponse<?> exception(HttpServletResponse response, Exception e) {
        var result = this.exception2ErrorResponse(e);
        var status = HttpUtils.getStatus(result.status());
        this.logException(status, e);
        response.setStatus(status.value());
        HttpUtils.initializeUnifiedResponseHeader(response);
        return result;
    }

    /**
     * 将异常转换成结果对象
     *
     * @param e 异常对象
     *
     * @return 结果对象
     */
    private ErrorResponse<?> exception2ErrorResponse(Exception e) {
        return switch (e) {
            case ServiceException ex -> serviceException2ErrorResponse(ex);
            case MethodArgumentTypeMismatchException ex ->
                    ErrorResponse.of(HttpStatus.PRECONDITION_FAILED.value(), null, null, this.format(((MethodArgumentTypeMismatchException) e)));
            case BindException ex ->
                    ErrorResponse.of(HttpStatus.PRECONDITION_FAILED.value(), null, "请求失败参数错误", this.format(((BindException) e).getBindingResult()));
            case HttpMessageNotReadableException ex -> {
                Map<String, String> errors = this.format((HttpMessageNotReadableException) e);
                yield MapUtils.isEmpty(errors)
                      ? ErrorResponse.ofSimpleMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase())
                      : ErrorResponse.of(HttpStatus.PRECONDITION_FAILED.value(), null, HttpStatus.PRECONDITION_FAILED.getReasonPhrase(), errors);
            }
            case HttpMessageConversionException e1 ->
                    ErrorResponse.ofSimpleMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            case ServletRequestBindingException e2 ->
                    ErrorResponse.ofSimpleMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            case HttpMediaTypeNotSupportedException ex ->
                    ErrorResponse.ofSimpleMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase());
            case HttpMediaTypeNotAcceptableException ex ->
                    ErrorResponse.ofSimpleMessage(HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
            case HttpRequestMethodNotSupportedException ex ->
                    ErrorResponse.ofSimpleMessage(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());

            default -> ErrorResponse.ofSimpleMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务内部错误");
        };
    }

    private ErrorResponse<?> serviceException2ErrorResponse(ServiceException e) {

        var status = switch (e) {
            case OuaaException _ignore -> HttpStatus.PRECONDITION_FAILED;
            case ClientException _ignore -> HttpStatus.BAD_REQUEST;
            case InvalidArgumentException _ignore -> HttpStatus.PRECONDITION_FAILED;
            case OutOfRangeException _ignore -> HttpStatus.PRECONDITION_FAILED;
            case PermissionDeniedException _ignore -> HttpStatus.FORBIDDEN;
            case ResourceConflictException _ignore -> HttpStatus.CONFLICT;
            case ResourceNotAcceptableException _ignore -> HttpStatus.NOT_ACCEPTABLE;
            case ResourceNotFoundException _ignore -> HttpStatus.NOT_FOUND;
            case ResourcePreconditionFailedException _ignore -> HttpStatus.PRECONDITION_FAILED;
            case TooManyRequestException _ignore -> HttpStatus.TOO_MANY_REQUESTS;
            case UnauthenticatedException _ignore -> HttpStatus.UNAUTHORIZED;
            case UnimplementedException _ignore -> HttpStatus.NOT_IMPLEMENTED;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return ErrorResponse.of(status.value(), e.getCode(), e.getMessage(), e.getDetails());
    }
}
