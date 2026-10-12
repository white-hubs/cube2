package com.guhecloud.platform.dcm.exception;

import com.guhecloud.platform.base.standard.exception.ServiceException;

import java.io.Serializable;

public class DcmRuntimeException extends ServiceException {
    private static final String CODE = "DCM_RUNTIME_EXCEPTION";

    public DcmRuntimeException() {
        super("", CODE, null);
    }

    public DcmRuntimeException(String message) {
        super(message, CODE, null);
    }

    public DcmRuntimeException(String message, Throwable cause) {
        super(message, CODE, cause);
    }

    public DcmRuntimeException(String message, Serializable details) {
        super(message, CODE, details);
    }

    public DcmRuntimeException(Throwable cause) {
        super("", CODE, cause);
    }
}
