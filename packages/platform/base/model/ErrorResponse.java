package com.guhecloud.platform.base.model;

/**
 * 通用错误返回结构体
 *
 * @param status    错误分类状态码，当为 HTTP 请求时，通常使用 HTTP 状态码
 * @param code      错误码，通常为业务错误码
 * @param message   错误信息，通常为错误描述
 * @param details   错误详情，通常为错误码对应的补充信息（禁止暴露内部服务堆栈等敏感信息），如：错误的字段、错误的参数等
 * @param timestamp 时间戳，通常为错误发生的时间戳
 * @param <T>       错误详情类型
 */
public record ErrorResponse<T>(int status, String code, String message, T details, Long timestamp) {

    public static <T> ErrorResponse<T> of(int status, String errorCode, String errorMessage, T errorDetails) {
        return new ErrorResponse<>(status, errorCode, errorMessage, errorDetails, System.currentTimeMillis());
    }

    public static <T> ErrorResponse<T> ofSimpleMessage(int status, String errorMessage) {
        return new ErrorResponse<>(status, null, errorMessage, null, System.currentTimeMillis());
    }
}
