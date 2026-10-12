package com.guhecloud.cube.iot.mgt.north.websocket.util;

import com.guhecloud.cube.iot.mgt.core.data.exception.IotException;

public class SubscribeInfoParseException extends IotException {
    public SubscribeInfoParseException(String message) {
        super(message);
    }

    public SubscribeInfoParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscribeInfoParseException(Throwable cause) {
        super(cause);
    }

    protected SubscribeInfoParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
