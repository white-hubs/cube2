package com.guhecloud.platform.base.model;

@Deprecated
public record LegacyResponse<T>(int code, String message, T data) {
    public static <T> LegacyResponse<T> of(int code, String message, T data) {
        return new LegacyResponse<>(code, message, data);
    }

    public static <T> LegacyResponse<T> success(T data) {
        return new LegacyResponse<>(200, "success", data);
    }

    public static <T> LegacyResponse<T> error(String message) {
        return new LegacyResponse<>(500, message, null);
    }
}
