package com.guhecloud.cube.iot.mgt.core.data.exception;

/**
 * 设备状态异常
 */
public class IllegalDeviceStatus extends IotException {
    public IllegalDeviceStatus(String message) {
        super(message);
    }

    public IllegalDeviceStatus(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDeviceStatus(Throwable cause) {
        super(cause);
    }

    protected IllegalDeviceStatus(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
