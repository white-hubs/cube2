package com.guhecloud.platform.base.standard.exception;

/**
 * 无效参数异常
 */
public class InvalidArgumentException extends ServiceException {

    private InvalidArgumentException() {
        super("");
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, String code) {
        super(message, code);
    }

    public InvalidArgumentException(String message, Object details) {
        super(message, details);
    }

    public InvalidArgumentException(String message, String code, Object details) {
        super(message, code, details);
    }
}
