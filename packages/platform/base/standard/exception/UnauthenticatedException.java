package com.guhecloud.platform.base.standard.exception;

/**
 * 认证失败异常
 */
public class UnauthenticatedException extends ServiceException {

    private UnauthenticatedException() {
        super("");
    }

    public UnauthenticatedException(String message) {
        super(message);
    }

    public UnauthenticatedException(String message, String code) {
        super(message, code);
    }

    public UnauthenticatedException(String message, Object details) {
        super(message, details);
    }

    public UnauthenticatedException(String message, String code, Object details) {
        super(message, code, details);
    }
}
