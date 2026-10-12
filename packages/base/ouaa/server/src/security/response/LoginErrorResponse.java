package com.guhecloud.cube.biz.ouaa.server.security.response;

public record LoginErrorResponse<T>(int status, String message, String code, T details, Long timestamp, String error,
                                    String error_description) {
    public static <T> LoginErrorResponse<T> of(int status, String message, String code, T details) {
        return new LoginErrorResponse<>(status, message, code, details, System.currentTimeMillis(), code, message);
    }
}
