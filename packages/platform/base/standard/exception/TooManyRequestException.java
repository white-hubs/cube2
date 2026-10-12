package com.guhecloud.platform.base.standard.exception;

/**
 * 请求超出限制异常
 */
public class TooManyRequestException extends ServiceException {

    private TooManyRequestException() {
        super("");
    }

    public TooManyRequestException(String message) {
        super(message);
    }

    public TooManyRequestException(String message, String code) {
        super(message, code);
    }

    public TooManyRequestException(String message, Object details) {
        super(message, details);
    }

    public TooManyRequestException(String message, String code, Object details) {
        super(message, code, details);
    }
}
