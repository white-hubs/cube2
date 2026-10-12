package com.guhecloud.cube.iot.north.bridge.consumer.util;

public class SubscribeFailException extends RuntimeException{
    public SubscribeFailException(String message) {
        super(message);
    }

    public SubscribeFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
