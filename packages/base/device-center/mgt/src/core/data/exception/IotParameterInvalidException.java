package com.guhecloud.cube.iot.mgt.core.data.exception;

public class IotParameterInvalidException extends IotBusinessException {

    public IotParameterInvalidException(String message) {
        super(message);
    }



    public IotParameterInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public IotParameterInvalidException(Throwable cause) {
        super(cause);
    }

    protected IotParameterInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IotParameterInvalidException(String param, String msg) {
        super(String.format("参数[%s]无效: %s", param, msg));
    }
}
