package com.guhecloud.cube.biz.ouaa.server.security.exceptions;

import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Setter
@Getter
public class LoginException extends AuthenticationException {
    private int httpStatus = 401;
    private String errorCode;
    private Object details;

    public LoginException(HttpStatus httpStatus, ErrorTypeEnum errorType, String details) {
        super(errorType.getMsg());
        this.httpStatus = httpStatus.value();
        this.errorCode = errorType.getCode();
        this.details = details;
    }

    public LoginException(ErrorTypeEnum errorType) {
        super(errorType.getMsg());
        this.errorCode = errorType.getCode();
        this.details = errorType.getErrType();
    }

    public LoginException(String msg, String code, Object errorType) {
        super(msg);
        this.errorCode = code;
        this.details = errorType;
    }
}
