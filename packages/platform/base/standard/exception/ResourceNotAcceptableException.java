package com.guhecloud.platform.base.standard.exception;

/**
 * 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
 */
public class ResourceNotAcceptableException extends ServiceException {

    private ResourceNotAcceptableException() {
        super("");
    }

    public ResourceNotAcceptableException(String message) {
        super(message);
    }

    public ResourceNotAcceptableException(String message, String code) {
        super(message, code);
    }

    public ResourceNotAcceptableException(String message, Object details) {
        super(message, details);
    }

    public ResourceNotAcceptableException(String message, String code, Object details) {
        super(message, code, details);
    }
}
