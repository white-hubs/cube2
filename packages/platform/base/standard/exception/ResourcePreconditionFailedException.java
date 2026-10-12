package com.guhecloud.platform.base.standard.exception;

/**
 * 资源前置条件未满足异常
 */
public class ResourcePreconditionFailedException extends ServiceException {

    private ResourcePreconditionFailedException() {
        super("");
    }

    public ResourcePreconditionFailedException(String message) {
        super(message);
    }

    public ResourcePreconditionFailedException(String message, String code) {
        super(message, code);
    }

    public ResourcePreconditionFailedException(String message, Object details) {
        super(message, details);
    }

    public ResourcePreconditionFailedException(String message, String code, Object details) {
        super(message, code, details);
    }
}
