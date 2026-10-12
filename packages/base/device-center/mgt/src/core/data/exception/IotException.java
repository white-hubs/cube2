package com.guhecloud.cube.iot.mgt.core.data.exception;

/**
 * 物联异常体系<br/>
 * 所有物联平台的异常都应该继承自此类<br/>
 */
public class IotException extends RuntimeException {
    public IotException(String message) {
        super(message);
    }

    public IotException(String message, Throwable cause) {
        super(message, cause);
    }

    public IotException(Throwable cause) {
        super(cause);
    }

    protected IotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
