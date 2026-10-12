package com.guhecloud.platform.base.standard.exception;

public class OuaaException extends ServiceException {
    public OuaaException(String message) {
        super(message);
    }

    public OuaaException(String message, String code, Object details) {
        super(message, code, details);
    }

    public OuaaException(ErrorTypeEnum errorType) {
        super(errorType.getMsg(), errorType.getCode(), errorType.getErrType());
    }
}
