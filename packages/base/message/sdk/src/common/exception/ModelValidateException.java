package com.guhecloud.cube.base.message.sdk.common.exception;

public class ModelValidateException extends RuntimeException {

    public ModelValidateException(String message) {
        super(message);
    }

    public ModelValidateException(String message, Exception e) {
        super(message, e);
    }
}
