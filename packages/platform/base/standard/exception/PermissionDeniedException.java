package com.guhecloud.platform.base.standard.exception;

/**
 * 超出权限范围异常（Forbidden）
 */
public class PermissionDeniedException extends ServiceException {

    private PermissionDeniedException() {
        super("");
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, String code) {
        super(message, code);
    }

    public PermissionDeniedException(String message, Object details) {
        super(message, details);
    }

    public PermissionDeniedException(String message, String code, Object details) {
        super(message, code, details);
    }
}
