package com.guhecloud.platform.base.standard.exception;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用服务异常
 */
public class ServiceException extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 异常补充信息，用于传递更多信息给 client，禁止返回敏感信息
     */
    private final Object details;

    /**
     * 异常补充码，用于需要区分更细节场景时使用
     */
    private final String code;

    private ServiceException() {
        super("");
        this.details = null;
        this.code = "";
    }

    public ServiceException(String message) {
        super(message);
        this.details = null;
        this.code = "";
    }

    /**
     * @param message 异常信息，该消息会原样返回给各 client
     * @param details 异常补充信息
     */
    public ServiceException(String message, Object details) {
        super(message);
        this.details = details;
        this.code = "";
    }

    public ServiceException(String message, String code) {
        super(message);
        this.details = null;
        this.code = code;
    }

    public ServiceException(String message, String code, Object details) {
        super(message);
        this.details = details;
        this.code = code;
    }

    public Object getDetails() {
        return details;
    }

    public String getCode() {
        return code;
    }
}
