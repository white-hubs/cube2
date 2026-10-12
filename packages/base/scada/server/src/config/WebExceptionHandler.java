package com.guhecloud.kaihe.scada.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceDataException;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceException;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.ResponseResult;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@ControllerAdvice
@ResponseBody
public class WebExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult exception(Exception e, HttpServletRequest request) {
        return handle(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误", e, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseResult httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return handle(HttpStatus.METHOD_NOT_ALLOWED.value(), "不允许的http方法", e, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseResult accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        return handle(HttpStatus.FORBIDDEN.value(), "禁止访问", e, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return handle(HttpStatus.BAD_REQUEST.value(), contert((MethodArgumentTypeMismatchException) e), e, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return handle(HttpStatus.BAD_REQUEST.value(), contert((MethodArgumentNotValidException) e), e, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult ConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        return handle(HttpStatus.BAD_REQUEST.value(), contert((ConstraintViolationException) e), e, request);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult serviceException(ServiceException e, HttpServletRequest request) {
        return handle(HttpStatus.OK.value(), e.getMessage(), e, request);
    }

    @ExceptionHandler(ServiceDataException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult serviceDataException(ServiceDataException e, HttpServletRequest request) {
        return handle(HttpStatus.OK.value(), e, request);
    }

    private ResponseResult handle(int code, String msg, Exception e, HttpServletRequest request) {
        log.error(msg, e);
        return ResponseResult.builder().success(false).message(msg).build();
    }

    private ResponseResult handle(int code, ServiceDataException e, HttpServletRequest request) {
        return ResponseResult.builder().success(false).errors(e.getErrors()).build();
    }

    private String contert(MethodArgumentTypeMismatchException e) {
        try {
            Map<String, String> errors = new HashMap<String, String>();
            errors.put(e.getName(), "必须为" + translate(e.getRequiredType()));
            return objectMapper.writeValueAsString(errors);
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String contert(MethodArgumentNotValidException e) {
        try {
            Map<String, String> errors = new HashMap<String, String>();
            for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
                String name = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errors.put(name, message);
            }
            return objectMapper.writeValueAsString(errors);
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String contert(ConstraintViolationException e) {
        try {
            Map<String, String> errors = new HashMap<String, String>();
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : constraintViolations) {
                String name = violation.getPropertyPath().toString();
                String[] names = name.split("\\.");
                StringBuilder stringBuilder = new StringBuilder();
                if (names != null && names.length > 2) {
                    for (int i = 2; i < names.length; i++) {
                        if (i == 2) {
                            stringBuilder.append(names[i]);
                        } else {
                            stringBuilder.append("[" + names[i] + "]");
                        }
                    }
                }
                errors.put(StringUtils.isEmpty(stringBuilder.toString()) ? name : stringBuilder.toString(), violation.getMessage());
            }
            return objectMapper.writeValueAsString(errors);
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String translate(Class<?> clazz) {
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return "整数";
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return "浮点数";
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return "双精度浮点数";
        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return "布尔型";
        } else {
            return clazz.getSimpleName();
        }
    }

}
