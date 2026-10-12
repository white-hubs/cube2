package com.guhecloud.platform.base.standard.exception;

/**
 * 错误规格接口
 */
public interface ErrorSpec {
    String errorCode();

    String errorMessage();

    void setErrorMessage(String message);
}
