package com.guhecloud.platform.base.standard.exception;

/**
 * 超出范围异常，如分页限制等
 */
public class OutOfRangeException extends ServiceException {

    private OutOfRangeException() {
        super("");
    }

    public OutOfRangeException(String message) {
        super(message);
    }

    public OutOfRangeException(String message, String code) {
        super(message, code);
    }

    public OutOfRangeException(String message, Object details) {
        super(message, details);
    }

    public OutOfRangeException(String message, String code, Object details) {
        super(message, code, details);
    }
}
