package com.guhecloud.platform.base.standard.exception;

/**
 * 资源冲突异常
 */
public class ResourceConflictException extends ServiceException {

    private ResourceConflictException() {
        super("");
    }

    public ResourceConflictException(String message) {
        super(message);
    }

    public ResourceConflictException(String message, String code) {
        super(message, code);
    }

    public ResourceConflictException(String message, Object details) {
        super(message, details);
    }

    public ResourceConflictException(String message, String code, Object details) {
        super(message, code, details);
    }
}
