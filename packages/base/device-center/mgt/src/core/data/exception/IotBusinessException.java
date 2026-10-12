package com.guhecloud.cube.iot.mgt.core.data.exception;

public class IotBusinessException extends IotException {

    public IotBusinessException(String message) {
        super(message);
    }

    public IotBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public IotBusinessException(Throwable cause) {
        super(cause);
    }

    protected IotBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
