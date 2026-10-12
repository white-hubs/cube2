package com.guhecloud.platform.base.standard.exception;

/**
 * 功能未实现异常
 */
public class UnimplementedException extends ServiceException {

    private UnimplementedException() {
        super("");
    }

    public UnimplementedException(String message) {
        super(message);
    }

    public UnimplementedException(String message, String code) {
        super(message, code);
    }

    public UnimplementedException(String message, Object details) {
        super(message, details);
    }

    public UnimplementedException(String message, String code, Object details) {
        super(message, code, details);
    }
}
