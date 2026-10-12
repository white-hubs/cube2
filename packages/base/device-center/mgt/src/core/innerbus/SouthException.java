package com.guhecloud.cube.iot.mgt.core.innerbus;

/**
 * 南向总线异常
 */
public class SouthException extends RuntimeException{
    public SouthException() {
        super();
    }

    public SouthException(String message) {
        super(message);
    }

    public SouthException(String message, Throwable cause) {
        super(message, cause);
    }

    public SouthException(Throwable cause) {
        super(cause);
    }

    protected SouthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
