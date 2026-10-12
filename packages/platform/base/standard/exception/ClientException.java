package com.guhecloud.platform.base.standard.exception;

/**
 * 通用客户端异常
 */
public class ClientException extends ServiceException {

    private ClientException() {
        super("");
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, String code) {
        super(message, code);
    }

    public ClientException(String message, Object details) {
        super(message, details);
    }

    public ClientException(String message, String code, Object details) {
        super(message, code, details);
    }

}
