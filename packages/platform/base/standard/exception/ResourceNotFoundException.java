package com.guhecloud.platform.base.standard.exception;

/**
 * 资源未找到异常
 */
public class ResourceNotFoundException extends ServiceException {

    private ResourceNotFoundException() {
        super("");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, String code) {
        super(message, code);
    }

    public ResourceNotFoundException(String message, Object details) {
        super(message, details);
    }

    public ResourceNotFoundException(String message, String code, Object details) {
        super(message, code, details);
    }
}
